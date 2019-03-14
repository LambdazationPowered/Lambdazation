package org.lambdazation.common.utils;

import java.util.function.Function;

public interface GeneralizedBuilder<B extends GeneralizedBuilder<B, T>, T> {
	default B accept(Function<? super B, ? extends B> f) {
		return f.apply(concrete());
	}

	B concrete();

	T build();
}
