package org.lambdazation.common.network.message.field;

import org.lambdazation.common.network.message.Message;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

public interface FieldTextComponent<M extends Message<M>, F extends Message.Field<M, F, ?>>
	extends Message.Field<M, F, ITextComponent> {
	@Override
	default void encode(ITextComponent value, PacketBuffer buf) {
		buf.writeTextComponent(value);
	}

	@Override
	default ITextComponent decode(PacketBuffer buf) {
		return buf.readTextComponent();
	}
}
