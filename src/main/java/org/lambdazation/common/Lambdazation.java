package org.lambdazation.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lambdazation.common.item.ItemLambdaCrystal;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;

@Mod("lambdazation")
public final class Lambdazation {
	public final Logger logger;
	public final Items items;
	public final Blocks blocks;
	public final EventHandlers eventHandlers;

	public Lambdazation() {
		logger = LogManager.getLogger();
		items = new Items();
		blocks = new Blocks();
		eventHandlers = new EventHandlers();

		MinecraftForge.EVENT_BUS.register(eventHandlers);
		FMLJavaModLoadingContext.get().getModEventBus().register(eventHandlers);
	}

	public final class Items {
		public final ItemLambdaCrystal itemLambdaCrystal;

		public Items() {
			itemLambdaCrystal = new ItemLambdaCrystal(new Item.Properties());
			itemLambdaCrystal.setRegistryName(new ResourceLocation("lambdazation:lambda_crystal"));
		}

		public void registerItems(IForgeRegistry<Item> registry) {
			registry.register(itemLambdaCrystal);
		}
	}

	public final class Blocks {
		public Blocks() {

		}

		public void registerBlocks(IForgeRegistry<Block> registry) {

		}
	}

	public final class EventHandlers {
		@SubscribeEvent
		public void registerItems(RegistryEvent.Register<Item> e) {
			items.registerItems(e.getRegistry());
		}

		@SubscribeEvent
		public void registerBlocks(RegistryEvent.Register<Block> e) {
			blocks.registerBlocks(e.getRegistry());
		}
	}
}
