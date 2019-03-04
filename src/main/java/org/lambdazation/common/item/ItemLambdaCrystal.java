package org.lambdazation.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lambdazation.Lambdazation;

import net.minecraft.item.Item;

public final class ItemLambdaCrystal extends Item {
	public final Lambdazation lambdazation;

	public ItemLambdaCrystal(Lambdazation lambdazation, Properties properties) {
		super(properties);

		this.lambdazation = lambdazation;
	}

	@Override
	public NBTTagCompound getShareTag(ItemStack stack) {
		NBTTagCompound nbtTagCompound = stack.getOrCreateTag();
		nbtTagCompound.setDouble("Size", 0);
		nbtTagCompound.setDouble("Energy", 0);
		return nbtTagCompound;
	}

	public double getSize(ItemStack itemStack) {
		return itemStack.getTag().getDouble("Size");
	}

	public double getEnergy(ItemStack itemStack) {
		return itemStack.getTag().getDouble("Energy");
	}
}
