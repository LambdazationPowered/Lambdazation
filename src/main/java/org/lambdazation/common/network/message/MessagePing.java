package org.lambdazation.common.network.message;

import org.lambdazation.common.util.EnumMetadata;

import net.minecraft.network.PacketBuffer;

public final class MessagePing implements Message<MessagePing> {
	public MessagePing() {

	}

	@Override
	public MessagePing concrete() {
		return this;
	}

	public enum FieldPing implements Message.Field<MessagePing, FieldPing, Void> {
		;

		public static final EnumMetadata<FieldPing> METADATA = new EnumMetadata<>(FieldPing.class);

		@Override
		public void encode(Void value, PacketBuffer buf) {
			throw new AbstractMethodError();
		}

		@Override
		public Void decode(PacketBuffer buf) {
			throw new AbstractMethodError();
		}

		@Override
		public Void get(MessagePing msg) {
			throw new AbstractMethodError();
		}
	}
}
