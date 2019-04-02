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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import java.util.Arrays;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.block.BlockCrystallizer;
import org.lambdazation.common.inventory.ContainerCrystallizer;
import org.lambdazation.common.inventory.field.InventoryField;
import org.lambdazation.common.item.ItemLambdaCrystal;
import org.lambdazation.common.state.properties.SlotState;

public final class TileEntityCrystallizer extends TileEntityLockable implements ISidedInventory, ITickable {
	public static final int SLOT_INPUT_0 = 0;
	public static final int SLOT_INPUT_1 = 1;
	public static final int SLOT_OUTPUT_2 = 2;

	public static final int[] SLOTS_NONE = new int[] {};
	public static final int[] SLOTS_INPUT = new int[] { SLOT_INPUT_0, SLOT_INPUT_1 };
	public static final int[] SLOTS_OUTPUT = new int[] { SLOT_OUTPUT_2 };
	public static final int[] SLOTS_ALL = new int[] { SLOT_INPUT_0, SLOT_INPUT_1, SLOT_OUTPUT_2 };

	public final Lambdazation lambdazation;

	public final NonNullList<ItemStack> inventoryContents;
	public NonNullList<ItemStack> prevInventoryContents;
	public boolean crystallizing;
	public int totalTime;
	public int crystallizeTime;

	private final LazyOptional<? extends IItemHandler>[] itemHandlers = SidedInvWrapper.create(this, EnumFacing.DOWN,
		EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST);

	public TileEntityCrystallizer(Lambdazation lambdazation) {
		super(lambdazation.lambdazationTileEntityTypes.tileEntityTypeCrystallizer);

		this.lambdazation = lambdazation;

		this.inventoryContents = NonNullList.withSize(3, ItemStack.EMPTY);
		this.prevInventoryContents = null;
		this.crystallizing = false;
		this.totalTime = 256;
		this.crystallizeTime = 0;
	}

	@Override
	public void read(NBTTagCompound compound) {
		super.read(compound);

		ItemStackHelper.loadAllItems(compound, inventoryContents);
		totalTime = compound.getInt("totalTime");
		crystallizeTime = compound.getInt("crystallizeTime");
	}

	@Override
	public NBTTagCompound write(NBTTagCompound compound) {
		super.write(compound);

		ItemStackHelper.saveAllItems(compound, inventoryContents);
		compound.setInt("totalTime", totalTime);
		compound.setInt("crystallizeTime", crystallizeTime);

		return compound;
	}

	@Override
	public void remove() {
		super.remove();
		Arrays.stream(itemHandlers).forEach(LazyOptional::invalidate);
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
		switch (index) {
		case SLOT_INPUT_0:
			return stack.getItem().equals(lambdazation.lambdazationItems.itemLambdaCrystal);
		case SLOT_INPUT_1:
			return stack.getItem().equals(lambdazation.lambdazationItems.itemLambdaCrystal);
		case SLOT_OUTPUT_2:
			return false;
		default:
			return false;
		}
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
		if (canAdvance())
			advance();
		if (completed())
			crystallized();
	}

	private void cache() {
		prevInventoryContents = NonNullList.from(ItemStack.EMPTY,
			inventoryContents.stream().map(ItemStack::copy).toArray(ItemStack[]::new));
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

	private void update() {
		if (world.isRemote)
			return;

		cache();

		ItemLambdaCrystal itemLambdaCrystal = lambdazation.lambdazationItems.itemLambdaCrystal;

		ItemStack firstItemStack = inventoryContents.get(SLOT_INPUT_0);
		ItemStack secondItemStack = inventoryContents.get(SLOT_INPUT_1);
		ItemStack resultItemStack = inventoryContents.get(SLOT_OUTPUT_2);

		if (!firstItemStack.isEmpty() && firstItemStack.getItem().equals(itemLambdaCrystal)
			&& !secondItemStack.isEmpty() && secondItemStack.getItem().equals(itemLambdaCrystal)
			&& resultItemStack.isEmpty()) {
			if (lambdazation.lambdazationItems.itemLambdaCrystal.isAlphaEquivalent(firstItemStack, secondItemStack)) {
				crystallizing = true;
				if (crystallizeTime <= 0)
					crystallizeTime = totalTime;
			} else {
				crystallizing = false;
				crystallizeTime = 0;
			}
		} else {
			crystallizing = false;
			crystallizeTime = 0;
		}

		markDirty();
	}

	private boolean canAdvance() {
		return crystallizing && crystallizeTime > 0;
	}

	private void advance() {
		crystallizeTime--;

		markDirty();
	}

	private boolean completed() {
		return crystallizing && crystallizeTime <= 0;
	}

	private void crystallized() {
		if (world.isRemote)
			return;

		ItemLambdaCrystal itemLambdaCrystal = lambdazation.lambdazationItems.itemLambdaCrystal;

		ItemStack firstItemStack = inventoryContents.get(SLOT_INPUT_0);
		ItemStack secondItemStack = inventoryContents.get(SLOT_INPUT_1);

		int capacity = itemLambdaCrystal.getCapacity(firstItemStack).orElse(0)
			+ itemLambdaCrystal.getCapacity(secondItemStack).orElse(0);
		int energy = itemLambdaCrystal.getEnergy(firstItemStack).orElse(0)
			+ itemLambdaCrystal.getEnergy(secondItemStack).orElse(0);

		ItemStack resultItemStack = firstItemStack.copy();
		itemLambdaCrystal.setCapacity(resultItemStack, capacity);
		itemLambdaCrystal.setEnergy(resultItemStack, energy);
		firstItemStack.shrink(1);
		secondItemStack.shrink(1);

		inventoryContents.set(SLOT_INPUT_0, firstItemStack.isEmpty() ? ItemStack.EMPTY : firstItemStack);
		inventoryContents.set(SLOT_INPUT_1, secondItemStack.isEmpty() ? ItemStack.EMPTY : secondItemStack);
		inventoryContents.set(SLOT_OUTPUT_2, resultItemStack);

		crystallizing = false;

		markDirty();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		SlotState slotState = getBlockState().get(BlockCrystallizer.FACING_PROPERTY_MAP.get(side));
		switch (slotState) {
		case NONE:
			return SLOTS_NONE;
		case INPUT:
			return SLOTS_INPUT;
		case OUTPUT:
			return SLOTS_OUTPUT;
		case ALL:
			return SLOTS_ALL;
		default:
			throw new IllegalStateException();
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, EnumFacing side) {
		if (!removed && side != null && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			switch (side) {
			case DOWN:
				return itemHandlers[0].cast();
			case UP:
				return itemHandlers[1].cast();
			case NORTH:
				return itemHandlers[2].cast();
			case SOUTH:
				return itemHandlers[3].cast();
			case WEST:
				return itemHandlers[4].cast();
			case EAST:
				return itemHandlers[5].cast();
			default:
				throw new IllegalStateException();
			}
		}

		return super.getCapability(cap, side);
	}

	public enum InventoryFieldCrystallizer implements InventoryField<TileEntityCrystallizer> {
		TOTAL_TIME {
			@Override
			public int getField(TileEntityCrystallizer inventory) {
				return inventory.totalTime;
			}

			@Override
			public void setField(TileEntityCrystallizer inventory, int value) {
				inventory.totalTime = value;
			}
		},
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
