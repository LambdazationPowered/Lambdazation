package org.lambdazation.common.inventory;

import java.util.Arrays;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.inventory.field.InventoryField;
import org.lambdazation.common.inventory.field.InventoryFieldCache;
import org.lambdazation.common.inventory.field.InventoryRef;
import org.lambdazation.common.tileentity.TileEntityCrystallizer;
import org.lambdazation.common.utils.GeneralizedEnum;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public final class ContainerCrystallizer extends Container {
	public static final ResourceLocation GUI_ID = new ResourceLocation("lambdazation:crystallizer");

	public final Lambdazation lambdazation;

	public final InventoryPlayer playerInventory;
	public final TileEntityCrystallizer crystallizerInventory;
	public final InventoryFieldCache<ContainerCrystallizer> inventoryFieldCache;

	public ContainerCrystallizer(Lambdazation lambdazation, InventoryPlayer playerInventory,
		TileEntityCrystallizer crystallizerInventory) {
		this.lambdazation = lambdazation;

		this.playerInventory = playerInventory;
		this.crystallizerInventory = crystallizerInventory;

		this.inventoryFieldCache = InventoryFieldCache
			.builder(this, listeners)
			.withInventory(playerInventory)
			.withInventory(crystallizerInventory)
			.build();

		addSlot(new Slot(crystallizerInventory, 0, 27, 47));
		addSlot(new Slot(crystallizerInventory, 1, 76, 47));
		addSlot(new Slot(crystallizerInventory, 2, 134, 47));

		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for (int k = 0; k < 9; ++k)
			addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
	}

	public <T extends IInventory> int lookupInventoryField(InventoryRefCrystallizer<T> inventoryRef, InventoryField<T> inventoryField) {
		return inventoryFieldCache.lookup(inventoryRef, inventoryField);
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);

		Arrays.stream(InventoryRefCrystallizer.values())
			.map(inventoryRef -> inventoryRef.getInventory(this))
			.forEach(inventory -> listener.sendAllWindowProperties(this, inventory));
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		inventoryFieldCache.refresh();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void updateProgressBar(int id, int data) {
		inventoryFieldCache.update(id, data);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return crystallizerInventory.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack affectedStack = ItemStack.EMPTY;

		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack currentStack = slot.getStack();
			affectedStack = currentStack.copy();

			if (index >= 0 && index < 3) {
				if (!this.mergeItemStack(currentStack, 3, 39, false))
					return ItemStack.EMPTY;
			} else if (index >= 3 && index < 39) {
				if (!this.mergeItemStack(currentStack, 0, 3, false))
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

	public static abstract class InventoryRefCrystallizer<T extends IInventory> extends GeneralizedEnum<InventoryRefCrystallizer<?>>
		implements InventoryRef<ContainerCrystallizer, T> {
		public static final InventoryRefCrystallizer<InventoryPlayer> PLAYER = new InventoryRefCrystallizer<InventoryPlayer>(
			"PLAYER", 0) {
			@Override
			public InventoryPlayer getInventory(ContainerCrystallizer container) {
				return container.playerInventory;
			}
		};
		public static final InventoryRefCrystallizer<TileEntityCrystallizer> CRYSTALLIZER = new InventoryRefCrystallizer<TileEntityCrystallizer>(
			"CRYSTALLIZER", 1) {
			@Override
			public TileEntityCrystallizer getInventory(ContainerCrystallizer container) {
				return container.crystallizerInventory;
			}
		};

		private static final InventoryRefCrystallizer<?>[] VALUES = new InventoryRefCrystallizer[] { PLAYER, CRYSTALLIZER };

		InventoryRefCrystallizer(String name, int ordinal) {
			super(name, ordinal);
		}

		@Override
		public int inventoryID() {
			return ordinal();
		}

		public static InventoryRefCrystallizer<?> valueOf(String name) {
			switch (name) {
			case "PLAYER":
				return PLAYER;
			case "CRYSTALLIZER":
				return CRYSTALLIZER;
			default:
				throw new IllegalArgumentException("No enum constant " + name);
			}
		}

		public static InventoryRefCrystallizer<?>[] values() {
			return VALUES;
		}
	}
}
