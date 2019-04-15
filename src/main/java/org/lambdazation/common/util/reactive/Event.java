package org.lambdazation.common.util.reactive;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

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
public abstract class Event<A> {
	Event() {

	}

	static class Fmap<A, B> extends Event<B> {
		final Function<A, B> f;

		Fmap(Function<A, B> f) {
			this.f = f;
		}
	}

	static class Filter<A> extends Event<A> {
		final Predicate<A> p;

		Filter(Predicate<A> p) {
			this.p = p;
		}
	}

	static class Mempty<A> extends Event<A> {
		Mempty() {

		}
	}

	static class Mappend<A> extends Event<A> {
		final BiFunction<A, A, A> f;
		final Event<A> event1;
		final Event<A> event2;

		public Mappend(BiFunction<A, A, A> f, Event<A> event1, Event<A> event2) {
			this.f = f;
			this.event1 = event1;
			this.event2 = event2;
		}
	}

	public <B> Event<B> fmap(Function<A, B> f) {
		return new Fmap<>(f);
	}

	public Event<A> filter(Predicate<A> p) {
		return new Filter<>(p);
	}

	public static <A> Event<A> mempty() {
		return new Mempty<>();
	}

	public static <A> Event<A> mappend(BiFunction<A, A, A> f, Event<A> event1, Event<A> event2) {
		return new Mappend<>(f, event1, event2);
	}
}
