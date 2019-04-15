package org.lambdazation.common.util.reactive;

import java.util.function.Function;

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
	}

	static class Apply<A, B> extends Behavior<B> {
		final Behavior<A> parent;
		final Behavior<Function<A, B>> behavior;

		Apply(Behavior<A> parent, Behavior<Function<A, B>> behavior) {
			this.parent = parent;
			this.behavior = behavior;
		}
	}

	static class Pure<A> extends Behavior<A> {
		final A a;

		Pure(A a) {
			this.a = a;
		}
	}

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
