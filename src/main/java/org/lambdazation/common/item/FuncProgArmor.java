package org.lambdazation.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.lambdazation.Lambdazation;
import org.lambdazation.common.core.LambdazationTermFactory;

public class FuncProgArmor extends ItemArmor {
	public final Lambdazation lambdazation;
	public final LambdazationTermFactory lambdazationTermFactory;

	public FuncProgArmor(Lambdazation lambdazation, LambdazationTermFactory lambdazationTermFactory) {
		super(ArmorMaterial.valueOf("lambda_crystal"), EntityEquipmentSlot.CHEST, new Properties());
		this.lambdazation = lambdazation;
		this.lambdazationTermFactory = lambdazationTermFactory;
	}

	@Override
	public void onArmorTick(ItemStack stack, World world, EntityPlayer player) {

	}
}
