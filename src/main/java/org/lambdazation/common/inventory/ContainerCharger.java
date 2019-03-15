package org.lambdazation.common.inventory;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.inventory.field.InventoryField;
import org.lambdazation.common.inventory.field.InventoryFieldCache;
import org.lambdazation.common.inventory.field.InventoryRef;
import org.lambdazation.common.tileentity.TileEntityCharger;
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

public final class ContainerCharger extends Container {
	public static final ResourceLocation GUI_ID = new ResourceLocation("lambdazation:charger");

	public final Lambdazation lambdazation;

	public final InventoryPlayer playerInventory;
	public final TileEntityCharger chargerInventory;
	public final InventoryFieldCache<ContainerCharger> inventoryFieldCache;

	public ContainerCharger(Lambdazation lambdazation, InventoryPlayer playerInventory,
		TileEntityCharger chargerInventory) {
		this.lambdazation = lambdazation;

		this.playerInventory = playerInventory;
		this.chargerInventory = chargerInventory;

		this.inventoryFieldCache = InventoryFieldCache
			.builder(this, listeners, InventoryRefCharger.METADATA)
			.build();

		addSlot(new SlotInput(chargerInventory, 0, 27, 47));
		addSlot(new SlotFuel(chargerInventory, 1, 76, 47));
		addSlot(new SlotOutput(chargerInventory, 2, 134, 47));

		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));

		for (int k = 0; k < 9; ++k)
			addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
	}

	public <T extends IInventory> int lookupInventoryField(InventoryRefCharger<T> inventoryRef, InventoryField<T> inventoryField) {
		return inventoryFieldCache.lookup(inventoryRef, inventoryField);
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);

		InventoryRefCharger.METADATA.values()
			.map(InventoryRef -> InventoryRef.getInventory(this))
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
		return chargerInventory.isUsableByPlayer(playerIn);
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

	public final class SlotInput extends Slot {
		public SlotInput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return inventory.isItemValidForSlot(getSlotIndex(), stack);
		}
	}

	public final class SlotFuel extends Slot {
		public SlotFuel(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return inventory.isItemValidForSlot(getSlotIndex(), stack);
		}
	}

	public final class SlotOutput extends Slot {
		public SlotOutput(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}

		@Override
		public boolean isItemValid(ItemStack stack) {
			return inventory.isItemValidForSlot(getSlotIndex(), stack);
		}
	}

	public static abstract class InventoryRefCharger<T extends IInventory> extends GeneralizedEnum<InventoryRefCharger<?>>
		implements InventoryRef<ContainerCharger, T> {
		public static final InventoryRefCharger<InventoryPlayer> PLAYER;
		public static final InventoryRefCharger<TileEntityCharger> CHARGER;

		public static final GeneralizedEnum.Metadata<InventoryRefCharger<?>> METADATA;

		static {
			GeneralizedEnum.Metadata.Builder<InventoryRefCharger<?>> builder = GeneralizedEnum.Metadata.builder();

			class Player extends InventoryRefCharger<InventoryPlayer> {
				Player(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public InventoryPlayer getInventory(ContainerCharger container) {
					return container.playerInventory;
				}
			}
			PLAYER = builder.withValue("PLAYER", Player::new);

			class Charger extends InventoryRefCharger<TileEntityCharger> {
				Charger(String name, int ordinal) {
					super(name, ordinal);
				}

				@Override
				public TileEntityCharger getInventory(ContainerCharger container) {
					return container.chargerInventory;
				}
			}
			CHARGER = builder.withValue("CHARGER", Charger::new);

			METADATA = builder.build();
		}

		InventoryRefCharger(String name, int ordinal) {
			super(name, ordinal);
		}

		@Override
		public int inventoryID() {
			return ordinal();
		}
	}
}
