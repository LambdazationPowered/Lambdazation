package org.lambdazation.common.util.reactive;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.lambdazation.common.util.data.Sum;
import org.lambdazation.common.util.data.Unit;
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
		Sum<Unit, B> accept(EvalVistor v) {
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
		Sum<Unit, A> accept(EvalVistor v) {
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
		Sum<Unit, A> accept(EvalVistor v) {
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
		Sum<Unit, A> accept(EvalVistor v) {
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
		Sum<Unit, A> accept(EvalVistor v) {
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
		Sum<Unit, B> accept(EvalVistor v) {
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
		Sum<Unit, A> accept(EvalVistor v) {
			return v.visit(this);
		}

		@Override
		<T> void accept(TraverseVistor<T> v, T t) {
			v.visit(this, t);
		}
	}

	interface EvalVistor {
		<A, B> Sum<Unit, B> visit(Fmap<A, B> event);

		<A> Sum<Unit, A> visit(Filter<A> event);

		<A> Sum<Unit, A> visit(Mempty<A> event);

		<A> Sum<Unit, A> visit(Mappend<A> event);

		<A> Sum<Unit, A> visit(FlowEfix<A> event);

		<A, B> Sum<Unit, B> visit(FlowRetrieve<A, B> event);

		<A> Sum<Unit, A> visit(FlowInput<A> event);

		default <A> Sum<Unit, A> accept(Event<A> event) {
			return event.accept(this);
		}
	}

	interface TraverseVistor<T> {
		<A, B> void visit(Fmap<A, B> event, T t);

		<A> void visit(Filter<A> event, T t);

		<A> void visit(Mempty<A> event, T t);

		<A> void visit(Mappend<A> event, T t);

		<A> void visit(FlowEfix<A> event, T t);

		<A, B> void visit(FlowRetrieve<A, B> event, T t);

		<A> void visit(FlowInput<A> event, T t);

		default <A> void accept(Event<A> event, T t) {
			event.accept(this, t);
		}
	}

	abstract Sum<Unit, A> accept(EvalVistor v);

	abstract <T> void accept(TraverseVistor<T> v, T t);

	Event<A> get() {
		return this;
	}

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
