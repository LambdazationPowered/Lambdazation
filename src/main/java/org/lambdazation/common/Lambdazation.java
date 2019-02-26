package org.lambdazation.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("lambdazation")
public final class Lambdazation {
	private final Logger logger;

	public Lambdazation() {
		logger = LogManager.getLogger();

		MinecraftForge.EVENT_BUS.register(this);
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> e) {
		logger.info("registerBlocks");
	}

	@SubscribeEvent
	public void registerItems(RegistryEvent.Register<Item> e) {
		logger.info("registerItems");
	}
}
