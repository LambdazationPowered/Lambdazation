package org.lambdazation.common.network.message.handler;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.network.message.Message;
import org.lambdazation.common.network.message.Message.Builder;
import org.lambdazation.common.network.message.MessagePing;
import org.lambdazation.common.network.message.MessagePing.FieldPing;
import org.lambdazation.common.network.message.builder.BuilderEmpty;
import org.lambdazation.common.util.EnumValue.EnumObject;

import net.minecraftforge.fml.network.NetworkEvent.Context;

public final class HandlerPing implements Message.Handler<MessagePing, MessagePing.FieldPing> {
	public final Lambdazation lambdazation;

	public HandlerPing(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;
	}

	@Override
	public EnumObject<FieldPing> metadata() {
		return MessagePing.FieldPing.METADATA;
	}

	@Override
	public Builder<MessagePing, FieldPing, ?> builder() {
		return new BuilderEmpty<>(MessagePing::new);
	}

	@Override
	public void handle(MessagePing msg, Context ctx) {
		ctx.setPacketHandled(true);
		System.out.println("Ping");
	}
}
