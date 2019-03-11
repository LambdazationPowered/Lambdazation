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
import org.lambdazation.common.item.ItemLambdaCrystal;

public final class TileEntityCrystallizer extends TileEntityLockable implements ISidedInventory, ITickable {
	public final Lambdazation lambdazation;

	public final NonNullList<ItemStack> inventoryContents;
	public NonNullList<ItemStack> prevInventoryContents;
	public int crystallizeTime;

	public TileEntityCrystallizer(Lambdazation lambdazation) {
		super(lambdazation.lambdazationTileEntityTypes.tileEntityTypeCrystallizer);

		this.lambdazation = lambdazation;

		inventoryContents = NonNullList.withSize(3, ItemStack.EMPTY);
		prevInventoryContents = null;
		crystallizeTime = 0;
	}

	@Override
	public void read(NBTTagCompound compound) {
		super.read(compound);

		ItemStackHelper.loadAllItems(compound, inventoryContents);
		crystallizeTime = compound.getInt("crystallizeTime");
	}

	@Override
	public NBTTagCompound write(NBTTagCompound compound) {
		super.write(compound);

		ItemStackHelper.saveAllItems(compound, inventoryContents);
		compound.setInt("crystallizeTime", crystallizeTime);

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
		if (changed())
			update();

		if (crystallizeTime > 0) {
			crystallizeTime--;
			if (crystallizeTime == 0)
				crystallized();
		}
	}

	private boolean changed() {
		if (prevInventoryContents == null)
			return true;
		if (inventoryContents.size() != prevInventoryContents.size())
			return true;

		for (int i = 0; i < inventoryContents.size(); i++) {
			ItemStack currentItemStack = inventoryContents.get(i);
			ItemStack prevItemStack = prevInventoryContents.get(i);
			if (!ItemStack.areItemStacksEqual(currentItemStack, prevItemStack))
				return true;
		}

		return false;
	}

	private void cache() {
		prevInventoryContents = NonNullList.from(ItemStack.EMPTY, inventoryContents.stream().map(ItemStack::copy).toArray(ItemStack[]::new));
	}

	private void update() {
		cache();

		ItemStack resultItemStack = inventoryContents.get(2);
		if (resultItemStack.isEmpty()) {
			ItemStack firstItemStack = inventoryContents.get(0);
			ItemStack secondItemStack = inventoryContents.get(1);
			if (!lambdazation.lambdazationItems.itemLambdaCrystal.isAlphaEquivalent(firstItemStack, secondItemStack))
				crystallizeTime = 0;
			else
				crystallizeTime = 20 * 10;
		} else
			crystallizeTime = 0;

		markDirty();
	}

	private void crystallized() {
		ItemLambdaCrystal itemLambdaCrystal = lambdazation.lambdazationItems.itemLambdaCrystal;

		ItemStack firstItemStack = inventoryContents.get(0);
		ItemStack secondItemStack = inventoryContents.get(1);

		int capacity = itemLambdaCrystal.getCapacity(firstItemStack).orElse(0)
			+ itemLambdaCrystal.getCapacity(secondItemStack).orElse(0);
		int energy = itemLambdaCrystal.getEnergy(firstItemStack).orElse(0)
			+ itemLambdaCrystal.getEnergy(secondItemStack).orElse(0);

		ItemStack resultItemStack = firstItemStack.copy();
		itemLambdaCrystal.setCapacity(resultItemStack, capacity);
		itemLambdaCrystal.setEnergy(resultItemStack, energy);

		inventoryContents.set(0, ItemStack.EMPTY);
		inventoryContents.set(1, ItemStack.EMPTY);
		inventoryContents.set(2, resultItemStack);

		cache();
		markDirty();
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
