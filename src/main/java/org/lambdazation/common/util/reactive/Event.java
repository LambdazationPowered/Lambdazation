package org.lambdazation.common.util.reactive;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.lambdazation.common.util.eval.Lazy;

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
		final Event<A> parent;
		final Function<A, B> f;

		Fmap(Event<A> parent, Function<A, B> f) {
			this.parent = parent;
			this.f = f;
		}

		@Override
		B accept(Vistor v) {
			return v.visit(this);
		}
	}

	static class Filter<A> extends Event<A> {
		final Event<A> parent;
		final Predicate<A> p;

		Filter(Event<A> parent, Predicate<A> p) {
			this.parent = parent;
			this.p = p;
		}

		@Override
		A accept(Vistor v) {
			return v.visit(this);
		}
	}

	static class Mempty<A> extends Event<A> {
		Mempty() {

		}

		@Override
		A accept(Vistor v) {
			return v.visit(this);
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

		@Override
		A accept(Vistor v) {
			return v.visit(this);
		}
	}

	static class FlowEfix<A> extends Event<A> {
		final Lazy<Event<A>> lazy;

		public FlowEfix(Lazy<Event<A>> lazy) {
			this.lazy = lazy;
		}

		@Override
		A accept(Vistor v) {
			return v.visit(this);
		}
	}

	static class FlowRetrieve<A, B> extends Event<B> {
		final Behavior<Function<A, B>> behavior;
		final Event<A> event;

		FlowRetrieve(Behavior<Function<A, B>> behavior, Event<A> event) {
			this.behavior = behavior;
			this.event = event;
		}

		@Override
		B accept(Vistor v) {
			return v.visit(this);
		}
	}

	static class FlowInput<A> extends Event<A> {
		final Source<A> source;

		FlowInput(Source<A> source) {
			this.source = source;
		}

		@Override
		A accept(Vistor v) {
			return v.visit(this);
		}
	}

	interface Vistor {
		<A, B> B visit(Fmap<A, B> event);

		<A> A visit(Filter<A> event);

		<A> A visit(Mempty<A> event);

		<A> A visit(Mappend<A> event);

		<A> A visit(FlowEfix<A> event);

		<A, B> B visit(FlowRetrieve<A, B> event);

		<A> A visit(FlowInput<A> event);

		default <A> A accept(Event<A> event) {
			return event.accept(this);
		}
	}

	abstract A accept(Vistor v);

	public <B> Event<B> fmap(Function<A, B> f) {
		return new Fmap<>(this, f);
	}

	public Event<A> filter(Predicate<A> p) {
		return new Filter<>(this, p);
	}

	public static <A> Event<A> mempty() {
		return new Mempty<>();
	}

	public static <A> Event<A> mappend(BiFunction<A, A, A> f, Event<A> event1, Event<A> event2) {
		return new Mappend<>(f, event1, event2);
	}
}
