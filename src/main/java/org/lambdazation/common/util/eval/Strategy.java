package org.lambdazation.common.util.eval;

import org.lambdazation.common.util.EnumMetadata;
import org.lambdazation.common.util.EnumValue;

public enum Strategy implements EnumValue<Strategy> {
	CALL_BY_VALUE(true, true), CALL_BY_NEED(false, true), CALL_BY_NAME(false, false);

	public static final EnumMetadata<Strategy> METADATA = new EnumMetadata<>(Strategy.class);

	public final boolean strict;
	public final boolean memoized;

	Strategy(boolean strict, boolean memoized) {
		this.strict = strict;
		this.memoized = memoized;
	}
}
