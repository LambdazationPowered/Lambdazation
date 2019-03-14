package org.lambdazation.common.utils;

import java.util.Optional;
import java.util.stream.Stream;

public interface EnumValue<E extends EnumValue<E>> {
	String name();

	int ordinal();

	interface EnumObject<E extends EnumValue<E>> {
		int size();

		Optional<E> valueAt(int ordinal);

		Stream<E> values();

		Optional<E> valueOf(String name);
	}
}
