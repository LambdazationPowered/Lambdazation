package org.lambdazation.common.util.reactive;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

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
		responsive = true;
		ports.forEach(Port::detach);
	}

	public State state() {
		return processing.getAsBoolean() ? State.PROCESSING : responsive ? State.RESUMED : State.SUSPENDED;
	}

	public static Reactive build(Flow<Unit> flow) {
		List<Event<Runnable>> outputs = new ArrayList<>();

		new Flow.Vistor() {
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
				outputs.add(flow.event);

				Unit unit = Unit.UNIT;
				return unit;
			}

			@Override
			public <A> Event<A> visit(Flow.Input<A> flow) {
				Event<A> event = new Event.FlowInput<>(flow.source);
				return event;
			}
		}.accept(flow);

		boolean responsive = false;
		BooleanSupplier processing = () -> false;
		List<Port<?>> ports = new ArrayList<>();

		// TODO Find all port and mutable state need in this reactive flow
		
		return new Reactive(responsive, processing, ports);
	}

	static final class Mutable<A> {
		A value;

		Mutable(A value) {
			this.value = value;
		}

		A get() {
			return value;
		}

		void set(A value) {
			this.value = value;
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
