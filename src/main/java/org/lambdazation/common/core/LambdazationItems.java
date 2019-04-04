package org.lambdazation.common.core;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.item.ItemCalibrator;
import org.lambdazation.common.item.ItemLambdaCrystal;
import org.lambdazation.common.item.ItemLens;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public final class LambdazationItems {
	public final Lambdazation lambdazation;

	public final ItemLambdaCrystal itemLambdaCrystal;
	public final ItemLens itemLens;
	public final ItemCalibrator itemCalibrator;

	public final ItemBlock itemBlockLambdaOre;
	public final ItemBlock itemBlockLambdaBlock;
	public final ItemBlock itemBlockCrystallizer;
	public final ItemBlock itemBlockTransformer;
	public final ItemBlock itemBlockCharger;
	public final ItemBlock itemBlockReducer;

	public LambdazationItems(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

		itemLambdaCrystal = new ItemLambdaCrystal(lambdazation, new Item.Properties()
			.defaultMaxDamage(Integer.MAX_VALUE)
			.group(lambdazation.lambdazationItemGroup)
			.rarity(EnumRarity.EPIC)
			.setNoRepair());
		itemLambdaCrystal.setRegistryName(new ResourceLocation("lambdazation:lambda_crystal"));

		itemLens = new ItemLens(lambdazation, new Item.Properties()
			.group(lambdazation.lambdazationItemGroup));
		itemLens.setRegistryName(new ResourceLocation("lambdazation:lens"));
		itemCalibrator = new ItemCalibrator(lambdazation, new Item.Properties()
			.group(lambdazation.lambdazationItemGroup));
		itemCalibrator.setRegistryName(new ResourceLocation("lambdazation:calibrator"));

		itemBlockLambdaOre = new ItemBlock(lambdazation.lambdazationBlocks.blockLambdaOre, new Item.Properties()
			.group(lambdazation.lambdazationItemGroup)
			.rarity(EnumRarity.EPIC));
		itemBlockLambdaOre.setRegistryName(new ResourceLocation("lambdazation:lambda_ore"));
		itemBlockLambdaBlock = new ItemBlock(lambdazation.lambdazationBlocks.blockLambdaBlock, new Item.Properties()
			.group(lambdazation.lambdazationItemGroup)
			.rarity(EnumRarity.EPIC));
		itemBlockLambdaBlock.setRegistryName(new ResourceLocation("lambdazation:lambda_block"));
		itemBlockCrystallizer = new ItemBlock(lambdazation.lambdazationBlocks.blockCrystallizer, new Item.Properties()
			.group(lambdazation.lambdazationItemGroup)
			.rarity(EnumRarity.COMMON));
		itemBlockCrystallizer.setRegistryName(new ResourceLocation("lambdazation:crystallizer"));
		itemBlockTransformer = new ItemBlock(lambdazation.lambdazationBlocks.blockTransformer, new Item.Properties()
			.group(lambdazation.lambdazationItemGroup)
			.rarity(EnumRarity.COMMON));
		itemBlockTransformer.setRegistryName(new ResourceLocation("lambdazation:transformer"));
		itemBlockCharger = new ItemBlock(lambdazation.lambdazationBlocks.blockCharger, new Item.Properties()
			.group(lambdazation.lambdazationItemGroup)
			.rarity(EnumRarity.COMMON));
		itemBlockCharger.setRegistryName(new ResourceLocation("lambdazation:charger"));
		itemBlockReducer = new ItemBlock(lambdazation.lambdazationBlocks.blockReducer, new Item.Properties()
			.group(lambdazation.lambdazationItemGroup)
			.rarity(EnumRarity.COMMON));
		itemBlockReducer.setRegistryName(new ResourceLocation("lambdazation:reducer"));
	}

	public void registerItems(RegistryEvent.Register<Item> e) {
		e.getRegistry().register(itemLambdaCrystal);
		e.getRegistry().register(itemLens);
		e.getRegistry().register(itemCalibrator);
		e.getRegistry().register(itemBlockLambdaOre);
		e.getRegistry().register(itemBlockLambdaBlock);
		e.getRegistry().register(itemBlockCrystallizer);
		e.getRegistry().register(itemBlockTransformer);
		e.getRegistry().register(itemBlockCharger);
		e.getRegistry().register(itemBlockReducer);
	}

	public void finalizeItems(RegistryEvent.Register<Item> e) {

	}
}
