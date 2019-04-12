package org.lambdazation.common.network.message.field;

import org.lambdazation.common.network.message.Message;

import net.minecraft.network.PacketBuffer;

public interface FieldEnum<M extends Message<M>, F extends Message.Field<M, F, ?>, E extends Enum<E>>
	extends Message.Field<M, F, E> {
	@Override
	default void encode(E value, PacketBuffer buf) {
		buf.writeEnumValue(value);
	}

	@Override
	default E decode(PacketBuffer buf) {
		return buf.readEnumValue(enumClass());
	}

	Class<E> enumClass();
}
