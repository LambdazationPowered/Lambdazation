package org.lambdazation.common.util.eval;

import java.util.function.Function;

public final class Thunk<A> implements Lazy<A> {
	private Lazy<A> lazy;
	private A value;
	private State state;

	Thunk(A a) {
		this.lazy = null;
		this.value = a;
		this.state = State.EVALUATED;
	}

	Thunk(Function<Lazy<A>, Lazy<A>> f) {
		this.lazy = () -> f.apply(this).get();
		this.value = null;
		this.state = State.UNEVALUATED;
	}

	Thunk(Lazy<A> lazy) {
		this.lazy = lazy;
		this.value = null;
		this.state = State.UNEVALUATED;
	}

	@Override
	public A get() {
		switch (state) {
		case UNEVALUATED:
			state = State.EVALUATING;

			A result;
			try {
				result = lazy.get();
			} catch (Throwable e) {
				state = State.UNEVALUATED;
				throw e;
			}

			if (state.equals(State.EVALUATING)) {
				lazy = null;
				value = result;
				state = State.EVALUATED;
			}

			return value;
		case EVALUATING:
			throw new RuntimeException("Reference cycle detected");
		case EVALUATED:
			return value;
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public Strategy strategy() {
		return Strategy.CALL_BY_NEED;
	}

	@Override
	public Lazy<A> as(Strategy strategy) {
		switch (strategy) {
		case CALL_BY_NAME:
			switch (state) {
			case UNEVALUATED:
			case EVALUATING:
				return lazy;
			case EVALUATED:
				return Lazy.pure(value);
			default:
				throw new IllegalStateException();
			}
		case CALL_BY_NEED:
			return this;
		case CALL_BY_VALUE:
			return Value.pure(get());
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public <B> Lazy<B> fmap(Function<A, B> f) {
		switch (state) {
		case UNEVALUATED:
		case EVALUATING:
			return new Thunk<>(() -> f.apply(get()));
		case EVALUATED:
			return new Thunk<>(() -> f.apply(value));
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public <B> Lazy<B> apply(Lazy<Function<A, B>> lazy) {
		switch (state) {
		case UNEVALUATED:
		case EVALUATING:
			return new Thunk<>(() -> lazy.get().apply(get()));
		case EVALUATED:
			return new Thunk<>(() -> lazy.get().apply(value));
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public <B> Lazy<B> compose(Function<A, Lazy<B>> f) {
		switch (state) {
		case UNEVALUATED:
		case EVALUATING:
			return new Thunk<>(() -> f.apply(get()).get());
		case EVALUATED:
			return new Thunk<>(() -> f.apply(value).get());
		default:
			throw new IllegalStateException();
		}
	}

	public static <A> Lazy<A> pure(A a) {
		return new Thunk<>(a);
	}

	public static <A> Lazy<A> mfix(Function<Lazy<A>, Lazy<A>> f) {
		return new Thunk<>(f);
	}

	public static <A> Lazy<A> of(Lazy<A> lazy) {
		return new Thunk<>(lazy);
	}

	enum State {
		UNEVALUATED, EVALUATING, EVALUATED
	}
}
