package org.lambdazation.common.network.message.field;

import java.nio.charset.StandardCharsets;

import org.lambdazation.common.network.message.Message;
import org.lambdazation.common.network.message.field.feature.FeatureLimit;

import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.network.PacketBuffer;

public interface FieldString<M extends Message<M>, F extends Message.Field<M, F, ?>>
	extends Message.Field<M, F, String>, FeatureLimit {
	@Override
	default void encode(String value, PacketBuffer buf) {
		byte[] arr = value.getBytes(StandardCharsets.UTF_8);
		if (limit() >= 0 && arr.length > limit())
			throw new EncoderException("Limit reached (was " + arr.length + " bytes, max " + limit() + ")");
		buf.writeVarInt(arr.length);
		buf.writeBytes(arr);
	}

	@Override
	default String decode(PacketBuffer buf) {
		int len = buf.readVarInt();
		if (len < 0)
			throw new DecoderException("Receiving bytes less than zero");
		if (limit() >= 0 && len > limit())
			throw new DecoderException("Limit reached (was " + len + " bytes, max " + limit() + ")");
		String value = buf.toString(buf.readerIndex(), len, StandardCharsets.UTF_8);
		buf.skipBytes(len);
		return value;
	}
}
