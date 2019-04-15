package org.lambdazation.common.util;

import java.util.function.Function;

public interface Functional {
	static <A> Function<A, A> id() {
		return a -> a;
	}

	static <A, B> Function<A, Function<B, A>> constant() {
		return a -> b -> a;
	}

	static <A, B, C> Function<Function<A, Function<B, C>>, Function<Function<A, B>, Function<A, C>>> subst() {
		return f -> g -> a -> f.apply(a).apply(g.apply(a));
	}

	static <A, B, C> Function<Function<B, C>, Function<Function<A, B>, Function<A, C>>> compose() {
		return g -> f -> a -> g.apply(f.apply(a));
	}

	static <A, B, C> Function<Function<A, Function<B, C>>, Function<B, Function<A, C>>> flip() {
		return f -> b -> a -> f.apply(a).apply(b);
	}

	static <A, B> Function<Function<A, B>, Function<A, B>> apply() {
		return f -> a -> f.apply(a);
	}

	static <A, B> Function<A, Function<Function<A, B>, B>> reverse() {
		return a -> f -> f.apply(a);
	}

	static <A, B> Function<Function<Function<A, B>, Function<A, B>>, Function<A, B>> fix() {
		return f -> a -> f.apply(Functional.<A, B> fix().apply(f)).apply(a);
	}
}
