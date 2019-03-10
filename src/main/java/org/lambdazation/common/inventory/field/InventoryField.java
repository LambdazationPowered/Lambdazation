package org.lambdazation.common.inventory.field;

import net.minecraft.inventory.IInventory;

public interface InventoryField<T extends IInventory> {
	int localFieldID();

	int getField(T inventory);

	void setField(T inventory, int value);
}
