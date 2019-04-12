package org.lambdazation.common.network.message.field;

import org.lambdazation.common.network.message.Message;
import org.lambdazation.common.network.message.field.feature.FeatureLimit;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.network.PacketBuffer;

public interface FieldByteArray<M extends Message<M>, F extends Message.Field<M, F, ?>>
	extends Message.Field<M, F, byte[]>, FeatureLimit {
	@Override
	default void encode(byte[] value, PacketBuffer buf) {
		if (limit() >= 0 && value.length > limit())
			throw new EncoderException("Limit reached (was " + value.length + " bytes, max " + limit() + ")");
		buf.writeVarInt(value.length);
		buf.writeBytes(value);
	}

	@Override
	default byte[] decode(PacketBuffer buf) {
		int len = buf.readVarInt();
		if (len < 0)
			throw new DecoderException("Receiving bytes less than zero");
		if (limit() >= 0 && len > limit())
			throw new DecoderException("Limit reached (was " + len + " bytes, max " + limit() + ")");
		byte[] value = new byte[len];
		buf.readBytes(value);
		return value;
	}
}
