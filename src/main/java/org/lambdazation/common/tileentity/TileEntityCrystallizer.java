package org.lambdazation.common.tileentity;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;
import org.lambdazation.Lambdazation;
import org.lambdazation.common.item.ItemLambdaCrystal;
import org.lamcalcj.ast.Lambda;
import org.lamcalcj.utils.Utils;
import scala.collection.immutable.HashMap;
import scala.collection.immutable.Map;

public final class TileEntityCrystallizer extends TileEntity {
	public final Lambdazation lambdazation;

	public TileEntityCrystallizer(Lambdazation lambdazation) {
		super(lambdazation.lambdazationTileEntityTypes.tileEntityTypeCrystallizer);

		this.lambdazation = lambdazation;
	}
	public ItemStack mergeCrystals() {
		Map<Lambda.Identifier, Lambda.Identifier> identifierMap = new HashMap<>();
		ItemStack itemStack0 = getItemStackInSlot(0);
		ItemStack itemStack1 = getItemStackInSlot(1);
		Item item0 = itemStack0.getItem();
		Item item1 = itemStack1.getItem();
		if (item0 instanceof ItemLambdaCrystal && item1 instanceof ItemLambdaCrystal) {
			if (Utils.isAlphaEquivalent(((ItemLambdaCrystal) item0).getTerm(itemStack0), ((ItemLambdaCrystal) item1).getTerm(itemStack1),
					identifierMap)) {
				ItemStack newCrystal = new ItemLambdaCrystal(lambdazation, new Item.Properties()).getDefaultInstance();
				NBTTagCompound nbtTagCompound = new NBTTagCompound();
				nbtTagCompound.setInt("energy", ((ItemLambdaCrystal) item0).getEnergy(itemStack0) + ((ItemLambdaCrystal) item1).getEnergy(itemStack1));
				newCrystal.setTag(nbtTagCompound);
				return newCrystal;
			}
			else return null;
		}
		else return null;
	}
	public ItemStack getItemStackInSlot(int slot){
		return new ItemStackHandler().getStackInSlot(slot);
	}
}
