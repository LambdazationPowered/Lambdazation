package org.lambdazation.common.network.message.field;

import java.util.Optional;

import org.lambdazation.common.network.message.Message;

import net.minecraft.network.PacketBuffer;

public interface FieldCharacter<M extends Message<M>, F extends Message.Field<M, F, ?>>
	extends Message.Field<M, F, Character> {
	@Override
	default Optional<Character> initial() {
		return Optional.of('\0');
	}

	@Override
	default void encode(Character value, PacketBuffer buf) {
		buf.writeChar(value);
	}

	@Override
	default Character decode(PacketBuffer buf) {
		return buf.readChar();
	}
}
