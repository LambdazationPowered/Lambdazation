package org.lambdazation.common.util.reactive;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Stream of events that can occurs in discrete point in time.
 * <p>
 * Typeclass:
 * <ul>
 * <li>Functor Event</li>
 * <li>Semigroup a => Monoid (Event a)</li>
 * </ul>
 * 
 * @param <A>
 *            Type of value associated with individual event
 */
public final class Event<A> {
	public <B> Event<B> fmap(Function<A, B> f) {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public static <A> Event<A> mempty() {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public static <A> Event<A> mappend(BiFunction<A, A, A> f, Event<A> event1, Event<A> event2) {
		// TODO NYI
		throw new AbstractMethodError();
	}

	public static <A> Event<A> filter(Event<Optional<A>> event) {
		// TODO NYI
		throw new AbstractMethodError();
	}
}
