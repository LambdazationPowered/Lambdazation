package org.lambdazation.common.util.reactive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

import org.lambdazation.common.util.data.Product;
import org.lambdazation.common.util.data.Sum;
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
		responsive = true;
		ports.forEach(Port::detach);
	}

	public State state() {
		return processing.getAsBoolean() ? State.PROCESSING : responsive ? State.RESUMED : State.SUSPENDED;
	}

	public static Reactive build(Flow<Unit> flow) {
		List<Event<Runnable>> outputEvents = new ArrayList<>();

		new Flow.EvalVistor() {
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
		}.accept(flow);

		Map<Event.FlowInput<?>, RelationEntry> inputEventRelationEntries = new HashMap<>();
		Map<Behavior.FlowStore<?>, ?> storeBehaviorValues = new HashMap<>();

		outputEvents.forEach(new Object() {
			final Event.TraverseVistor<TraverseLog> eventVistor = new Event.TraverseVistor<TraverseLog>() {
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
						behaviorVistor.accept(event.behavior, t.copy());
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
			};
			final Behavior.TraverseVistor<TraverseLog> behaviorVistor = new Behavior.TraverseVistor<TraverseLog>() {
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
					@SuppressWarnings("unchecked")
					Map<Behavior.FlowStore<A>, A> values = (Map<Behavior.FlowStore<A>, A>) (Map<?, ?>) storeBehaviorValues;
					values.put(behavior, behavior.a);
					if (t.traverse(behavior, behavior.event.get()))
						eventVistor.accept(behavior.event, t.move());
				}
			};
			final Set<Event.FlowInput<?>> inputEvents = new HashSet<>();

			void accept(Event<Runnable> outputEvent) {
				eventVistor.accept(outputEvent, new TraverseLog());
				inputEvents.forEach(inputEvent -> inputEventRelationEntries.get(inputEvent).targetOutputEvents.add(outputEvent));
			}
		}::accept);

		AtomicInteger activePort = new AtomicInteger(0);

		boolean responsive = false;
		BooleanSupplier processing = () -> activePort.get() > 0;
		List<Port<?>> ports = new ArrayList<>();

		inputEventRelationEntries.forEach((inputEvent, relationEntry) -> {
			Port<?> port = new Port<>(inputEvent.source, currentPort -> value -> {
				if (!currentPort.responsive)
					return;
				activePort.incrementAndGet();
				try {
					Evaluator evaluator = new Evaluator(inputEventRelationEntries, storeBehaviorValues, Collections.singletonMap(inputEvent, value));
					evaluator.eval();
				} finally {
					activePort.decrementAndGet();
				}
			});
			ports.add(port);
		});

		return new Reactive(responsive, processing, ports);
	}

	static final class Evaluator {
		final Map<Event.FlowInput<?>, RelationEntry> inputEventRelationEntries;
		final Map<Behavior.FlowStore<?>, ?> storeBehaviorValues;
		final Map<Event.FlowInput<?>, ?> firedInputEventValues;
		final Map<Event<?>, Sum<Unit, ?>> eventValues;
		final Map<Behavior<?>, ?> behaviorValues;
		final Event.EvalVistor eventVistor;
		final Behavior.EvalVistor behaviorVistor;

		Evaluator(Map<Event.FlowInput<?>, RelationEntry> inputEventRelationEntries, Map<Behavior.FlowStore<?>, ?> storeBehaviorValues,
			Map<Event.FlowInput<?>, ?> firedInputEventValues) {
			this.inputEventRelationEntries = inputEventRelationEntries;
			this.storeBehaviorValues = storeBehaviorValues;
			this.firedInputEventValues = firedInputEventValues;
			this.eventValues = new HashMap<>();
			this.behaviorValues = new HashMap<>();
			this.eventVistor = new Event.EvalVistor() {
				@Override
				public <A, B> Sum<Unit, B> visit(Event.Fmap<A, B> event) {
					Sum<Unit, A> eventParentValue = eval(event.parent);

					Sum<Unit, B> value = eventParentValue.mapRight(event.f);
					eventValues.put(event, value);
					return value;
				}

				@Override
				public <A> Sum<Unit, A> visit(Event.Filter<A> event) {
					Sum<Unit, A> eventParentValue = eval(event.parent);

					Sum<Unit, A> value = Sum.filterRight(eventParentValue, event.p);
					eventValues.put(event, value);
					return value;
				}

				@Override
				public <A> Sum<Unit, A> visit(Event.Mempty<A> event) {
					Sum<Unit, A> value = Sum.ofSumLeft(Unit.UNIT);
					eventValues.put(event, value);
					return value;
				}

				@Override
				public <A> Sum<Unit, A> visit(Event.Mappend<A> event) {
					Sum<Unit, A> eventEvent1Value = eval(event.event1);
					Sum<Unit, A> eventEvent2Value = eval(event.event2);

					Sum<Unit, A> value = Sum.mappendRight(event.f, eventEvent1Value, eventEvent2Value);
					eventValues.put(event, value);
					return value;
				}

				@Override
				public <A> Sum<Unit, A> visit(Event.FlowEfix<A> event) {
					return accept(event.get());
				}

				@Override
				public <A, B> Sum<Unit, B> visit(Event.FlowRetrieve<A, B> event) {
					Function<A, B> eventBehaviorValue = eval(event.behavior);
					Sum<Unit, A> eventEventValue = eval(event.event);

					Sum<Unit, B> value = eventEventValue.mapRight(eventBehaviorValue);
					eventValues.put(event, value);
					return value;
				}

				@Override
				public <A> Sum<Unit, A> visit(Event.FlowInput<A> event) {
					Sum<Unit, A> value = Sum.ofSumRight(Evaluator.this.<A> firedInputEventValues().get(event));
					eventValues.put(event, value);
					return value;
				}
			};
			behaviorVistor = new Behavior.EvalVistor() {
				@Override
				public <A, B> B visit(Behavior.Fmap<A, B> behavior) {
					A behaviorParentValue = eval(behavior.parent);

					B value = behavior.f.apply(behaviorParentValue);
					Evaluator.this.<B> behaviorValues().put(behavior, value);
					return value;
				}

				@Override
				public <A, B> B visit(Behavior.Apply<A, B> behavior) {
					A behaviorParentValue = eval(behavior.parent);
					Function<A, B> behavioBehaviorValue = eval(behavior.behavior);

					B value = behavioBehaviorValue.apply(behaviorParentValue);
					Evaluator.this.<B> behaviorValues().put(behavior, value);
					return value;
				}

				@Override
				public <A> A visit(Behavior.Pure<A> behavior) {
					A value = behavior.a;
					Evaluator.this.<A> behaviorValues().put(behavior, value);
					return value;
				}

				@Override
				public <A> A visit(Behavior.FlowBfix<A> behavior) {
					return accept(behavior.get());
				}

				@Override
				public <A> A visit(Behavior.FlowStore<A> behavior) {
					A value = Evaluator.this.<A> storeBehaviorValues().get(behavior);
					Evaluator.this.<A> behaviorValues().put(behavior, value);
					return value;
				}
			};
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
		<A> Map<Event<A>, Sum<Unit, A>> eventValues() {
			return (Map<Event<A>, Sum<Unit, A>>) (Map<?, ?>) eventValues;
		}

		@SuppressWarnings("unchecked")
		<A> Map<Behavior<A>, A> behaviorValues() {
			return (Map<Behavior<A>, A>) (Map<?, ?>) behaviorValues;
		}

		void eval() {
			List<Runnable> actions = new ArrayList<>();
			for (Event.FlowInput<?> inputEvent : firedInputEventValues.keySet()) {
				RelationEntry relationEntry = inputEventRelationEntries.get(inputEvent);
				for (Event<Runnable> outputEvent : relationEntry.targetOutputEvents) {
					Sum<Unit, Runnable> eventValue = eval(outputEvent);
					eventValue.ifRight(value -> actions.add(value));
				}
				for (Behavior.FlowStore<?> storeBehavior : relationEntry.targetStoreBehaviors) {
					eval(storeBehavior);
					Sum<Unit, ?> eventValue = eval(storeBehavior.event);
					eventValue.ifRight(value -> storeBehaviorValues().put(storeBehavior, value));
				}
			}
			for (Runnable action : actions)
				action.run();
		}

		<A> Sum<Unit, A> eval(Event<A> event) {
			if (eventValues.containsKey(event))
				return this.<A> eventValues().get(event);
			Sum<Unit, A> value = eventVistor.accept(event);
			return value;
		}

		<A> A eval(Behavior<A> behavior) {
			if (behaviorValues.containsKey(behavior))
				return this.<A> behaviorValues().get(behavior);
			A value = behaviorVistor.accept(behavior);
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
