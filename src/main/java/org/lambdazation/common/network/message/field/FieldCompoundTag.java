package org.lambdazation.common.network.message.field;

import org.lambdazation.common.network.message.Message;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public interface FieldCompoundTag<M extends Message<M>, F extends Message.Field<M, F, ?>>
	extends Message.Field<M, F, NBTTagCompound> {
	@Override
	default void encode(NBTTagCompound value, PacketBuffer buf) {
		buf.writeCompoundTag(value);
	}

	@Override
	default NBTTagCompound decode(PacketBuffer buf) {
		return buf.readCompoundTag();
	}
}
