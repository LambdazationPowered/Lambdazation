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
import org.lambdazation.common.block.BlockReducer;
import org.lambdazation.common.core.LambdazationTermFactory.TermRef;
import org.lambdazation.common.core.LambdazationTermFactory.TermState;
import org.lambdazation.common.core.LambdazationTermFactory.TermAsyncResult;
import org.lambdazation.common.core.LambdazationTermFactory.TermReductionResult;
import org.lambdazation.common.inventory.ContainerReducer;
import org.lambdazation.common.inventory.field.InventoryField;
import org.lambdazation.common.item.ItemLambdaCrystal;
import org.lambdazation.common.state.properties.SlotState;

public final class TileEntityReducer extends TileEntityLockable implements ISidedInventory, ITickable {
	public static final int SLOT_INPUT_0 = 0;
	public static final int SLOT_OUTPUT_1 = 1;

	public static final int[] SLOTS_NONE = new int[] {};
	public static final int[] SLOTS_INPUT = new int[] { SLOT_INPUT_0 };
	public static final int[] SLOTS_OUTPUT = new int[] { SLOT_OUTPUT_1 };
	public static final int[] SLOTS_ALL = new int[] { SLOT_INPUT_0, SLOT_OUTPUT_1 };

	public final Lambdazation lambdazation;

	public final NonNullList<ItemStack> inventoryContents;
	public NonNullList<ItemStack> prevInventoryContents;
	public TermAsyncResult<TermReductionResult> termReductionResult;
	public int aggregateStep;
	public int reduceSpeed;
	public int reduceTime;

	private final LazyOptional<? extends IItemHandler>[] itemHandlers = SidedInvWrapper.create(this, EnumFacing.DOWN,
		EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST);

	public TileEntityReducer(Lambdazation lambdazation) {
		super(lambdazation.lambdazationTileEntityTypes.tileEntityTypeReducer);

		this.lambdazation = lambdazation;

		this.inventoryContents = NonNullList.withSize(3, ItemStack.EMPTY);
		this.prevInventoryContents = null;
		this.termReductionResult = null;
		this.aggregateStep = 256;
		this.reduceSpeed = 1;
		this.reduceTime = 0;
	}

	@Override
	public void read(NBTTagCompound compound) {
		super.read(compound);

		ItemStackHelper.loadAllItems(compound, inventoryContents);
		aggregateStep = compound.getInt("aggregateStep");
		reduceSpeed = compound.getInt("reduceSpeed");
		reduceTime = compound.getInt("reduceTime");
	}

	@Override
	public NBTTagCompound write(NBTTagCompound compound) {
		super.write(compound);

		ItemStackHelper.saveAllItems(compound, inventoryContents);
		compound.setInt("aggregateStep", aggregateStep);
		compound.setInt("reduceSpeed", reduceSpeed);
		compound.setInt("reduceTime", reduceTime);

		return compound;
	}

	@Override
	public void remove() {
		super.remove();
		discard();
		Arrays.stream(itemHandlers).forEach(LazyOptional::invalidate);
	}

	@Override
	public int getSizeInventory() {
		return 2;
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
		case SLOT_OUTPUT_1:
			return false;
		default:
			return false;
		}
	}

	@Override
	public int getField(int id) {
		if (id < 0 || id >= InventoryFieldReducer.values().length)
			return 0;

		int value = InventoryFieldReducer.values()[id].getField(this);
		return value;
	}

	@Override
	public void setField(int id, int value) {
		if (id < 0 || id >= InventoryFieldReducer.values().length)
			return;

		InventoryFieldReducer.values()[id].setField(this, value);
	}

	@Override
	public int getFieldCount() {
		return InventoryFieldReducer.values().length;
	}

	@Override
	public void clear() {
		inventoryContents.clear();
	}

	@Override
	public ITextComponent getName() {
		return new TextComponentString("Reducer");
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
		return new ContainerReducer(lambdazation, playerInventory, this);
	}

	@Override
	public String getGuiID() {
		return ContainerReducer.GUI_ID.toString();
	}

	@Override
	public void tick() {
		if (changed())
			update();
		if (canAdvance())
			advance();
		if (completed())
			reduced();
	}

	private void discard() {
		if (termReductionResult != null) {
			termReductionResult.discard();
			termReductionResult = null;
		}
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

		ItemStack inputItemStack = inventoryContents.get(SLOT_INPUT_0);
		ItemStack outputItemStack = inventoryContents.get(SLOT_OUTPUT_1);

		if (!inputItemStack.isEmpty() && inputItemStack.getItem().equals(itemLambdaCrystal)
			&& outputItemStack.isEmpty()) {
			TermRef termRef = itemLambdaCrystal.getTermRef(inputItemStack).orElse(null);
			int energy = itemLambdaCrystal.getEnergy(inputItemStack).orElse(0);
			int capacity = itemLambdaCrystal.getCapacity(inputItemStack).orElse(0);
			if (termRef != null && energy > 0) {
				int maxStep = Math.min(aggregateStep, energy);
				int maxSize = capacity;

				discard();
				termReductionResult = lambdazation.lambdazationTermFactory.termAsyncFactory.reduceTerm(termRef, maxStep,
					maxSize, world.getServer().getTickCounter());
				if (reduceTime <= 0)
					reduceTime = aggregateStep;
			} else {
				discard();
				reduceTime = 0;
			}
		} else {
			discard();
			reduceTime = 0;
		}
	}

	private boolean canAdvance() {
		return termReductionResult != null && reduceTime > 0;
	}

	private void advance() {
		reduceTime -= Math.min(reduceTime, reduceSpeed);

		markDirty();
	}

	private boolean completed() {
		return termReductionResult != null && reduceTime <= 0;
	}

	private void reduced() {
		if (world.isRemote)
			return;

		ItemLambdaCrystal itemLambdaCrystal = lambdazation.lambdazationItems.itemLambdaCrystal;

		ItemStack inputItemStack = inventoryContents.get(SLOT_INPUT_0);

		TermReductionResult result = termReductionResult.get().orElse(null);
		int energy = itemLambdaCrystal.getEnergy(inputItemStack).orElse(0);

		ItemStack resultItemStack = inputItemStack.copy();
		if (result != null) {
			itemLambdaCrystal.setEnergy(resultItemStack, energy - result.step);
			itemLambdaCrystal.setTermRef(resultItemStack, result.termRef);
		}

		if (result != null && result.termRef.termState.equals(TermState.BETA_ETA_NORMAL_FORM)) {
			inventoryContents.set(SLOT_INPUT_0, ItemStack.EMPTY);
			inventoryContents.set(SLOT_OUTPUT_1, resultItemStack);
		} else {
			inventoryContents.set(SLOT_INPUT_0, resultItemStack);
		}

		discard();

		markDirty();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		SlotState slotState = getBlockState().get(BlockReducer.FACING_PROPERTY_MAP.get(side));
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

	public enum InventoryFieldReducer implements InventoryField<TileEntityReducer> {
		AGGREGATE_STEP {
			@Override
			public int getField(TileEntityReducer inventory) {
				return inventory.aggregateStep;
			}

			@Override
			public void setField(TileEntityReducer inventory, int value) {
				inventory.aggregateStep = value;
			}
		},
		REDUCE_SPEED {
			@Override
			public int getField(TileEntityReducer inventory) {
				return inventory.reduceSpeed;
			}

			@Override
			public void setField(TileEntityReducer inventory, int value) {
				inventory.reduceSpeed = value;
			}
		},
		REDUCE_TIME {
			@Override
			public int getField(TileEntityReducer inventory) {
				return inventory.reduceTime;
			}

			@Override
			public void setField(TileEntityReducer inventory, int value) {
				inventory.reduceTime = value;
			}
		};

		@Override
		public int localFieldID() {
			return ordinal();
		}
	}
}
