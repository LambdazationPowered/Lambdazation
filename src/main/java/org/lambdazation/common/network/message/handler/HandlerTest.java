package org.lambdazation.common.network.message.handler;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.network.message.Message;
import org.lambdazation.common.network.message.Message.Builder;
import org.lambdazation.common.network.message.MessageTest;
import org.lambdazation.common.network.message.MessageTest.FieldTest;
import org.lambdazation.common.network.message.builder.BuilderGeneric;
import org.lambdazation.common.util.EnumValue.EnumObject;

import net.minecraftforge.fml.network.NetworkEvent.Context;

public final class HandlerTest implements Message.Handler<MessageTest, MessageTest.FieldTest<?>> {
	public final Lambdazation lambdazation;

	public HandlerTest(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;
	}

	@Override
	public EnumObject<FieldTest<?>> metadata() {
		return MessageTest.FieldTest.METADATA;
	}

	@Override
	public Builder<MessageTest, FieldTest<?>, ?> builder() {
		return new BuilderGeneric<>(this, MessageTest::new);
	}

	@Override
	public void handle(MessageTest msg, Context ctx) {
		ctx.setPacketHandled(true);
		System.out.println(msg);
	}
}
