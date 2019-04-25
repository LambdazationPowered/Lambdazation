package org.lambdazation.common.util.reactive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

import org.lambdazation.common.util.data.Maybe;
import org.lambdazation.common.util.data.Product;
import org.lambdazation.common.util.data.Unit;
import org.lambdazation.common.util.eval.Thunk;

public final class Reactive {
	private boolean responsive;
	private BooleanSupplier processing;
	private final List<Port<?>> ports;

	Reactive(boolean responsive, BooleanSupplier processing, List<Port<?>> ports) {
		this.responsive = responsive;
		this.processing = processing;
		this.ports = ports;
	}

	public void resume() {
		if (responsive)
			return;
		responsive = true;
		ports.forEach(Port::attach);
	}

	public void suspend() {
		if (!responsive)
			return;
		responsive = false;
		ports.forEach(Port::detach);
	}

	public State state() {
		return processing.getAsBoolean() ? State.PROCESSING : responsive ? State.RESUMED : State.SUSPENDED;
	}

	public static Reactive build(Flow<Unit> flow) {
		FlowEvaluator flowEvaluator = new FlowEvaluator(flow);
		List<Event<Runnable>> outputEvents = flowEvaluator.eval();

		Map<Event.FlowInput<?>, RelationEntry> inputEventRelationEntries = new HashMap<>();

		outputEvents.forEach(outputEvent -> {
			NodeAnalyzer nodeAnalyzer = new NodeAnalyzer(outputEvent, inputEventRelationEntries);
			nodeAnalyzer.analyze();
		});

		Lock reactiveLock = new ReentrantLock();
		AtomicInteger activePort = new AtomicInteger(0);
		Map<Behavior.FlowStore<?>, ?> storeBehaviorValues = new HashMap<>();

		boolean responsive = false;
		BooleanSupplier processing = () -> activePort.get() > 0;
		List<Port<?>> ports = new ArrayList<>();

		inputEventRelationEntries.forEach((inputEvent, relationEntry) -> {
			Port<?> port = new Port<>(inputEvent.source, currentPort -> value -> {
				if (!currentPort.responsive)
					return;
				activePort.incrementAndGet();
				try {
					NodeEvaluator nodeEvaluator = new NodeEvaluator(reactiveLock, inputEventRelationEntries, storeBehaviorValues,
						Collections.singletonMap(inputEvent, value));
					nodeEvaluator.eval();
				} finally {
					activePort.decrementAndGet();
				}
			});
			ports.add(port);
		});

		return new Reactive(responsive, processing, ports);
	}

	static final class FlowEvaluator implements Flow.EvalVistor {
		final Flow<Unit> flow;
		final List<Event<Runnable>> outputEvents;

		FlowEvaluator(Flow<Unit> flow) {
			this.flow = flow;
			this.outputEvents = new ArrayList<>();
		}

		@Override
		public <A, B> B visit(Flow.Fmap<A, B> flow) {
			A a = accept(flow.parent);
			B b = flow.f.apply(a);
			return b;
		}

		@Override
		public <A, B> B visit(Flow.Apply<A, B> flow) {
			A a = accept(flow.parent);
			Function<A, B> f = accept(flow.flow);
			B b = f.apply(a);
			return b;
		}

		@Override
		public <A, B> B visit(Flow.Compose<A, B> flow) {
			A a = accept(flow.parent);
			Flow<B> flow0 = flow.f.apply(a);
			B b = accept(flow0);
			return b;
		}

		@Override
		public <A> A visit(Flow.Pure<A> flow) {
			A a = flow.a;
			return a;
		}

		@Override
		public <A, B> B visit(Flow.Efix<A, B> flow) {
			B b = Thunk.<Product<Event<A>, B>> mfix(lazy -> {
				Event<A> event = new Event.FlowEfix<>(lazy.fmap(Product.projectionLeft()));
				Flow<Product<Event<A>, B>> flow0 = flow.f.apply(event);
				Product<Event<A>, B> product = accept(flow0);
				return Thunk.pure(product);
			}).fmap(Product.projectionRight()).get();
			return b;
		}

		@Override
		public <A, B> B visit(Flow.Bfix<A, B> flow) {
			B b = Thunk.<Product<Behavior<A>, B>> mfix(lazy -> {
				Behavior<A> event = new Behavior.FlowBfix<>(lazy.fmap(Product.projectionLeft()));
				Flow<Product<Behavior<A>, B>> flow0 = flow.f.apply(event);
				Product<Behavior<A>, B> product = accept(flow0);
				return Thunk.pure(product);
			}).fmap(Product.projectionRight()).get();
			return b;
		}

		@Override
		public <A> Behavior<A> visit(Flow.Store<A> flow) {
			Behavior<A> behavior = new Behavior.FlowStore<A>(flow.a, flow.event);
			return behavior;
		}

		@Override
		public <A, B> Event<B> visit(Flow.Retrieve<A, B> flow) {
			Event<B> event = new Event.FlowRetrieve<>(flow.behavior, flow.event);
			return event;
		}

		@Override
		public Unit visit(Flow.Output flow) {
			outputEvents.add(flow.event.get());

			Unit unit = Unit.UNIT;
			return unit;
		}

		@Override
		public <A> Event<A> visit(Flow.Input<A> flow) {
			Event<A> event = new Event.FlowInput<>(flow.source);
			return event;
		}

		List<Event<Runnable>> eval() {
			accept(flow);
			return outputEvents;
		}
	}

	static final class NodeAnalyzer implements Event.TraverseVistor<TraverseLog>, Behavior.TraverseVistor<TraverseLog> {
		final Event<Runnable> outputEvent;
		final Map<Event.FlowInput<?>, RelationEntry> inputEventRelationEntries;
		final Set<Event.FlowInput<?>> inputEvents;

		NodeAnalyzer(Event<Runnable> outputEvent, Map<Event.FlowInput<?>, RelationEntry> inputEventRelationEntries) {
			this.outputEvent = outputEvent;
			this.inputEventRelationEntries = inputEventRelationEntries;
			this.inputEvents = new HashSet<>();
		}

		@Override
		public <A, B> void visit(Event.Fmap<A, B> event, TraverseLog t) {
			if (t.traverse(event, event.parent.get()))
				accept(event.parent, t.move());
		}

		@Override
		public <A> void visit(Event.Filter<A> event, TraverseLog t) {
			if (t.traverse(event, event.parent.get()))
				accept(event.parent, t.move());
		}

		@Override
		public <A, B, C> void visit(Event.Combine<A, B, C> event, TraverseLog t) {
			if (t.traverse(event, event.event1.get()))
				accept(event.event1, t.copy());
			if (t.traverse(event, event.event2.get()))
				accept(event.event2, t.move());
		}

		@Override
		public <A> void visit(Event.Mempty<A> event, TraverseLog t) {

		}

		@Override
		public <A> void visit(Event.Mappend<A> event, TraverseLog t) {
			if (t.traverse(event, event.event1.get()))
				accept(event.event1, t.copy());
			if (t.traverse(event, event.event2.get()))
				accept(event.event2, t.move());
		}

		@Override
		public <A> void visit(Event.FlowEfix<A> event, TraverseLog t) {
			accept(event.get(), t.move());
		}

		@Override
		public <A, B> void visit(Event.FlowRetrieve<A, B> event, TraverseLog t) {
			if (t.traverse(event, event.behavior.get()))
				accept(event.behavior, t.copy());
			if (t.traverse(event, event.event.get()))
				accept(event.event, t.move());
		}

		@Override
		public <A> void visit(Event.FlowInput<A> event, TraverseLog t) {
			inputEvents.add(event);
			RelationEntry relationEntry = inputEventRelationEntries.get(event);
			if (relationEntry == null)
				inputEventRelationEntries.put(event, relationEntry = new RelationEntry());
			relationEntry.merge(t.move());
		}

		@Override
		public <A, B> void visit(Behavior.Fmap<A, B> behavior, TraverseLog t) {
			if (t.traverse(behavior, behavior.parent.get()))
				accept(behavior.parent, t.move());
		}

		@Override
		public <A, B> void visit(Behavior.Apply<A, B> behavior, TraverseLog t) {
			if (t.traverse(behavior, behavior.parent.get()))
				accept(behavior.parent, t.copy());
			if (t.traverse(behavior, behavior.behavior.get()))
				accept(behavior.behavior, t.move());
		}

		@Override
		public <A> void visit(Behavior.Pure<A> behavior, TraverseLog t) {

		}

		@Override
		public <A> void visit(Behavior.FlowBfix<A> behavior, TraverseLog t) {
			accept(behavior.get(), t.move());
		}

		@Override
		public <A> void visit(Behavior.FlowStore<A> behavior, TraverseLog t) {
			if (t.traverse(behavior, behavior.event.get()))
				accept(behavior.event, t.move());
		}

		void analyze() {
			accept(outputEvent, new TraverseLog());
			inputEvents.forEach(inputEvent -> inputEventRelationEntries.get(inputEvent).targetOutputEvents.add(outputEvent));
		}
	}

	static final class NodeEvaluator implements Event.EvalVistor, Behavior.EvalVistor {
		final Lock reactiveLock;
		final Map<Event.FlowInput<?>, RelationEntry> inputEventRelationEntries;
		final Map<Behavior.FlowStore<?>, ?> storeBehaviorValues;
		final Map<Event.FlowInput<?>, ?> firedInputEventValues;
		final Map<Event<?>, Maybe<?>> eventValues;
		final Map<Behavior<?>, ?> behaviorValues;

		NodeEvaluator(Lock reactiveLock, Map<Event.FlowInput<?>, RelationEntry> inputEventRelationEntries,
			Map<Behavior.FlowStore<?>, ?> storeBehaviorValues, Map<Event.FlowInput<?>, ?> firedInputEventValues) {
			this.reactiveLock = reactiveLock;
			this.inputEventRelationEntries = inputEventRelationEntries;
			this.storeBehaviorValues = storeBehaviorValues;
			this.firedInputEventValues = firedInputEventValues;
			this.eventValues = new HashMap<>();
			this.behaviorValues = new HashMap<>();
		}

		@SuppressWarnings("unchecked")
		<A> Map<Behavior.FlowStore<?>, A> storeBehaviorValues() {
			return (Map<Behavior.FlowStore<?>, A>) (Map<?, ?>) storeBehaviorValues;
		}

		@SuppressWarnings("unchecked")
		<A> Map<Event.FlowInput<A>, A> firedInputEventValues() {
			return (Map<Event.FlowInput<A>, A>) (Map<?, ?>) firedInputEventValues;
		}

		@SuppressWarnings("unchecked")
		<A> Map<Event<A>, Maybe<A>> eventValues() {
			return (Map<Event<A>, Maybe<A>>) (Map<?, ?>) eventValues;
		}

		@SuppressWarnings("unchecked")
		<A> Map<Behavior<A>, A> behaviorValues() {
			return (Map<Behavior<A>, A>) (Map<?, ?>) behaviorValues;
		}

		@Override
		public <A, B> Maybe<B> visit(Event.Fmap<A, B> event) {
			Maybe<A> eventParentValue = eval(event.parent);

			Maybe<B> value = eventParentValue.map(event.f);
			this.<B> eventValues().put(event, value);
			return value;
		}

		@Override
		public <A> Maybe<A> visit(Event.Filter<A> event) {
			Maybe<A> eventParentValue = eval(event.parent);

			Maybe<A> value = eventParentValue.filter(event.p);
			this.<A> eventValues().put(event, value);
			return value;
		}

		@Override
		public <A, B, C> Maybe<C> visit(Event.Combine<A, B, C> event) {
			Maybe<A> eventEvent1Value = eval(event.event1);
			Maybe<B> eventEvent2Value = eval(event.event2);

			Maybe<C> value = Maybe.combine(event.f, event.g, event.h, eventEvent1Value, eventEvent2Value);
			this.<C> eventValues().put(event, value);
			return value;
		}

		@Override
		public <A> Maybe<A> visit(Event.Mempty<A> event) {
			Maybe<A> value = Maybe.ofNothing();
			this.<A> eventValues().put(event, value);
			return value;
		}

		@Override
		public <A> Maybe<A> visit(Event.Mappend<A> event) {
			Maybe<A> eventEvent1Value = eval(event.event1);
			Maybe<A> eventEvent2Value = eval(event.event2);

			Maybe<A> value = Maybe.mappend(event.f, eventEvent1Value, eventEvent2Value);
			this.<A> eventValues().put(event, value);
			return value;
		}

		@Override
		public <A> Maybe<A> visit(Event.FlowEfix<A> event) {
			return eval(event.get());
		}

		@Override
		public <A, B> Maybe<B> visit(Event.FlowRetrieve<A, B> event) {
			Function<A, B> eventBehaviorValue = eval(event.behavior);
			Maybe<A> eventEventValue = eval(event.event);

			Maybe<B> value = eventEventValue.map(eventBehaviorValue);
			this.<B> eventValues().put(event, value);
			return value;
		}

		@Override
		public <A> Maybe<A> visit(Event.FlowInput<A> event) {
			Maybe<A> value = this.<A> firedInputEventValues().containsKey(event)
				? Maybe.ofJust(this.<A> firedInputEventValues().get(event))
				: Maybe.ofNothing();
			this.<A> eventValues().put(event, value);
			return value;
		}

		@Override
		public <A, B> B visit(Behavior.Fmap<A, B> behavior) {
			A behaviorParentValue = eval(behavior.parent);

			B value = behavior.f.apply(behaviorParentValue);
			this.<B> behaviorValues().put(behavior, value);
			return value;
		}

		@Override
		public <A, B> B visit(Behavior.Apply<A, B> behavior) {
			A behaviorParentValue = eval(behavior.parent);
			Function<A, B> behavioBehaviorValue = eval(behavior.behavior);

			B value = behavioBehaviorValue.apply(behaviorParentValue);
			this.<B> behaviorValues().put(behavior, value);
			return value;
		}

		@Override
		public <A> A visit(Behavior.Pure<A> behavior) {
			A value = behavior.a;
			this.<A> behaviorValues().put(behavior, value);
			return value;
		}

		@Override
		public <A> A visit(Behavior.FlowBfix<A> behavior) {
			return eval(behavior.get());
		}

		@Override
		public <A> A visit(Behavior.FlowStore<A> behavior) {
			A value = this.<A> storeBehaviorValues().containsKey(behavior)
				? this.<A> storeBehaviorValues().get(behavior)
				: behavior.a;
			this.<A> behaviorValues().put(behavior, value);
			return value;
		}

		void eval() {
			List<Runnable> actions = new ArrayList<>();
			reactiveLock.lock();
			try {
				for (Event.FlowInput<?> inputEvent : firedInputEventValues.keySet()) {
					RelationEntry relationEntry = inputEventRelationEntries.get(inputEvent);
					for (Event<Runnable> outputEvent : relationEntry.targetOutputEvents) {
						Maybe<Runnable> eventValue = eval(outputEvent);
						eventValue.ifJust(value -> actions.add(value));
					}
					for (Behavior.FlowStore<?> storeBehavior : relationEntry.targetStoreBehaviors) {
						eval(storeBehavior);
						Maybe<?> eventValue = eval(storeBehavior.event);
						eventValue.ifJust(value -> storeBehaviorValues().put(storeBehavior, value));
					}
				}
			} finally {
				reactiveLock.unlock();
			}
			for (Runnable action : actions)
				action.run();
		}

		<A> Maybe<A> eval(Event<A> event) {
			if (eventValues.containsKey(event))
				return this.<A> eventValues().get(event);
			Maybe<A> value = accept(event);
			return value;
		}

		<A> A eval(Behavior<A> behavior) {
			if (behaviorValues.containsKey(behavior))
				return this.<A> behaviorValues().get(behavior);
			A value = accept(behavior);
			return value;
		}
	}

	static final class TraverseLog {
		final Set<Path> paths;
		final Set<Event<?>> events;
		final Set<Behavior<?>> behaviors;

		TraverseLog() {
			this.paths = new HashSet<>();
			this.events = new HashSet<>();
			this.behaviors = new HashSet<>();
		}

		TraverseLog(Set<Path> paths, Set<Event<?>> events, Set<Behavior<?>> behaviors) {
			this.paths = paths;
			this.events = events;
			this.behaviors = behaviors;
		}

		boolean traverse(Event<?> from, Event<?> to) {
			events.add(to);
			return paths.add(new Path(from, to));
		}

		boolean traverse(Event<?> from, Behavior<?> to) {
			behaviors.add(to);
			return paths.add(new Path(from, to));
		}

		boolean traverse(Behavior<?> from, Behavior<?> to) {
			behaviors.add(to);
			return paths.add(new Path(from, to));
		}

		boolean traverse(Behavior<?> from, Event<?> to) {
			events.add(to);
			return paths.add(new Path(from, to));
		}

		TraverseLog move() {
			return this;
		}

		TraverseLog copy() {
			return new TraverseLog(new HashSet<>(paths), new HashSet<>(events), new HashSet<>(behaviors));
		}

		static final class Path {
			Object from;
			Object to;

			public Path(Object from, Object to) {
				this.from = from;
				this.to = to;
			}

			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + ((from == null) ? 0 : from.hashCode());
				result = prime * result + ((to == null) ? 0 : to.hashCode());
				return result;
			}

			@Override
			public boolean equals(Object obj) {
				if (this == obj) return true;
				if (obj == null) return false;
				if (getClass() != obj.getClass()) return false;
				Path other = (Path) obj;
				if (from == null) {
					if (other.from != null) return false;
				} else if (!from.equals(other.from)) return false;
				if (to == null) {
					if (other.to != null) return false;
				} else if (!to.equals(other.to)) return false;
				return true;
			}
		}
	}

	static final class RelationEntry {
		final List<Event<Runnable>> targetOutputEvents;
		final Set<Event<?>> targetEvents;
		final Set<Behavior.FlowStore<?>> targetStoreBehaviors;

		RelationEntry() {
			this.targetOutputEvents = new ArrayList<>();
			this.targetEvents = new HashSet<>();
			this.targetStoreBehaviors = new HashSet<>();
		}

		void merge(TraverseLog t) {
			targetEvents.addAll(t.events);
			for (Behavior<?> behavior : t.behaviors)
				if (behavior instanceof Behavior.FlowStore<?>)
					targetStoreBehaviors.add((Behavior.FlowStore<?>) behavior);
		}
	}

	static final class Port<A> {
		final Source<A> source;
		final Consumer<A> callback;
		boolean responsive;
		Source.Handler<A> handler;

		Port(Source<A> source, Function<Port<A>, Consumer<A>> callback) {
			this.source = source;
			this.callback = callback.apply(this);
			this.responsive = false;
			this.handler = null;
		}

		void attach() {
			if (responsive)
				throw new IllegalStateException();
			responsive = true;
			handler = source.attach(callback);
		}

		void detach() {
			if (!responsive)
				throw new IllegalStateException();
			responsive = false;
			handler.detach();
			handler = null;
		}
	}

	public enum State {
		/**
		 * Reactive is ready for processing external input
		 */
		RESUMED,
		/**
		 * Reactive is processing external input
		 */
		PROCESSING,
		/**
		 * Reactive is suspended so will not respond to any external input
		 */
		SUSPENDED
	}
}
