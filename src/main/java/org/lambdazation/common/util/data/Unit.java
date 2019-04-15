package org.lambdazation.common.util.data;

import java.util.function.Function;

import org.lambdazation.common.util.EnumMetadata;
import org.lambdazation.common.util.EnumValue;

public enum Unit implements EnumValue<Unit> {
	UNIT;

	public static final EnumMetadata<Unit> METADATA = new EnumMetadata<>(Unit.class);

	public <A> A match(A f) {
		return f;
	}

	public static Unit ofUnit() {
		return UNIT;
	}

	public static <A> Function<Unit, Function<A, A>> matchUnit() {
		return unit -> f -> unit.match(f);
	}

	public static <A> Function<A, A> matchUnit(Unit unit) {
		return f -> unit.match(f);
	}

	public static <A> A matchUnit(Unit unit, A f) {
		return unit.match(f);
	}

	public static <A> Function<Unit, A> unboxUnit(A f) {
		return unit -> unit.match(f);
	}
}
