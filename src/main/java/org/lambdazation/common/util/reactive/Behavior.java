package org.lambdazation.common.util.reactive;

import java.util.function.Function;

import org.lambdazation.common.util.eval.Lazy;

/**
 * Stream of values that can vary over continuous time.
 * <p>
 * Typeclass:
 * <ul>
 * <li>Functor Behavior</li>
 * <li>Applicative Behavior</li>
 * </ul>
 * 
 * @param <A>
 *            Type of value in behavior
 */
public abstract class Behavior<A> {
	Behavior() {

	}

	static class Fmap<A, B> extends Behavior<B> {
		final Behavior<A> parent;
		final Function<A, B> f;

		Fmap(Behavior<A> parent, Function<A, B> f) {
			this.parent = parent;
			this.f = f;
		}

		@Override
		B accept(Vistor v) {
			return v.visit(this);
		}
	}

	static class Apply<A, B> extends Behavior<B> {
		final Behavior<A> parent;
		final Behavior<Function<A, B>> behavior;

		Apply(Behavior<A> parent, Behavior<Function<A, B>> behavior) {
			this.parent = parent;
			this.behavior = behavior;
		}

		@Override
		B accept(Vistor v) {
			return v.visit(this);
		}
	}

	static class Pure<A> extends Behavior<A> {
		final A a;

		Pure(A a) {
			this.a = a;
		}

		@Override
		A accept(Vistor v) {
			return v.visit(this);
		}
	}

	static class FlowBfix<A> extends Behavior<A> {
		final Lazy<Behavior<A>> lazy;

		public FlowBfix(Lazy<Behavior<A>> lazy) {
			this.lazy = lazy;
		}

		@Override
		A accept(Vistor v) {
			return v.visit(this);
		}
	}

	static class FlowStore<A> extends Behavior<A> {
		final A a;
		final Event<A> event;

		FlowStore(A a, Event<A> event) {
			this.a = a;
			this.event = event;
		}

		@Override
		A accept(Vistor v) {
			return v.visit(this);
		}
	}

	interface Vistor {
		<A, B> B visit(Fmap<A, B> behavior);

		<A, B> B visit(Apply<A, B> behavior);

		<A> A visit(Pure<A> behavior);

		<A> A visit(FlowBfix<A> behavior);

		<A> A visit(FlowStore<A> behavior);

		default <A> A accept(Behavior<A> behavior) {
			return behavior.accept(this);
		}
	}

	abstract A accept(Vistor v);

	public <B> Behavior<B> fmap(Function<A, B> f) {
		return new Fmap<>(this, f);
	}

	public <B> Behavior<B> apply(Behavior<Function<A, B>> behavior) {
		return new Apply<>(this, behavior);
	}

	public static <A> Behavior<A> pure(A a) {
		return new Pure<>(a);
	}
}
