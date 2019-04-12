package org.lambdazation.common.network.message;

import java.util.Optional;
import java.util.function.Supplier;

import org.lambdazation.common.util.EnumValue;
import org.lambdazation.common.util.GeneralizedBuilder;
import org.lambdazation.common.util.EnumValue.EnumObject;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public interface Message<M extends Message<M>> {
	default <F extends Field<M, F, T>, T> T get(F field) {
		return field.get(concrete());
	}

	M concrete();

	interface Handler<M extends Message<M>, F extends Field<M, F, ?>> {
		default void encode(M msg, PacketBuffer buf) {
			metadata().values().forEachOrdered(field -> field.write(msg, buf));
		}

		default M decode(PacketBuffer buf) {
			Builder<M, F, ?> builder = builder();
			metadata().values().forEachOrdered(field -> field.read(builder, buf));
			return builder.build();
		}

		default void consume(M msg, Supplier<NetworkEvent.Context> ctx) {
			handle(msg, ctx.get());
		}

		EnumObject<F> metadata();

		Builder<M, F, ?> builder();

		void handle(M msg, NetworkEvent.Context ctx);
	}

	interface Builder<M extends Message<M>, F extends Field<M, F, ?>, B extends Builder<M, F, B>>
		extends GeneralizedBuilder<B, M> {
		<T> B with(Field<M, F, T> field, T value);
	}

	interface Field<M extends Message<M>, F extends Field<M, F, ?>, T> extends EnumValue<F> {
		default <B extends Builder<M, F, B>> B set(B builder, T value) {
			return builder.with(this, value);
		}

		default Optional<T> initial() {
			return Optional.empty();
		}

		default void write(M msg, PacketBuffer buf) {
			encode(get(msg), buf);
		}

		default void read(Builder<M, F, ?> builder, PacketBuffer buf) {
			builder.with(this, decode(buf));
		}

		void encode(T value, PacketBuffer buf);

		T decode(PacketBuffer buf);

		T get(M msg);
	}
}
