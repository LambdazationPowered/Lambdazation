package org.lambdazation.common.core;

import org.lambdazation.Lambdazation;
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

	public final ItemBlock itemBlockLambdaOre;
	public final ItemBlock itemBlockCrystallizer;

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

		itemBlockLambdaOre = new ItemBlock(lambdazation.lambdazationBlocks.blockLambdaOre, new Item.Properties()
			.group(lambdazation.lambdazationItemGroup)
			.rarity(EnumRarity.EPIC));
		itemBlockLambdaOre.setRegistryName(new ResourceLocation("lambdazation:lambda_ore"));
		itemBlockCrystallizer = new ItemBlock(lambdazation.lambdazationBlocks.blockCrystallizer, new Item.Properties()
			.group(lambdazation.lambdazationItemGroup)
			.rarity(EnumRarity.COMMON));
		itemBlockCrystallizer.setRegistryName(new ResourceLocation("lambdazation:crystallizer"));
	}

	public void registerItems(RegistryEvent.Register<Item> e) {
		e.getRegistry().register(itemLambdaCrystal);
		e.getRegistry().register(itemLens);
		e.getRegistry().register(itemBlockLambdaOre);
		e.getRegistry().register(itemBlockCrystallizer);
	}

	public void finalizeItems(RegistryEvent.Register<Item> e) {

	}
}
