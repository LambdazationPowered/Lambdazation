package org.lambdazation.common.utils;

import java.util.List;
import java.util.Optional;

public interface EnumValue<E extends EnumValue<E>> {
	String name();

	int ordinal();

	interface EnumObject<E extends EnumValue<E>> {
		List<E> values();

		Optional<E> valueOf(String name);
	}
}
