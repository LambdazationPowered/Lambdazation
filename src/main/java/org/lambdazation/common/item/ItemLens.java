package org.lambdazation.common.item;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.inventory.ContainerLens;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public final class ItemLens extends Item implements IInteractionObject {
	public final Lambdazation lambdazation;

	public ItemLens(Lambdazation lambdazation, Properties properties) {
		super(properties);

		this.lambdazation = lambdazation;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.swingArm(handIn);

		if (playerIn instanceof EntityPlayerMP)
			NetworkHooks.openGui((EntityPlayerMP) playerIn, this);

		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getCustomName() {
		return null;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerLens(lambdazation, playerInventory);
	}

	@Override
	public String getGuiID() {
		return ContainerLens.GUI_ID.toString();
	}
}