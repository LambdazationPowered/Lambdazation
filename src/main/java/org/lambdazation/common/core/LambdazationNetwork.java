package org.lambdazation.common.core;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.network.NetworkHandler;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public final class LambdazationNetwork {
	public final Lambdazation lambdazation;

	public final SimpleChannel simpleChannel;
	public final NetworkHandler networkHandler;

	public LambdazationNetwork(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

		this.simpleChannel = NetworkRegistry.ChannelBuilder
			.named(new ResourceLocation("lambdazation", "network"))
			.networkProtocolVersion(() -> "0")
			.clientAcceptedVersions("0"::equals)
			.serverAcceptedVersions("0"::equals)
			.simpleChannel();
		this.networkHandler = new NetworkHandler(lambdazation, simpleChannel);
	}

	public void registerNetwork(FMLCommonSetupEvent e) {
		networkHandler.registerMessage();
	}
}
