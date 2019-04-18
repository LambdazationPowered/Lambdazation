package org.lambdazation.common.util.data;

import java.util.function.Function;

public abstract class Sum<A, B> {
	Sum() {}

	public abstract <C> C match(Function<A, C> f, Function<B, C> g);

	public abstract Sum<B, A> commute();

	public abstract Sum<B, Unit> leftShift();

	public abstract Sum<Unit, A> rightShift();

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

		@Override
		public Sum<B, A> commute() {
			return new Right<>(left);
		}

		@Override
		public Sum<B, Unit> leftShift() {
			return new Right<>(Unit.UNIT);
		}

		@Override
		public Sum<Unit, A> rightShift() {
			return new Right<>(left);
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

		@Override
		public Sum<B, A> commute() {
			return new Left<>(right);
		}

		@Override
		public Sum<B, Unit> leftShift() {
			return new Left<>(right);
		}

		@Override
		public Sum<Unit, A> rightShift() {
			return new Left<>(Unit.UNIT);
		}
	}

	public static <A, B> Function<A, Sum<A, B>> ofSumLeft() {
		return left -> new Left<>(left);
	}

	public static <A, B> Sum<A, B> ofSumLeft(A left) {
		return new Left<>(left);
	}

	public static <A, B> Function<B, Sum<A, B>> ofSumRight() {
		return right -> new Right<>(right);
	}

	public static <A, B> Sum<A, B> ofSumRight(B right) {
		return new Right<>(right);
	}

	public static <A, B> Function<Left<A, B>, A> projectionLeft() {
		return sum -> sum.left;
	}

	public static <A, B> Function<Right<A, B>, B> projectionRight() {
		return sum -> sum.right;
	}

	public static <A, B> Function<Sum<A, B>, Sum<B, Unit>> leftShiftSum() {
		return sum -> sum.leftShift();
	}

	public static <A, B> Sum<B, Unit> leftShiftSum(Sum<A, B> sum) {
		return sum.leftShift();
	}

	public static <A, B> Function<Sum<A, B>, Sum<Unit, A>> rightShiftSum() {
		return sum -> sum.rightShift();
	}

	public static <A, B> Sum<Unit, A> rightShiftSum(Sum<A, B> sum) {
		return sum.rightShift();
	}

	public static <A, B, C> Function<Sum<A, Sum<B, C>>, Sum<Sum<A, B>, C>> assocLeftSum() {
		return sum -> sum.match(a -> new Left<>(new Left<>(a)), bc -> bc.match(b -> new Left<>(new Right<>(b)), c -> new Right<>(c)));
	}

	public static <A, B, C> Sum<Sum<A, B>, C> assocLeftSum(Sum<A, Sum<B, C>> sum) {
		return sum.match(a -> new Left<>(new Left<>(a)), bc -> bc.match(b -> new Left<>(new Right<>(b)), c -> new Right<>(c)));
	}

	public static <A, B, C> Function<Sum<Sum<A, B>, C>, Sum<A, Sum<B, C>>> assocRightSum() {
		return sum -> sum.match(ab -> ab.match(a -> new Left<>(a), b -> new Right<>(new Left<>(b))), c -> new Right<>(new Right<>(c)));
	}

	public static <A, B, C> Sum<A, Sum<B, C>> assocRightSum(Sum<Sum<A, B>, C> sum) {
		return sum.match(ab -> ab.match(a -> new Left<>(a), b -> new Right<>(new Left<>(b))), c -> new Right<>(new Right<>(c)));
	}

	public static <A, B> Function<Sum<A, B>, Sum<B, A>> commuteSum() {
		return sum -> sum.commute();
	}

	public static <A, B> Sum<B, A> commuteSum(Sum<A, B> sum) {
		return sum.commute();
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
