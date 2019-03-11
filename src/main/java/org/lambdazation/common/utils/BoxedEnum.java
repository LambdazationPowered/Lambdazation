package org.lambdazation.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class BoxedEnum<T extends Enum<T>> implements EnumValue<BoxedEnum<T>>, Comparable<BoxedEnum<T>> {
	protected final T enumValue;

	public BoxedEnum(T enumValue) {
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
	public int compareTo(BoxedEnum<T> o) {
		return enumValue.compareTo(o.enumValue);
	}

	public static final class BoxedEnumClass<T extends Enum<T>> implements EnumObject<BoxedEnum<T>> {
		private final List<BoxedEnum<T>> values;
		private final Map<String, BoxedEnum<T>> mapping;
		
		public BoxedEnumClass(Class<T> enumClass) {
			values = new ArrayList<>();
			mapping = new HashMap<>();

			Arrays.stream(enumClass.getEnumConstants()).forEach(enumValue -> {
				BoxedEnum<T> value = new BoxedEnum<>(enumValue);
				values.add(value);
				mapping.put(enumValue.name(), value);
			});
		}

		@Override
		public List<BoxedEnum<T>> values() {
			return Collections.unmodifiableList(values);
		}

		@Override
		public Optional<BoxedEnum<T>> valueOf(String name) {
			return Optional.ofNullable(mapping.get(name));
		}
		
	}
}
