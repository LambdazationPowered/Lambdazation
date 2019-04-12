package org.lambdazation.common.network.message.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.lambdazation.common.network.message.Message;

public final class BuilderGeneric<M extends Message<M>, F extends Message.Field<M, F, ?>>
	implements Message.Builder<M, F, BuilderGeneric<M, F>> {
	private final Message.Handler<M, F> handler;
	private final Function<Initializer, M> constructor;
	private final Initializer initializer;

	public BuilderGeneric(Message.Handler<M, F> handler, Function<Initializer, M> constructor) {
		this.handler = handler;
		this.constructor = constructor;
		this.initializer = new Initializer();
	}

	@Override
	public <T> BuilderGeneric<M, F> with(Message.Field<M, F, T> field, T value) {
		initializer.set(field, value);

		return this;
	}

	@Override
	public BuilderGeneric<M, F> concrete() {
		return this;
	}

	@Override
	public M build() {
		initializer.validate();

		return constructor.apply(initializer);
	}

	public final class Initializer {
		private final Map<Message.Field<M, F, ?>, ?> values;

		Initializer() {
			values = new HashMap<>();
			handler.metadata().values().forEach(field -> field.initial().ifPresent(value -> unsafe().put(field, value)));
		}

		public <T> T get(Message.Field<M, F, T> field) {
			return this.<T> coerce().get(field);
		}

		<T> void set(Message.Field<M, F, T> field, T value) {
			this.<T> coerce().put(field, value);
		}

		Map<Message.Field<M, F, ?>, Object> unsafe() {
			@SuppressWarnings("unchecked")
			Map<Message.Field<M, F, ?>, Object> values = (Map<Message.Field<M, F, ?>, Object>) (Map<?, ?>) this.values;
			return values;
		}

		<T> Map<Message.Field<M, F, T>, T> coerce() {
			@SuppressWarnings("unchecked")
			Map<Message.Field<M, F, T>, T> values = (Map<Message.Field<M, F, T>, T>) (Map<?, ?>) this.values;
			return values;
		}

		void validate() {
			if (values.size() != handler.metadata().size())
				throw new IllegalStateException("Field uninitialized");
		}
	}
}
