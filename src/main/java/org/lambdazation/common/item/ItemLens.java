package org.lambdazation.common.item;

import java.util.UUID;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.inventory.ContainerLens;
import org.lambdazation.common.network.message.MessagePing;
import org.lambdazation.common.network.message.MessageTest;
import org.lambdazation.common.util.data.Unit;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

public final class ItemLens extends Item implements IInteractionObject {
	public final Lambdazation lambdazation;

	public ItemLens(Lambdazation lambdazation, Properties properties) {
		super(properties);

		this.lambdazation = lambdazation;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.swingArm(handIn);

		if (worldIn.isRemote)
			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));

		if (!(playerIn instanceof EntityPlayerMP))
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
		EntityPlayerMP entityPlayerMP = (EntityPlayerMP) playerIn;

		NetworkHooks.openGui(entityPlayerMP, this);

		// FIXME Testcode
		lambdazation.lambdazationNetwork.networkHandler.sendMessage(
			PacketDistributor.PLAYER.with(() -> entityPlayerMP),
			new MessagePing());
		MessageTest msg = lambdazation.lambdazationNetwork.networkHandler.handlerTest
			.builder()
			.with(MessageTest.FieldTest.P0, BlockPos.ORIGIN)
			.with(MessageTest.FieldTest.P3, new byte[0])
			.with(MessageTest.FieldTest.P5, new NBTTagCompound())
			.with(MessageTest.FieldTest.P7, Unit.UNIT)
			.with(MessageTest.FieldTest.P10, new ItemStack(Blocks.STONE))
			.with(MessageTest.FieldTest.P12, new ResourceLocation("lambdazation", "network"))
			.with(MessageTest.FieldTest.P14, "lambdazation")
			.with(MessageTest.FieldTest.P15, new TextComponentString("lambdazation"))
			.with(MessageTest.FieldTest.P16, new UUID(0L, 0L))
			.build();
		lambdazation.lambdazationNetwork.networkHandler.sendMessage(
			PacketDistributor.PLAYER.with(() -> entityPlayerMP),
			msg);

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
