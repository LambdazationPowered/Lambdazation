package org.lambdazation.common.inventory;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.item.ItemLambdaCrystal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public final class ContainerLens extends Container {
	public static final ResourceLocation GUI_ID = new ResourceLocation("lambdazation:lens");

	public final Lambdazation lambdazation;

	public final InventoryLens inventoryLens = new InventoryLens();

	public ContainerLens(Lambdazation lambdazation, IInventory playerInventory) {
		this.lambdazation = lambdazation;

		addSlot(new SlotLens(inventoryLens, 0, 27, 47));

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
			clearContainer(playerIn, playerIn.world, inventoryLens);
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

	public final class InventoryLens implements IInventory {
		private final NonNullList<ItemStack> inventoryContents;

		public InventoryLens() {
			inventoryContents = NonNullList.withSize(1, ItemStack.EMPTY);
		}

		@Override
		public ITextComponent getName() {
			return new TextComponentString("Lens");
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
		public int getSizeInventory() {
			return 1;
		}

		@Override
		public boolean isEmpty() {
			return inventoryContents.stream().allMatch(ItemStack::isEmpty);
		}

		@Override
		public ItemStack getStackInSlot(int index) {
			return index >= 0 && index < getSizeInventory() ? inventoryContents.get(index) : ItemStack.EMPTY;
		}

		@Override
		public ItemStack decrStackSize(int index, int count) {
			return ItemStackHelper.getAndSplit(inventoryContents, index, count);
		}

		@Override
		public ItemStack removeStackFromSlot(int index) {
			return ItemStackHelper.getAndRemove(inventoryContents, index);
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack) {
			if (index >= 0 && index < getSizeInventory())
				inventoryContents.set(index, stack);

			// FIXME Test code
			ItemLambdaCrystal itemLambdaCrystal = lambdazation.lambdazationItems.itemLambdaCrystal;

			if (stack.getItem().equals(itemLambdaCrystal)) {
				System.out.println("capacity: " + itemLambdaCrystal.getCapacity(stack));
				System.out.println("energy: " + itemLambdaCrystal.getEnergy(stack));
				System.out.println("term: " + itemLambdaCrystal.getTerm(stack));
				System.out.println("termState: " + itemLambdaCrystal.getTermState(stack));
			}
		}

		@Override
		public int getInventoryStackLimit() {
			return 64;
		}

		@Override
		public void markDirty() {

		}

		@Override
		public boolean isUsableByPlayer(EntityPlayer player) {
			return false;
		}

		@Override
		public void openInventory(EntityPlayer player) {

		}

		@Override
		public void closeInventory(EntityPlayer player) {

		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack) {
			return true;
		}

		@Override
		public int getField(int id) {
			return 0;
		}

		@Override
		public void setField(int id, int value) {

		}

		@Override
		public int getFieldCount() {
			return 0;
		}

		@Override
		public void clear() {
			inventoryContents.clear();
		}
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
