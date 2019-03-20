package org.lambdazation.common.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ValueBuilder {
	public static <T> T build(T value, Consumer<? super T> builder) {
		return internal(() -> value, builder, Function.identity()).get();
	}

	public static <T> T build(T value, Consumer<? super T> builder, Function<? super T, ? extends T> finalizer) {
		return internal(() -> value, builder, Function.identity()).get();
	}

	public static <T> T build(Supplier<? extends T> constructor, Consumer<? super T> builder) {
		return internal(constructor, builder, Function.identity()).get();
	}

	public static <T> T build(Supplier<? extends T> constructor, Consumer<? super T> builder,
		Function<? super T, ? extends T> finalizer) {
		return internal(constructor, builder, finalizer).get();
	}

	public static <T> Supplier<? extends T> define(T value, Consumer<? super T> builder) {
		return internal(() -> value, builder, Function.identity());
	}

	public static <T> Supplier<? extends T> define(T value, Consumer<? super T> builder,
		Function<? super T, ? extends T> finalizer) {
		return internal(() -> value, builder, Function.identity());
	}

	public static <T> Supplier<? extends T> define(Supplier<? extends T> constructor, Consumer<? super T> builder) {
		return internal(constructor, builder, Function.identity());
	}

	public static <T> Supplier<? extends T> define(Supplier<? extends T> constructor, Consumer<? super T> builder,
		Function<? super T, ? extends T> finalizer) {
		return internal(constructor, builder, finalizer);
	}

	private static <T> Supplier<? extends T> internal(Supplier<? extends T> constructor, Consumer<? super T> builder,
		Function<? super T, ? extends T> finalizer) {
		return () -> {
			T value = constructor.get();
			builder.accept(value);
			return finalizer.apply(value);
		};
	}
}
