package org.lambdazation.common.util.data;

import java.util.function.Function;

public final class Top {
	public final Object value;

	public Top(Object value) {
		this.value = value;
	}

	public Object value() {
		return value;
	}

	public <A> A match(Function<Object, A> f) {
		return f.apply(value);
	}

	public static <A> Function<A, Top> ofTop() {
		return value -> new Top(value);
	}

	public static <A> Top ofTop(A value) {
		return new Top(value);
	}

	public static Function<Top, Object> projectionTop() {
		return top -> top.value;
	}

	public static <A> Function<Top, Function<Function<Object, A>, A>> matchTop() {
		return top -> f -> top.match(f);
	}

	public static <A> Function<Function<Object, A>, A> matchTop(Top top) {
		return f -> top.match(f);
	}

	public static <A> A matchTop(Top top, Function<Object, A> f) {
		return top.match(f);
	}

	public static <A> Function<Top, A> unboxTop(Function<Object, A> f) {
		return top -> top.match(f);
	}
}
