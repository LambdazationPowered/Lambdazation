package org.lambdazation.common.util.data;

import java.util.function.Function;

import org.lambdazation.common.util.EnumMetadata;
import org.lambdazation.common.util.EnumValue;

public enum Bottom implements EnumValue<Bottom> {
	;

	public static final EnumMetadata<Bottom> METADATA = new EnumMetadata<>(Bottom.class);

	public <A> A match() {
		throw new AbstractMethodError();
	}

	public static <A> Function<Bottom, A> matchBottom() {
		return bottom -> bottom.match();
	}

	public static <A> A matchBottom(Bottom bottom) {
		return bottom.match();
	}

	public static <A> Function<Bottom, A> unboxBottom() {
		return bottom -> bottom.match();
	}
}
