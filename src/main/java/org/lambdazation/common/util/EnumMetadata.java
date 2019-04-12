package org.lambdazation.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.lambdazation.common.util.EnumValue.EnumObject;

public final class EnumMetadata<E extends Enum<E> & EnumValue<E>> implements EnumObject<E> {
	private final List<E> values;
	private final Map<String, E> mapping;

	public EnumMetadata(Class<E> enumClass) {
		values = new ArrayList<>();
		mapping = new HashMap<>();

		Arrays.stream(enumClass.getEnumConstants()).forEach(enumValue -> {
			values.add(enumValue);
			mapping.put(enumValue.name(), enumValue);
		});
	}

	@Override
	public int size() {
		return values.size();
	}

	@Override
	public Optional<E> valueAt(int ordinal) {
		return Optional.ofNullable(values.get(ordinal));
	}

	@Override
	public Stream<E> values() {
		return values.stream();
	}

	@Override
	public Optional<E> valueOf(String name) {
		return Optional.ofNullable(mapping.get(name));
	}
}
