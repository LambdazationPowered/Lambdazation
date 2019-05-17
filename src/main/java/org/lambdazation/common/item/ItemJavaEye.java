package org.lambdazation.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.lambdazation.Lambdazation;

public class ItemJavaEye extends Item {
	public final Lambdazation lambdazation;

	public ItemJavaEye(Lambdazation lambdazation, Properties properties) {
		super(properties);
		this.lambdazation = lambdazation;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		Minecraft.getInstance().renderGlobal.markBlockRangeForRenderUpdate((int) playerIn.posX - 24,
			(int) playerIn.posY, (int) playerIn.posZ - 24, (int) playerIn.posX + 24, 0, (int) playerIn.posZ + 24);
		//todo
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
