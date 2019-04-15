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
public final class Behavior<A> {
	public <B> Behavior<B> fmap(Function<A, B> f) {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public <B> Behavior<B> apply(Behavior<Function<A, B>> behavior) {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public static <A> Behavior<A> unit(A a) {
		// TODO NYI
		throw new AbstractMethodError();
	}
}
