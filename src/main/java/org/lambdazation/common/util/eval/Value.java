package org.lambdazation.common.util.eval;

import java.util.function.Function;

public final class Value<A> implements Lazy<A> {
	private final A value;

	Value(A a) {
		this.value = a;
	}

	Value(Function<Lazy<A>, Lazy<A>> f) {
		throw new RuntimeException("Reference cycle detected");
	}

	Value(Lazy<A> lazy) {
		this.value = lazy.get();
	}

	@Override
	public A get() {
		return value;
	}

	@Override
	public Strategy strategy() {
		return Strategy.CALL_BY_VALUE;
	}

	@Override
	public Lazy<A> as(Strategy strategy) {
		switch (strategy) {
		case CALL_BY_NAME:
			return Lazy.pure(value);
		case CALL_BY_NEED:
			return Thunk.pure(value);
		case CALL_BY_VALUE:
			return this;
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public <B> Lazy<B> fmap(Function<A, B> f) {
		return new Value<>(f.apply(value));
	}

	@Override
	public <B> Lazy<B> apply(Lazy<Function<A, B>> lazy) {
		return new Value<>(lazy.get().apply(value));
	}

	@Override
	public <B> Lazy<B> compose(Function<A, Lazy<B>> f) {
		return new Value<>(f.apply(value).get());
	}

	public static <A> Lazy<A> pure(A a) {
		return new Value<>(a);
	}

	public static <A> Lazy<A> mfix(Function<Lazy<A>, Lazy<A>> f) {
		return new Value<>(f);
	}

	public static <A> Lazy<A> of(Lazy<A> lazy) {
		return new Value<>(lazy);
	}
}
