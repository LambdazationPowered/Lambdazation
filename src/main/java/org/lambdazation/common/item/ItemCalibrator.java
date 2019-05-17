package org.lambdazation.common.item;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.inventory.ContainerCalibrator;

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

public final class ItemCalibrator extends Item implements IInteractionObject {
	public final Lambdazation lambdazation;

	public ItemCalibrator(Lambdazation lambdazation, Properties properties) {
		super(properties);

		this.lambdazation = lambdazation;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.swingArm(handIn);

		if (worldIn.isRemote)
			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));

		if (!(playerIn instanceof EntityPlayerMP))
			return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
		EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerIn;

		NetworkHooks.openGui(entityPlayerMP, this);

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
		return new ContainerCalibrator(lambdazation, playerInventory);
	}

	@Override
	public String getGuiID() {
		return ContainerCalibrator.GUI_ID.toString();
	}
}
