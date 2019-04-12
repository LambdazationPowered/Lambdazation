package org.lambdazation.common.network;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.network.message.Message;
import org.lambdazation.common.network.message.MessagePing;
import org.lambdazation.common.network.message.MessageTest;
import org.lambdazation.common.network.message.handler.HandlerPing;
import org.lambdazation.common.network.message.handler.HandlerTest;

import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class NetworkHandler {
	public final Lambdazation lambdazation;
	public final SimpleChannel simpleChannel;

	public final HandlerPing handlerPing;
	public final HandlerTest handlerTest;

	public NetworkHandler(Lambdazation lambdazation, SimpleChannel simpleChannel) {
		this.lambdazation = lambdazation;
		this.simpleChannel = simpleChannel;

		this.handlerPing = new HandlerPing(lambdazation);
		this.handlerTest = new HandlerTest(lambdazation);
	}

	public <M extends Message<M>> void sendMessage(PacketDistributor.PacketTarget target, M msg) {
		simpleChannel.send(target, msg);
	}

	public void registerMessage() {
		registerHandler(handlerPing, MessagePing.class, 0);
		registerHandler(handlerTest, MessageTest.class, 1);
	}

	private <M extends Message<M>> void registerHandler(Message.Handler<M, ?> handler,
		Class<M> messageClass, int id) {
		simpleChannel.messageBuilder(messageClass, id)
			.encoder(handler::encode)
			.decoder(handler::decode)
			.consumer(handler::consume)
			.add();
	}
}
