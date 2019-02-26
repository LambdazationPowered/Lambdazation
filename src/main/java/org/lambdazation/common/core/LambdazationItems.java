package org.lambdazation.common.core;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.item.ItemLambdaCrystal;

import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public final class LambdazationItems {
	public final Lambdazation lambdazation;

	public final ItemLambdaCrystal itemLambdaCrystal;

	public LambdazationItems(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

		itemLambdaCrystal = new ItemLambdaCrystal(new Item.Properties()
			.defaultMaxDamage(Integer.MAX_VALUE)
			.group(lambdazation.lambdazationItemGroup)
			.rarity(EnumRarity.EPIC)
			.setNoRepair());
		itemLambdaCrystal.setRegistryName(new ResourceLocation("lambdazation:lambda_crystal"));
	}

	public void registerItems(RegistryEvent.Register<Item> e) {
		e.getRegistry().register(itemLambdaCrystal);
	}
}
