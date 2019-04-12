package org.lambdazation.common.network.message.field;

import java.util.Optional;

import org.lambdazation.common.network.message.Message;
import org.lambdazation.common.network.message.field.feature.FeatureEndian;

import net.minecraft.network.PacketBuffer;

public interface FieldFloat<M extends Message<M>, F extends Message.Field<M, F, ?>>
	extends Message.Field<M, F, Float>, FeatureEndian {
	@Override
	default Optional<Float> initial() {
		return Optional.of(0.0F);
	}

	@Override
	default void encode(Float value, PacketBuffer buf) {
		if (networkEndian())
			buf.writeFloat(value);
		else
			buf.writeFloatLE(value);
	}

	@Override
	default Float decode(PacketBuffer buf) {
		if (networkEndian())
			return buf.readFloat();
		else
			return buf.readFloatLE();
	}
}
