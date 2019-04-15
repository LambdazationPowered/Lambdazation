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
		final Function<A, B> f;

		Fmap(Function<A, B> f) {
			this.f = f;
		}
	}

	static class Apply<A, B> extends Behavior<B> {
		final Behavior<Function<A, B>> behavior;

		Apply(Behavior<Function<A, B>> behavior) {
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
		return new Fmap<>(f);
	}

	public <B> Behavior<B> apply(Behavior<Function<A, B>> behavior) {
		return new Apply<>(behavior);
	}

	public static <A> Behavior<A> pure(A a) {
		return new Pure<>(a);
	}
}
