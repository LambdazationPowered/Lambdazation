package org.lambdazation.common.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lambdazation.Lambdazation;
import org.lambdazation.common.entity.EntityCSharp;
import org.lambdazation.common.entity.EntityJava;

public class ItemOOPSoul extends Item {
	//todo NYI It should be a material for recipes,but I still don't know what to recipe
	public final Lambdazation lambdazation;

	public ItemOOPSoul(Lambdazation lambdazation, Properties properties) {
		super(properties);
		this.lambdazation = lambdazation;
	}

	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target,
		EnumHand hand) {
		World world = target.world;
		if (target instanceof EntitySquid) {
			NBTTagCompound nbtTagCompound = new NBTTagCompound();
			nbtTagCompound.setInt("oop_amounts", 1);
			int oopAmounts = nbtTagCompound.getInt("oop_amounts");
			if (oopAmounts >= 1) {
				nbtTagCompound.setInt("oop_amount", oopAmounts + 1);
			}
			if (oopAmounts >= 10) {
				target.remove();
				EntityCSharp entityCSharp = lambdazation.lambdazationEntityTypes.entityTypeCSharp.create(world);
				BlockPos blockPos = target.getPosition();
				if (entityCSharp != null) {
					entityCSharp.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
					world.spawnEntity(entityCSharp);
				}
			}
			return true;
		}
		if (target instanceof EntitySpider) {
			NBTTagCompound nbtTagCompound = new NBTTagCompound();
			nbtTagCompound.setInt("oop_amounts", 1);
			int oopAmounts = nbtTagCompound.getInt("oop_amounts");
			if (oopAmounts >= 1) {
				nbtTagCompound.setInt("oop_amount", oopAmounts + 1);
			}
			if (oopAmounts >= 10) {
				target.remove();
				EntityJava entityJava = lambdazation.lambdazationEntityTypes.entityTypeJava.create(world);
				BlockPos blockPos = target.getPosition();
				if (entityJava != null) {
					entityJava.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
					world.spawnEntity(entityJava);
				}
			}
			return true;
		} else
			return false;
	}
}
