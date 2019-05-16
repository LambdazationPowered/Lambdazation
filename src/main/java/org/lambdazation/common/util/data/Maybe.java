package org.lambdazation.common.util.data;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class Maybe<A> {
	Maybe() {}

	public abstract <B> B match(Supplier<B> f, Function<A, B> g);

	public abstract <B> Maybe<B> map(Function<A, B> f);

	public abstract <B> Maybe<B> flatMap(Function<A, Maybe<B>> f);

	public abstract Maybe<A> filter(Predicate<A> p);

	public abstract void ifNothing(Runnable f);

	public abstract void ifJust(Consumer<A> f);

	public abstract boolean isNothing();

	public abstract boolean isJust();

	public abstract Nothing<A> asNothing() throws ClassCastException;

	public abstract Just<A> asJust() throws ClassCastException;

	public static final class Nothing<A> extends Maybe<A> {
		@Override
		public <B> B match(Supplier<B> f, Function<A, B> g) {
			return f.get();
		}

		@Override
		public <B> Maybe<B> map(Function<A, B> f) {
			return new Nothing<>();
		}

		@Override
		public <B> Maybe<B> flatMap(Function<A, Maybe<B>> f) {
			return new Nothing<>();
		}

		@Override
		public Maybe<A> filter(Predicate<A> p) {
			return new Nothing<>();
		}

		@Override
		public void ifNothing(Runnable f) {
			f.run();
		}

		@Override
		public void ifJust(Consumer<A> f) {

		}

		@Override
		public boolean isNothing() {
			return true;
		}

		@Override
		public boolean isJust() {
			return false;
		}

		@Override
		public Nothing<A> asNothing() throws ClassCastException {
			return this;
		}

		@Override
		public Just<A> asJust() throws ClassCastException {
			throw new ClassCastException();
		}
	}

	public static final class Just<A> extends Maybe<A> {
		public final A value;

		public Just(A value) {
			this.value = value;
		}

		public A value() {
			return value;
		}

		@Override
		public <B> B match(Supplier<B> f, Function<A, B> g) {
			return g.apply(value);
		}

		@Override
		public <B> Maybe<B> map(Function<A, B> f) {
			return new Just<>(f.apply(value));
		}

		@Override
		public <B> Maybe<B> flatMap(Function<A, Maybe<B>> f) {
			return f.apply(value);
		}

		@Override
		public Maybe<A> filter(Predicate<A> p) {
			return p.test(value) ? this : new Nothing<>();
		}

		@Override
		public void ifNothing(Runnable f) {

		}

		@Override
		public void ifJust(Consumer<A> f) {
			f.accept(value);
		}

		@Override
		public boolean isNothing() {
			return false;
		}

		@Override
		public boolean isJust() {
			return true;
		}

		@Override
		public Nothing<A> asNothing() throws ClassCastException {
			throw new ClassCastException();
		}

		@Override
		public Just<A> asJust() throws ClassCastException {
			return this;
		}
	}

	public static <A> Maybe<A> ofNothing() {
		return new Nothing<>();
	}

	public static <A> Function<A, Maybe<A>> ofJust() {
		return value -> new Just<>(value);
	}

	public static <A> Maybe<A> ofJust(A value) {
		return new Just<>(value);
	}

	public static <A> Function<Just<A>, A> projectionJust() {
		return sum -> sum.value;
	}

	public static <A, B> Function<Maybe<A>, Function<Supplier<B>, Function<Function<A, B>, B>>> matchMaybe() {
		return maybe -> f -> g -> maybe.match(f, g);
	}

	public static <A, B> Function<Supplier<B>, Function<Function<A, B>, B>> matchMaybe(Maybe<A> maybe) {
		return f -> g -> maybe.match(f, g);
	}

	public static <A, B> Function<Function<A, B>, B> matchMaybe(Maybe<A> maybe, Supplier<B> f) {
		return g -> maybe.match(f, g);
	}

	public static <A, B> B matchMaybe(Maybe<A> maybe, Supplier<B> f, Function<A, B> g) {
		return maybe.match(f, g);
	}

	public static <A, B> Function<Maybe<A>, Function<Function<A, B>, Maybe<B>>> map() {
		return maybe -> f -> maybe.map(f);
	}

	public static <A, B> Function<Function<A, B>, Maybe<B>> map(Maybe<A> maybe) {
		return f -> maybe.map(f);
	}

	public static <A, B> Maybe<B> map(Maybe<A> maybe, Function<A, B> f) {
		return maybe.map(f);
	}

	public static <A, B> Function<Maybe<A>, Function<Function<A, Maybe<B>>, Maybe<B>>> flatMap() {
		return maybe -> f -> maybe.flatMap(f);
	}

	public static <A, B> Function<Function<A, Maybe<B>>, Maybe<B>> flatMap(Maybe<A> maybe) {
		return f -> maybe.flatMap(f);
	}

	public static <A, B> Maybe<B> flatMap(Maybe<A> maybe, Function<A, Maybe<B>> f) {
		return maybe.flatMap(f);
	}

	public static <A> Function<Maybe<A>, Function<Predicate<A>, Maybe<A>>> filter() {
		return maybe -> p -> maybe.filter(p);
	}

	public static <A> Function<Predicate<A>, Maybe<A>> filter(Maybe<A> maybe) {
		return p -> maybe.filter(p);
	}

	public static <A> Maybe<A> filter(Maybe<A> maybe, Predicate<A> p) {
		return maybe.filter(p);
	}

	public static <A> Function<BiFunction<A, A, A>, Function<Maybe<A>, Function<Maybe<A>, Maybe<A>>>> mappend() {
		return f -> maybe1 -> maybe2 -> maybe1.match(() -> maybe2, right1 -> maybe2.match(() -> maybe1, right2 -> ofJust(f.apply(right1, right2))));
	}

	public static <A> Function<Maybe<A>, Function<Maybe<A>, Maybe<A>>> mappend(BiFunction<A, A, A> f) {
		return maybe1 -> maybe2 -> maybe1.match(() -> maybe2, right1 -> maybe2.match(() -> maybe1, right2 -> ofJust(f.apply(right1, right2))));
	}

	public static <A> Function<Maybe<A>, Maybe<A>> mappend(BiFunction<A, A, A> f, Maybe<A> maybe1) {
		return maybe2 -> maybe1.match(() -> maybe2, right1 -> maybe2.match(() -> maybe1, right2 -> ofJust(f.apply(right1, right2))));
	}

	public static <A> Maybe<A> mappend(BiFunction<A, A, A> f, Maybe<A> maybe1, Maybe<A> maybe2) {
		return maybe1.match(() -> maybe2, right1 -> maybe2.match(() -> maybe1, right2 -> ofJust(f.apply(right1, right2))));
	}

	public static <A, B, C> Function<Function<A, Maybe<C>>, Function<Function<B, Maybe<C>>, Function<BiFunction<A, B, Maybe<C>>, Function<Maybe<A>, Function<Maybe<B>, Maybe<C>>>>>> combine() {
		return f -> g -> h -> maybe1 -> maybe2 -> maybe1.match(() -> maybe2.match(() -> ofNothing(), b -> g.apply(b)), a -> maybe2.match(() -> f.apply(a), b -> h.apply(a, b)));
	}

	public static <A, B, C> Function<Function<B, Maybe<C>>, Function<BiFunction<A, B, Maybe<C>>, Function<Maybe<A>, Function<Maybe<B>, Maybe<C>>>>> combine(Function<A, Maybe<C>> f) {
		return g -> h -> maybe1 -> maybe2 -> maybe1.match(() -> maybe2.match(() -> ofNothing(), b -> g.apply(b)), a -> maybe2.match(() -> f.apply(a), b -> h.apply(a, b)));
	}

	public static <A, B, C> Function<BiFunction<A, B, Maybe<C>>, Function<Maybe<A>, Function<Maybe<B>, Maybe<C>>>> combine(Function<A, Maybe<C>> f, Function<B, Maybe<C>> g) {
		return h -> maybe1 -> maybe2 -> maybe1.match(() -> maybe2.match(() -> ofNothing(), b -> g.apply(b)), a -> maybe2.match(() -> f.apply(a), b -> h.apply(a, b)));
	}

	public static <A, B, C> Function<Maybe<A>, Function<Maybe<B>, Maybe<C>>> combine(Function<A, Maybe<C>> f, Function<B, Maybe<C>> g, BiFunction<A, B, Maybe<C>> h) {
		return maybe1 -> maybe2 -> maybe1.match(() -> maybe2.match(() -> ofNothing(), b -> g.apply(b)), a -> maybe2.match(() -> f.apply(a), b -> h.apply(a, b)));
	}

	public static <A, B, C> Function<Maybe<B>, Maybe<C>> combine(Function<A, Maybe<C>> f, Function<B, Maybe<C>> g, BiFunction<A, B, Maybe<C>> h,
		Maybe<A> maybe1) {
		return maybe2 -> maybe1.match(() -> maybe2.match(() -> ofNothing(), b -> g.apply(b)), a -> maybe2.match(() -> f.apply(a), b -> h.apply(a, b)));
	}

	public static <A, B, C> Maybe<C> combine(Function<A, Maybe<C>> f, Function<B, Maybe<C>> g, BiFunction<A, B, Maybe<C>> h,
		Maybe<A> maybe1, Maybe<B> maybe2) {
		return maybe1.match(() -> maybe2.match(() -> ofNothing(), b -> g.apply(b)), a -> maybe2.match(() -> f.apply(a), b -> h.apply(a, b)));
	}

	public static <A, B> Function<Maybe<A>, B> unboxMaybe(Supplier<B> f, Function<A, B> g) {
		return maybe -> maybe.match(f, g);
	}

}
