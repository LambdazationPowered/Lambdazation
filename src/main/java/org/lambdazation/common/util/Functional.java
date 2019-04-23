package org.lambdazation.common.util;

import java.util.function.Function;

public interface Functional {
	static <A> Function<A, A> id() {
		return a -> a;
	}

	static <A> A id(A a) {
		return a;
	}

	static <A, B> Function<A, Function<B, A>> constant() {
		return a -> b -> a;
	}

	static <A, B> Function<B, A> constant(A a) {
		return b -> a;
	}

	static <A, B> A constant(A a, B b) {
		return a;
	}

	static <A, B, C> Function<Function<A, Function<B, C>>, Function<Function<A, B>, Function<A, C>>> subst() {
		return f -> g -> a -> f.apply(a).apply(g.apply(a));
	}

	static <A, B, C> Function<Function<A, B>, Function<A, C>> subst(Function<A, Function<B, C>> f) {
		return g -> a -> f.apply(a).apply(g.apply(a));
	}

	static <A, B, C> Function<A, C> subst(Function<A, Function<B, C>> f, Function<A, B> g) {
		return a -> f.apply(a).apply(g.apply(a));
	}

	static <A, B, C> C subst(Function<A, Function<B, C>> f, Function<A, B> g, A a) {
		return f.apply(a).apply(g.apply(a));
	}

	static <A, B, C> Function<Function<B, C>, Function<Function<A, B>, Function<A, C>>> compose() {
		return g -> f -> a -> g.apply(f.apply(a));
	}

	static <A, B, C> Function<Function<A, B>, Function<A, C>> compose(Function<B, C> g) {
		return f -> a -> g.apply(f.apply(a));
	}

	static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, B> f) {
		return a -> g.apply(f.apply(a));
	}

	static <A, B, C> C compose(Function<B, C> g, Function<A, B> f, A a) {
		return g.apply(f.apply(a));
	}

	static <A, B, C> Function<Function<A, Function<B, C>>, Function<B, Function<A, C>>> flip() {
		return f -> b -> a -> f.apply(a).apply(b);
	}

	static <A, B, C> Function<B, Function<A, C>> flip(Function<A, Function<B, C>> f) {
		return b -> a -> f.apply(a).apply(b);
	}

	static <A, B, C> Function<A, C> flip(Function<A, Function<B, C>> f, B b) {
		return a -> f.apply(a).apply(b);
	}

	static <A, B, C> C flip(Function<A, Function<B, C>> f, B b, A a) {
		return f.apply(a).apply(b);
	}

	static <A, B> Function<Function<A, B>, Function<A, B>> apply() {
		return f -> a -> f.apply(a);
	}

	static <A, B> Function<A, B> apply(Function<A, B> f) {
		return a -> f.apply(a);
	}

	static <A, B> B apply(Function<A, B> f, A a) {
		return f.apply(a);
	}

	static <A, B> Function<A, Function<Function<A, B>, B>> reverse() {
		return a -> f -> f.apply(a);
	}

	static <A, B> Function<Function<A, B>, B> reverse(A a) {
		return f -> f.apply(a);
	}

	static <A, B> B reverse(A a, Function<A, B> f) {
		return f.apply(a);
	}

	static <A, B> Function<Function<Function<A, B>, Function<A, B>>, Function<A, B>> fix() {
		return f -> a -> f.apply(Functional.<A, B> fix().apply(f)).apply(a);
	}

	static <A, B> Function<A, B> fix(Function<Function<A, B>, Function<A, B>> f) {
		return a -> f.apply(Functional.<A, B> fix().apply(f)).apply(a);
	}

	static <A, B> B fix(Function<Function<A, B>, Function<A, B>> f, A a) {
		return f.apply(Functional.<A, B> fix().apply(f)).apply(a);
	}
}
