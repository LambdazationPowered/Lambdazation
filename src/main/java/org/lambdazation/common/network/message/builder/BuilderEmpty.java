package org.lambdazation.common.network.message.builder;

import java.util.function.Supplier;

import org.lambdazation.common.network.message.Message;
import org.lambdazation.common.network.message.Message.Field;

public final class BuilderEmpty<M extends Message<M>, F extends Message.Field<M, F, ?>>
	implements Message.Builder<M, F, BuilderEmpty<M, F>> {
	private final Supplier<M> constructor;

	public BuilderEmpty(Supplier<M> constructor) {
		this.constructor = constructor;
	}

	@Override
	public BuilderEmpty<M, F> concrete() {
		return this;
	}

	@Override
	public M build() {
		return constructor.get();
	}

	@Override
	public <T> BuilderEmpty<M, F> with(Field<M, F, T> field, T value) {
		throw new IllegalStateException("Field inaccessible");
	}
}
