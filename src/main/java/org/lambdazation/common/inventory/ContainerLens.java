package org.lambdazation.common.inventory;

import org.lambdazation.Lambdazation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

public final class ContainerLens extends Container {
	public static final ResourceLocation GUI_ID = new ResourceLocation("lambdazation:lens");

	public final Lambdazation lambdazation;

	public final IInventory lensInventory = new InventoryBasic(new TextComponentString("Lens"), 1);

	public ContainerLens(Lambdazation lambdazation, IInventory playerInventory) {
		this.lambdazation = lambdazation;

		addSlot(new SlotLens(lensInventory, 0, 27, 47));

		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for (int k = 0; k < 9; ++k)
			addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		if (!playerIn.world.isRemote)
			clearContainer(playerIn, playerIn.world, lensInventory);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack affectedStack = ItemStack.EMPTY;

		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack currentStack = slot.getStack();
			affectedStack = currentStack.copy();

			if (index >= 0 && index < 1) {
				if (!this.mergeItemStack(currentStack, 1, 37, false))
					return ItemStack.EMPTY;
			} else if (index >= 1 && index < 37) {
				if (!this.mergeItemStack(currentStack, 0, 1, false))
					return ItemStack.EMPTY;
			} else
				return ItemStack.EMPTY;

			if (currentStack.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();

			if (currentStack.getCount() == affectedStack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(playerIn, currentStack);
		}

		return affectedStack;
	}

	public final class SlotLens extends Slot {
		public SlotLens(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return stack.getItem().equals(lambdazation.lambdazationItems.itemLambdaCrystal);
		}
	}
}