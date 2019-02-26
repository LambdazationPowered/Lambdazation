package org.lambdazation.common.core;

import org.lambdazation.Lambdazation;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public final class LambdazationItemGroup extends ItemGroup {
	public final Lambdazation lambdazation;

	public LambdazationItemGroup(Lambdazation lambdazation) {
		super("lambdazation");

		this.lambdazation = lambdazation;
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(lambdazation.lambdazationItems.itemLambdaCrystal);
	}
}
