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
import org.lambdazation.common.block.BlockTransformer;
import org.lambdazation.common.inventory.ContainerTransformer;
import org.lambdazation.common.inventory.field.InventoryField;
import org.lambdazation.common.item.ItemLambdaCrystal;
import org.lambdazation.common.item.ItemLambdaCrystal.TermState;
import org.lambdazation.common.state.properties.SlotState;
import org.lamcalcj.ast.Lambda.App;
import org.lamcalcj.ast.Lambda.Term;

public final class TileEntityTransformer extends TileEntityLockable implements ISidedInventory, ITickable {
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
	public int transformTime;

	private final LazyOptional<? extends IItemHandler>[] itemHandlers = SidedInvWrapper.create(this, EnumFacing.DOWN,
		EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.EAST);

	public TileEntityTransformer(Lambdazation lambdazation) {
		super(lambdazation.lambdazationTileEntityTypes.tileEntityTypeTransformer);

		this.lambdazation = lambdazation;

		this.inventoryContents = NonNullList.withSize(3, ItemStack.EMPTY);
		this.prevInventoryContents = null;
		this.transformTime = 0;
	}

	@Override
	public void read(NBTTagCompound compound) {
		super.read(compound);

		ItemStackHelper.loadAllItems(compound, inventoryContents);
		transformTime = compound.getInt("transformTime");
	}

	@Override
	public NBTTagCompound write(NBTTagCompound compound) {
		super.write(compound);

		ItemStackHelper.saveAllItems(compound, inventoryContents);
		compound.setInt("transformTime", transformTime);

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
		if (id < 0 || id >= InventoryFieldTransformer.values().length)
			return 0;

		int value = InventoryFieldTransformer.values()[id].getField(this);
		return value;
	}

	@Override
	public void setField(int id, int value) {
		if (id < 0 || id >= InventoryFieldTransformer.values().length)
			return;

		InventoryFieldTransformer.values()[id].setField(this, value);
	}

	@Override
	public int getFieldCount() {
		return InventoryFieldTransformer.values().length;
	}

	@Override
	public void clear() {
		inventoryContents.clear();
	}

	@Override
	public ITextComponent getName() {
		return new TextComponentString("Transformer");
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
		return new ContainerTransformer(lambdazation, playerInventory, this);
	}

	@Override
	public String getGuiID() {
		return ContainerTransformer.GUI_ID.toString();
	}

	@Override
	public void tick() {
		if (changed())
			update();

		if (transformTime > 0 && advance())
			transformed();
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

		ItemStack functionItemStack = inventoryContents.get(SLOT_INPUT_0);
		ItemStack argumentItemStack = inventoryContents.get(SLOT_INPUT_1);
		ItemStack resultItemStack = inventoryContents.get(SLOT_OUTPUT_2);

		if (!functionItemStack.isEmpty() && functionItemStack.getItem().equals(itemLambdaCrystal)
			&& !argumentItemStack.isEmpty() && argumentItemStack.getItem().equals(itemLambdaCrystal)
			&& resultItemStack.isEmpty()) {
			int functionTermSize = itemLambdaCrystal.getTermSize(functionItemStack).orElse(0);
			int argumentEnergy = itemLambdaCrystal.getEnergy(argumentItemStack).orElse(0);

			if (argumentEnergy < functionTermSize)
				transformTime = 0;
			else
				transformTime = 20 * 10;
		} else
			transformTime = 0;

		markDirty();
	}

	private boolean advance() {
		transformTime--;

		markDirty();

		return transformTime <= 0;
	}
	private void transformed() {
		if (world.isRemote)
			return;

		ItemLambdaCrystal itemLambdaCrystal = lambdazation.lambdazationItems.itemLambdaCrystal;

		ItemStack functionItemStack = inventoryContents.get(SLOT_INPUT_0);
		ItemStack argumentItemStack = inventoryContents.get(SLOT_INPUT_1);

		Term functionTerm = itemLambdaCrystal.getTerm(functionItemStack).orElse(lambdazation.lambdazationTermFactory.predefTermId.term);
		int functionTermSize = itemLambdaCrystal.getTermSize(functionItemStack).orElse(0);
		int argumentCapacity = itemLambdaCrystal.getCapacity(argumentItemStack).orElse(0);
		int argumentEnergy = itemLambdaCrystal.getEnergy(argumentItemStack).orElse(0);
		Term argumentTerm = itemLambdaCrystal.getTerm(argumentItemStack).orElse(lambdazation.lambdazationTermFactory.predefTermFix.applyTerm(functionTerm));

		int capacity = argumentCapacity;
		int energy = argumentEnergy - functionTermSize;
		Term term = new App(functionTerm, argumentTerm);
		TermState termState = TermState.REDUCIBLE_FORM;
		int termSize = term.size();
		int termDepth = term.depth();

		ItemStack resultItemStack = itemLambdaCrystal.builder()
			.capacity(capacity)
			.energy(energy)
			.term(term)
			.termState(termState)
			.termSize(termSize)
			.termDepth(termDepth)
			.build();
		argumentItemStack.shrink(1);

		inventoryContents.set(SLOT_INPUT_1, argumentItemStack.isEmpty() ? ItemStack.EMPTY : argumentItemStack);
		inventoryContents.set(SLOT_OUTPUT_2, resultItemStack);

		cache();
		markDirty();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		SlotState slotState = getBlockState().get(BlockTransformer.FACING_PROPERTY_MAP.get(side));
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

	public enum InventoryFieldTransformer implements InventoryField<TileEntityTransformer> {
		TRANSFORM_TIME {
			@Override
			public int getField(TileEntityTransformer inventory) {
				return inventory.transformTime;
			}

			@Override
			public void setField(TileEntityTransformer inventory, int value) {
				inventory.transformTime = value;
			}
		};

		@Override
		public int localFieldID() {
			return ordinal();
		}
	}
}
