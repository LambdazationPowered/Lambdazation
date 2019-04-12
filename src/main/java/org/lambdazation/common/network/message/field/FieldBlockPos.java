package org.lambdazation.common.network.message.field;

import org.lambdazation.common.network.message.Message;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;

public interface FieldBlockPos<M extends Message<M>, F extends Message.Field<M, F, ?>>
	extends Message.Field<M, F, BlockPos> {
	@Override
	default void encode(BlockPos value, PacketBuffer buf) {
		buf.writeBlockPos(value);
	}

	@Override
	default BlockPos decode(PacketBuffer buf) {
		return buf.readBlockPos();
	}
}
