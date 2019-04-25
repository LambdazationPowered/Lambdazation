package org.lambdazation.common.util.reactive;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.lambdazation.common.util.Functional;
import org.lambdazation.common.util.data.Maybe;
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
		Maybe<B> accept(EvalVistor v) {
			return v.visit(this);
		}

		@Override
		<T> void accept(TraverseVistor<T> v, T t) {
			v.visit(this, t);
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
		Maybe<A> accept(EvalVistor v) {
			return v.visit(this);
		}

		@Override
		<T> void accept(TraverseVistor<T> v, T t) {
			v.visit(this, t);
		}
	}

	static class Combine<A, B, C> extends Event<C> {
		final Function<A, Maybe<C>> f;
		final Function<B, Maybe<C>> g;
		final BiFunction<A, B, Maybe<C>> h;
		final Event<A> event1;
		final Event<B> event2;

		public Combine(Function<A, Maybe<C>> f, Function<B, Maybe<C>> g, BiFunction<A, B, Maybe<C>> h, Event<A> event1, Event<B> event2) {
			this.f = f;
			this.g = g;
			this.h = h;
			this.event1 = event1;
			this.event2 = event2;
		}

		@Override
		Maybe<C> accept(EvalVistor v) {
			return v.visit(this);
		}

		@Override
		<T> void accept(TraverseVistor<T> v, T t) {
			v.visit(this, t);
		}
	}

	static class Mempty<A> extends Event<A> {
		Mempty() {

		}

		@Override
		Maybe<A> accept(EvalVistor v) {
			return v.visit(this);
		}

		@Override
		<T> void accept(TraverseVistor<T> v, T t) {
			v.visit(this, t);
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
		Maybe<A> accept(EvalVistor v) {
			return v.visit(this);
		}

		@Override
		<T> void accept(TraverseVistor<T> v, T t) {
			v.visit(this, t);
		}
	}

	static class FlowEfix<A> extends Event<A> {
		final Lazy<Event<A>> lazy;

		public FlowEfix(Lazy<Event<A>> lazy) {
			this.lazy = lazy;
		}

		@Override
		Maybe<A> accept(EvalVistor v) {
			return v.visit(this);
		}

		@Override
		<T> void accept(TraverseVistor<T> v, T t) {
			v.visit(this, t);
		}

		@Override
		Event<A> get() {
			return lazy.get().get();
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
		Maybe<B> accept(EvalVistor v) {
			return v.visit(this);
		}

		@Override
		<T> void accept(TraverseVistor<T> v, T t) {
			v.visit(this, t);
		}
	}

	static class FlowInput<A> extends Event<A> {
		final Source<A> source;

		FlowInput(Source<A> source) {
			this.source = source;
		}

		@Override
		Maybe<A> accept(EvalVistor v) {
			return v.visit(this);
		}

		@Override
		<T> void accept(TraverseVistor<T> v, T t) {
			v.visit(this, t);
		}
	}

	interface EvalVistor {
		<A, B> Maybe<B> visit(Fmap<A, B> event);

		<A> Maybe<A> visit(Filter<A> event);

		<A, B, C> Maybe<C> visit(Combine<A, B, C> event);

		<A> Maybe<A> visit(Mempty<A> event);

		<A> Maybe<A> visit(Mappend<A> event);

		<A> Maybe<A> visit(FlowEfix<A> event);

		<A, B> Maybe<B> visit(FlowRetrieve<A, B> event);

		<A> Maybe<A> visit(FlowInput<A> event);

		default <A> Maybe<A> accept(Event<A> event) {
			return event.accept(this);
		}
	}

	interface TraverseVistor<T> {
		<A, B> void visit(Fmap<A, B> event, T t);

		<A> void visit(Filter<A> event, T t);

		<A, B, C> void visit(Combine<A, B, C> event, T t);

		<A> void visit(Mempty<A> event, T t);

		<A> void visit(Mappend<A> event, T t);

		<A> void visit(FlowEfix<A> event, T t);

		<A, B> void visit(FlowRetrieve<A, B> event, T t);

		<A> void visit(FlowInput<A> event, T t);

		default <A> void accept(Event<A> event, T t) {
			event.accept(this, t);
		}
	}

	abstract Maybe<A> accept(EvalVistor v);

	abstract <T> void accept(TraverseVistor<T> v, T t);

	Event<A> get() {
		return this;
	}

	public <B> Event<B> fmap(Function<A, B> f) {
		return new Fmap<>(this, f);
	}

	public <B> Event<B> replace(B b) {
		return fmap(Functional.constant(b));
	}

	public Event<A> filter(Predicate<A> p) {
		return new Filter<>(this, p);
	}

	public static <A, B, C> Event<C> combine(Function<A, Maybe<C>> f, Function<B, Maybe<C>> g, BiFunction<A, B, Maybe<C>> h,
		Event<A> event1, Event<B> event2) {
		return new Combine<>(f, g, h, event1, event2);
	}

	public static <A> Event<A> mempty() {
		return new Mempty<>();
	}

	public static <A> Event<A> mappend(BiFunction<A, A, A> f, Event<A> event1, Event<A> event2) {
		return new Mappend<>(f, event1, event2);
	}
}
