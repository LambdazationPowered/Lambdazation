package org.lambdazation.common.network.message.field;

import java.util.Optional;

import org.lambdazation.common.network.message.Message;

import net.minecraft.network.PacketBuffer;

public interface FieldByte<M extends Message<M>, F extends Message.Field<M, F, ?>>
	extends Message.Field<M, F, Byte> {
	@Override
	default Optional<Byte> initial() {
		return Optional.of((byte) 0);
	}

	@Override
	default void encode(Byte value, PacketBuffer buf) {
		buf.writeByte(value);
	}

	@Override
	default Byte decode(PacketBuffer buf) {
		return buf.readByte();
	}
}
