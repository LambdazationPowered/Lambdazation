package org.lambdazation.common.util.data;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Sum<A, B> {
	Sum() {}

	public abstract Sum<B, Unit> leftShift();

	public abstract Sum<Unit, A> rightShift();

	public abstract Sum<B, A> commute();

	public abstract <C> C match(Function<A, C> f, Function<B, C> g);

	public abstract <C> Sum<C, B> mapLeft(Function<A, C> f);

	public abstract <C> Sum<A, C> mapRight(Function<B, C> f);

	public abstract <C> Sum<C, B> flatMapLeft(Function<A, Sum<C, B>> f);

	public abstract <C> Sum<A, C> flatMapRight(Function<B, Sum<A, C>> f);

	public abstract void ifLeft(Consumer<A> f);

	public abstract void ifRight(Consumer<B> f);

	public static final class Left<A, B> extends Sum<A, B> {
		public final A left;

		public Left(A left) {
			this.left = left;
		}

		public A left() {
			return left;
		}

		@Override
		public Sum<B, Unit> leftShift() {
			return new Right<>(Unit.UNIT);
		}

		@Override
		public Sum<Unit, A> rightShift() {
			return new Right<>(left);
		}

		@Override
		public Sum<B, A> commute() {
			return new Right<>(left);
		}

		@Override
		public <C> C match(Function<A, C> f, Function<B, C> g) {
			return f.apply(left);
		}

		@Override
		public <C> Sum<C, B> mapLeft(Function<A, C> f) {
			return new Left<>(f.apply(left));
		}

		@Override
		public <C> Sum<A, C> mapRight(Function<B, C> f) {
			return new Left<>(left);
		}

		@Override
		public <C> Sum<C, B> flatMapLeft(Function<A, Sum<C, B>> f) {
			return f.apply(left);
		}

		@Override
		public <C> Sum<A, C> flatMapRight(Function<B, Sum<A, C>> f) {
			return new Left<>(left);
		}

		@Override
		public void ifLeft(Consumer<A> f) {
			f.accept(left);
		}

		@Override
		public void ifRight(Consumer<B> f) {

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
		public Sum<B, Unit> leftShift() {
			return new Left<>(right);
		}

		@Override
		public Sum<Unit, A> rightShift() {
			return new Left<>(Unit.UNIT);
		}

		@Override
		public Sum<B, A> commute() {
			return new Left<>(right);
		}

		@Override
		public <C> C match(Function<A, C> f, Function<B, C> g) {
			return g.apply(right);
		}

		@Override
		public <C> Sum<C, B> mapLeft(Function<A, C> f) {
			return new Right<>(right);
		}

		@Override
		public <C> Sum<A, C> mapRight(Function<B, C> f) {
			return new Right<>(f.apply(right));
		}

		@Override
		public <C> Sum<C, B> flatMapLeft(Function<A, Sum<C, B>> f) {
			return new Right<>(right);
		}

		@Override
		public <C> Sum<A, C> flatMapRight(Function<B, Sum<A, C>> f) {
			return f.apply(right);
		}

		@Override
		public void ifLeft(Consumer<A> f) {

		}

		@Override
		public void ifRight(Consumer<B> f) {
			f.accept(right);
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

	public static <A, B, C> Function<Sum<A, B>, Function<Function<A, C>, Sum<C, B>>> mapLeft() {
		return sum -> f -> sum.mapLeft(f);
	}

	public static <A, B, C> Function<Function<A, C>, Sum<C, B>> mapLeft(Sum<A, B> sum) {
		return f -> sum.mapLeft(f);
	}

	public static <A, B, C> Sum<C, B> mapLeft(Sum<A, B> sum, Function<A, C> f) {
		return sum.mapLeft(f);
	}

	public static <A, B, C> Function<Sum<A, B>, Function<Function<B, C>, Sum<A, C>>> mapRight() {
		return sum -> f -> sum.mapRight(f);
	}

	public static <A, B, C> Function<Function<B, C>, Sum<A, C>> mapRight(Sum<A, B> sum) {
		return f -> sum.mapRight(f);
	}

	public static <A, B, C> Sum<A, C> mapRight(Sum<A, B> sum, Function<B, C> f) {
		return sum.mapRight(f);
	}

	public static <A, B, C> Function<Sum<A, B>, Function<Function<A, Sum<C, B>>, Sum<C, B>>> flatMapLeft() {
		return sum -> f -> sum.flatMapLeft(f);
	}

	public static <A, B, C> Function<Function<A, Sum<C, B>>, Sum<C, B>> flatMapLeft(Sum<A, B> sum) {
		return f -> sum.flatMapLeft(f);
	}

	public static <A, B, C> Sum<C, B> flatMapLeft(Sum<A, B> sum, Function<A, Sum<C, B>> f) {
		return sum.flatMapLeft(f);
	}

	public static <A, B, C> Function<Sum<A, B>, Function<Function<B, Sum<A, C>>, Sum<A, C>>> flatMapRight() {
		return sum -> f -> sum.flatMapRight(f);
	}

	public static <A, B, C> Function<Function<B, Sum<A, C>>, Sum<A, C>> flatMapRight(Sum<A, B> sum) {
		return f -> sum.flatMapRight(f);
	}

	public static <A, B, C> Sum<A, C> flatMapRight(Sum<A, B> sum, Function<B, Sum<A, C>> f) {
		return sum.flatMapRight(f);
	}

	public static <A> Sum<A, Unit> filterLeft(Sum<A, Unit> sum, Predicate<A> p) {
		return sum.flatMapLeft(left -> p.test(left) ? ofSumLeft(left) : ofSumRight(Unit.UNIT));
	}

	public static <B> Sum<Unit, B> filterRight(Sum<Unit, B> sum, Predicate<B> p) {
		return sum.flatMapRight(right -> p.test(right) ? ofSumRight(right) : ofSumLeft(Unit.UNIT));
	}

	public static <A> Sum<A, Unit> mappendLeft(BiFunction<A, A, A> f, Sum<A, Unit> sum1, Sum<A, Unit> sum2) {
		return sum1.match(left1 -> sum2.match(left2 -> ofSumLeft(f.apply(left1, left2)), unit -> sum1), unit -> sum2);
	}

	public static <B> Sum<Unit, B> mappendRight(BiFunction<B, B, B> f, Sum<Unit, B> sum1, Sum<Unit, B> sum2) {
		return sum1.match(unit -> sum2, right1 -> sum2.match(unit -> sum1, right2 -> ofSumRight(f.apply(right1, right2))));
	}

	public static <A, B, C> Sum<C, Unit> combineLeft(Function<A, Sum<C, Unit>> f, Function<B, Sum<C, Unit>> g, BiFunction<A, B, Sum<C, Unit>> h,
		Sum<A, Unit> sum1, Sum<B, Unit> sum2) {
		return sum1.match(a -> sum2.match(b -> h.apply(a, b), unit2 -> f.apply(a)), unit1 -> sum2.match(b -> g.apply(b), unit2 -> ofSumRight(Unit.UNIT)));
	}

	public static <A, B, C> Sum<Unit, C> combineRight(Function<A, Sum<Unit, C>> f, Function<B, Sum<Unit, C>> g, BiFunction<A, B, Sum<Unit, C>> h,
		Sum<Unit, A> sum1, Sum<Unit, B> sum2) {
		return sum1.match(unit1 -> sum2.match(unit2 -> ofSumLeft(Unit.UNIT), b -> g.apply(b)), a -> sum2.match(unit2 -> f.apply(a), b -> h.apply(a, b)));
	}

	public static <A, B, C> Function<Sum<A, B>, C> unboxSum(Function<A, C> f, Function<B, C> g) {
		return sum -> sum.match(f, g);
	}
}
