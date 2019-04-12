package org.lambdazation.common.util;

public enum EnumBoolean implements EnumValue<EnumBoolean> {
	FALSE, TRUE;

	public static final EnumMetadata<EnumBoolean> METADATA = new EnumMetadata<>(EnumBoolean.class);
}
