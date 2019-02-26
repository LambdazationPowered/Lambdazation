package org.lambdazation.common.item;

import org.lambdazation.Lambdazation;

import net.minecraft.item.Item;

public final class ItemLambdaCrystal extends Item {
	public final Lambdazation lambdazation;

	public ItemLambdaCrystal(Lambdazation lambdazation, Properties properties) {
		super(properties);

		this.lambdazation = lambdazation;
	}
}
