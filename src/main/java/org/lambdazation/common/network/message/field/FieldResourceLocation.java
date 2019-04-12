package org.lambdazation.common.network.message.field;

import org.lambdazation.common.network.message.Message;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

public interface FieldResourceLocation<M extends Message<M>, F extends Message.Field<M, F, ?>>
	extends Message.Field<M, F, ResourceLocation> {
	@Override
	default void encode(ResourceLocation value, PacketBuffer buf) {
		buf.writeResourceLocation(value);
	}

	@Override
	default ResourceLocation decode(PacketBuffer buf) {
		return buf.readResourceLocation();
	}
}
