package org.lambdazation.common.utils;

public final class EnumBoolean extends GeneralizedEnum<EnumBoolean> {
	public static final EnumBoolean FALSE;
	public static final EnumBoolean TRUE;

	public static final GeneralizedEnum.Metadata<EnumBoolean> METADATA;

	static {
		GeneralizedEnum.Metadata.Builder<EnumBoolean> builder = GeneralizedEnum.Metadata.builder();

		FALSE = builder.withValue("FALSE", (name, ordinal) -> new EnumBoolean(name, ordinal, false));
		TRUE = builder.withValue("TRUE", (name, ordinal) -> new EnumBoolean(name, ordinal, true));

		METADATA = builder.build();
	}

	private final boolean value;

	EnumBoolean(String name, int ordinal, boolean value) {
		super(name, ordinal);
		this.value = value;
	}

	public boolean value() {
		return value;
	}
}
