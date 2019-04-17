package org.lambdazation.common.util.eval;

import java.util.function.Function;

@FunctionalInterface
public interface Lazy<A> {
	A get();

	default Strategy strategy() {
		return Strategy.CALL_BY_NAME;
	}

	default Lazy<A> as(Strategy strategy) {
		switch (strategy) {
		case CALL_BY_NAME:
			return this;
		case CALL_BY_NEED:
			return Thunk.of(this);
		case CALL_BY_VALUE:
			return Value.of(this);
		default:
			throw new IllegalStateException();
		}
	}

	default <B> Lazy<B> fmap(Function<A, B> f) {
		return () -> f.apply(get());
	}

	default <B> Lazy<B> apply(Lazy<Function<A, B>> lazy) {
		return () -> lazy.get().apply(get());
	}

	default <B> Lazy<B> compose(Function<A, Lazy<B>> f) {
		return () -> f.apply(get()).get();
	}

	static <A> Lazy<A> pure(A a) {
		return () -> a;
	}

	static <A> Lazy<A> mfix(Function<Lazy<A>, Lazy<A>> f) {
		return () -> f.apply(mfix(f)).get();
	}

	static <A> Lazy<A> of(Lazy<A> lazy) {
		return () -> lazy.get();
	}
}
