package org.lambdazation.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public final class BoxedEnum<E extends Enum<E>> implements EnumValue<BoxedEnum<E>>, Comparable<BoxedEnum<E>> {
	protected final E enumValue;

	public BoxedEnum(E enumValue) {
		this.enumValue = enumValue;
	}

	@Override
	public String name() {
		return enumValue.name();
	}

	@Override
	public int ordinal() {
		return enumValue.ordinal();
	}

	@Override
	public String toString() {
		return enumValue.toString();
	}

	@Override
	public int compareTo(BoxedEnum<E> o) {
		return enumValue.compareTo(o.enumValue);
	}

	public static final class BoxedEnumClass<E extends Enum<E>> implements EnumObject<BoxedEnum<E>> {
		private final List<BoxedEnum<E>> values;
		private final Map<String, BoxedEnum<E>> mapping;
		
		public BoxedEnumClass(Class<E> enumClass) {
			values = new ArrayList<>();
			mapping = new HashMap<>();

			Arrays.stream(enumClass.getEnumConstants()).forEach(enumValue -> {
				BoxedEnum<E> value = new BoxedEnum<>(enumValue);
				values.add(value);
				mapping.put(enumValue.name(), value);
			});
		}

		@Override
		public int size() {
			return values.size();
		}

		@Override
		public Optional<BoxedEnum<E>> valueAt(int ordinal) {
			return Optional.ofNullable(values.get(ordinal));
		}

		@Override
		public Stream<BoxedEnum<E>> values() {
			return values.stream();
		}

		@Override
		public Optional<BoxedEnum<E>> valueOf(String name) {
			return Optional.ofNullable(mapping.get(name));
		}
	}
}

