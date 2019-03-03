package org.lambdazation.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.lambdazation.Lambdazation;

import net.minecraft.item.Item;

import javax.annotation.Nullable;

public final class ItemLambdaCrystal extends Item {
	public final Lambdazation lambdazation;
	private static double SIZE=Math.random()*100;
	private static double ENERGY=Math.random()*50+SIZE/2;

	public ItemLambdaCrystal(Lambdazation lambdazation, Properties properties) {
		super(properties);
		this.lambdazation = lambdazation;
	}

	@Nullable
	@Override
	public NBTTagCompound getShareTag(ItemStack stack) {
		NBTTagCompound nbtTagCompound=stack.getOrCreateTag();
		nbtTagCompound.setDouble("Size", SIZE);
		nbtTagCompound.setDouble("Energy", ENERGY);
		return nbtTagCompound;
	}
	public double getSize(ItemStack itemStack){
	    return itemStack.getTag().getDouble("Size");
    }
    public double getEnergy(ItemStack itemStack){
	    return itemStack.getTag().getDouble("Energy");
    }
}
