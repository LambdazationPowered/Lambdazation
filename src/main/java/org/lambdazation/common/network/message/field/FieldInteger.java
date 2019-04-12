package org.lambdazation.common.network.message.field;

import java.util.Optional;

import org.lambdazation.common.network.message.Message;
import org.lambdazation.common.network.message.field.feature.FeatureEndian;
import org.lambdazation.common.network.message.field.feature.FeatureVarying;

import net.minecraft.network.PacketBuffer;

public interface FieldInteger<M extends Message<M>, F extends Message.Field<M, F, ?>>
	extends Message.Field<M, F, Integer>, FeatureVarying, FeatureEndian {
	@Override
	default Optional<Integer> initial() {
		return Optional.of(0);
	}

	@Override
	default void encode(Integer value, PacketBuffer buf) {
		if (varying())
			buf.writeVarInt(value);
		else if (networkEndian())
			buf.writeInt(value);
		else
			buf.writeIntLE(value);
	}

	@Override
	default Integer decode(PacketBuffer buf) {
		if (varying())
			return buf.readVarInt();
		else if (networkEndian())
			return buf.readInt();
		else
			return buf.readIntLE();
	}
}
