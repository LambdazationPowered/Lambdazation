package org.lambdazation.common.network.message.field;

import java.util.Optional;

import org.lambdazation.common.network.message.Message;
import org.lambdazation.common.network.message.field.feature.FeatureEndian;
import org.lambdazation.common.network.message.field.feature.FeatureVarying;

import net.minecraft.network.PacketBuffer;

public interface FieldLong<M extends Message<M>, F extends Message.Field<M, F, ?>>
	extends Message.Field<M, F, Long>, FeatureVarying, FeatureEndian {
	@Override
	default Optional<Long> initial() {
		return Optional.of(0L);
	}

	@Override
	default void encode(Long value, PacketBuffer buf) {
		if (varying())
			buf.writeVarLong(value);
		else if (networkEndian())
			buf.writeLong(value);
		else
			buf.writeLongLE(value);
	}

	@Override
	default Long decode(PacketBuffer buf) {
		if (varying())
			return buf.readVarLong();
		else if (networkEndian())
			return buf.readLong();
		else
			return buf.readLongLE();
	}
}
