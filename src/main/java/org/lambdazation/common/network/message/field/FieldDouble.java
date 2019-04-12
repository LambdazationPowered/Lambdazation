package org.lambdazation.common.network.message.field;

import java.util.Optional;

import org.lambdazation.common.network.message.Message;
import org.lambdazation.common.network.message.field.feature.FeatureEndian;

import net.minecraft.network.PacketBuffer;

public interface FieldDouble<M extends Message<M>, F extends Message.Field<M, F, ?>>
	extends Message.Field<M, F, Double>, FeatureEndian {
	@Override
	default Optional<Double> initial() {
		return Optional.of(0.0D);
	}

	@Override
	default void encode(Double value, PacketBuffer buf) {
		if (networkEndian())
			buf.writeDouble(value);
		else
			buf.writeDoubleLE(value);
	}

	@Override
	default Double decode(PacketBuffer buf) {
		if (networkEndian())
			return buf.readDouble();
		else
			return buf.readDoubleLE();
	}
}
