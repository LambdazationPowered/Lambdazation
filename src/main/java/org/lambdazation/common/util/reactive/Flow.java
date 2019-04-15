package org.lambdazation.common.util.reactive;

import java.util.function.Function;

import org.lambdazation.common.util.data.Unit;

/**
 * Flow is intended for modeling reactive flow in monadic ways.
 * <p>
 * Typeclass:
 * <ul>
 * <li>Functor Flow</li>
 * <li>Applicative Flow</li>
 * <li>Monad Flow</li>
 * <li>MonadFix Flow</li>
 * </ul>
 *
 * @param <A>
 *            Type of monadic result
 */
public final class Flow<A> {
	public <B> Flow<B> fmap(Function<A, B> f) {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public <B> Flow<B> apply(Flow<Function<A, B>> flow) {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public <B> Flow<B> compose(Function<A, Flow<B>> f) {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public static <A> Flow<A> unit(A a) {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public static <A> Flow<A> mfix(Function<A, Flow<A>> f) {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public static <A> Flow<Behavior<A>> store(A a, Event<A> event) {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public static <A, B> Flow<Event<B>> retrieve(Behavior<Function<A, B>> behavior, Event<A> event) {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public static <A> Flow<Event<A>> input(Source<A> input) {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public static Flow<Unit> output(Event<Runnable> event) {
		// TODO NYI
		throw new AbstractMethodError();
	}
}
