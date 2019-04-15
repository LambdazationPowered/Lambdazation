package org.lambdazation.common.util.data;

import java.util.function.Function;

public abstract class Sum<A, B> {
	Sum() {}

	public abstract <C> C match(Function<A, C> f, Function<B, C> g);

	public static final class Left<A, B> extends Sum<A, B> {
		public final A left;

		public Left(A left) {
			this.left = left;
		}

		public A left() {
			return left;
		}

		@Override
		public <C> C match(Function<A, C> f, Function<B, C> g) {
			return f.apply(left);
		}
	}

	public static final class Right<A, B> extends Sum<A, B> {
		public final B right;

		public Right(B right) {
			this.right = right;
		}

		public B right() {
			return right;
		}

		@Override
		public <C> C match(Function<A, C> f, Function<B, C> g) {
			return g.apply(right);
		}
	}

	public static <A, B> Function<A, Sum<A, B>> ofLeft() {
		return left -> new Left<>(left);
	}

	public static <A, B> Sum<A, B> ofLeft(A left) {
		return new Left<>(left);
	}

	public static <A, B> Function<B, Sum<A, B>> ofRight() {
		return right -> new Right<>(right);
	}

	public static <A, B> Sum<A, B> ofRight(B right) {
		return new Right<>(right);
	}

	public static <A, B, C> Function<Sum<A, B>, Function<Function<A, C>, Function<Function<B, C>, C>>> matchSum() {
		return sum -> f -> g -> sum.match(f, g);
	}

	public static <A, B, C> Function<Function<A, C>, Function<Function<B, C>, C>> matchSum(Sum<A, B> sum) {
		return f -> g -> sum.match(f, g);
	}

	public static <A, B, C> Function<Function<B, C>, C> matchSum(Sum<A, B> sum, Function<A, C> f) {
		return g -> sum.match(f, g);
	}

	public static <A, B, C> C matchSum(Sum<A, B> sum, Function<A, C> f, Function<B, C> g) {
		return sum.match(f, g);
	}

	public static <A, B, C> Function<Sum<A, B>, C> unboxSum(Function<A, C> f, Function<B, C> g) {
		return sum -> sum.match(f, g);
	}
}
