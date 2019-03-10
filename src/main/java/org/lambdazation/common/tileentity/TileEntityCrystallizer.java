package org.lambdazation.common.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.inventory.ContainerCrystallizer;
import org.lambdazation.common.inventory.field.InventoryField;

public final class TileEntityCrystallizer extends TileEntityLockable implements ISidedInventory, ITickable {
	public final Lambdazation lambdazation;

	public final NonNullList<ItemStack> inventoryContents;
	public int crystallizeTime;

	public TileEntityCrystallizer(Lambdazation lambdazation) {
		super(lambdazation.lambdazationTileEntityTypes.tileEntityTypeCrystallizer);

		this.lambdazation = lambdazation;

		inventoryContents = NonNullList.withSize(3, ItemStack.EMPTY);
		crystallizeTime = 0;
	}

	@Override
	public void read(NBTTagCompound compound) {
		super.read(compound);
		ItemStackHelper.loadAllItems(compound, inventoryContents);
	}

	@Override
	public NBTTagCompound write(NBTTagCompound compound) {
		super.write(compound);
		ItemStackHelper.saveAllItems(compound, inventoryContents);
		return compound;
	}

	@Override
	public int getSizeInventory() {
		return 3;
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
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
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
		if (id < 0 || id >= InventoryFieldCrystallizer.values().length)
			return 0;

		int value = InventoryFieldCrystallizer.values()[id].getField(this);
		return value;
	}

	@Override
	public void setField(int id, int value) {
		if (id < 0 || id >= InventoryFieldCrystallizer.values().length)
			return;

		InventoryFieldCrystallizer.values()[id].setField(this, value);
	}

	@Override
	public int getFieldCount() {
		return InventoryFieldCrystallizer.values().length;
	}

	@Override
	public void clear() {
		inventoryContents.clear();
	}

	@Override
	public ITextComponent getName() {
		return new TextComponentString("Crystallizer");
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
		return new ContainerCrystallizer(lambdazation, playerInventory, this);
	}

	@Override
	public String getGuiID() {
		return ContainerCrystallizer.GUI_ID.toString();
	}

	@Override
	public void tick() {
		// TODO NYI
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		// TODO NYI
		return new int[] {};
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		// TODO NYI
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		// TODO NYI
		return false;
	}

	public enum InventoryFieldCrystallizer implements InventoryField<TileEntityCrystallizer> {
		CRYSTALLIZE_TIME {
			@Override
			public int getField(TileEntityCrystallizer inventory) {
				return inventory.crystallizeTime;
			}

			@Override
			public void setField(TileEntityCrystallizer inventory, int value) {
				inventory.crystallizeTime = value;
			}
		};

		@Override
		public int localFieldID() {
			return ordinal();
		}
	}
}
