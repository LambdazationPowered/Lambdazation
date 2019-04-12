package org.lambdazation.common.network.message.field;

import org.lambdazation.common.network.message.Message;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public interface FieldItemStack<M extends Message<M>, F extends Message.Field<M, F, ?>>
	extends Message.Field<M, F, ItemStack> {
	@Override
	default void encode(ItemStack value, PacketBuffer buf) {
		buf.writeItemStack(value);
	}

	@Override
	default ItemStack decode(PacketBuffer buf) {
		return buf.readItemStack();
	}
}
