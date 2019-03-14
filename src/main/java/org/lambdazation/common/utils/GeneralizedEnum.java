package org.lambdazation.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

public abstract class GeneralizedEnum<E extends GeneralizedEnum<E>> implements EnumValue<E>, Comparable<E> {
	private final String name;
	private final int ordinal;

	protected GeneralizedEnum(String name, int ordinal) {
		this.name = name;
		this.ordinal = ordinal;
	}

	@Override
	public final String name() {
		return name;
	}

	@Override
	public final int ordinal() {
		return ordinal;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(E o) {
		return ordinal - o.ordinal();
	}

	public static final class Metadata<E extends GeneralizedEnum<E>> implements EnumObject<E> {
		private final List<E> values;
		private final Map<String, E> mapping;

		private Metadata(List<E> values, Map<String, E> mapping) {
			this.values = values;
			this.mapping = mapping;
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

		public static <E extends GeneralizedEnum<E>> Builder<E> builder() {
			return new Builder<>();
		}

		public static final class Builder<E extends GeneralizedEnum<E>> implements GeneralizedBuilder<Builder<E>, Metadata<E>> {
			private final List<E> values;
			private final Map<String, E> mapping;
			private int ordinal;

			private Builder() {
				values = new ArrayList<>();
				mapping = new HashMap<>();
				ordinal = 0;
			}

			public <T extends E> T withValue(String name, BiFunction<String, Integer, T> constructor) {
				if (mapping.containsKey(name))
					throw new IllegalArgumentException("Duplicate enum name");

				T value = constructor.apply(name, ordinal);
				if (values.contains(value))
					throw new IllegalArgumentException("Duplicate enum value");

				values.add(value);
				mapping.put(name, value);
				ordinal++;

				return value;
			}

			@Override
			public Builder<E> concrete() {
				return this;
			}

			@Override
			public Metadata<E> build() {
				return new Metadata<>(values, mapping);
			}
		}
	}
}
