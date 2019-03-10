package org.lambdazation.common.inventory.field;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

public interface InventoryRef<C extends Container, T extends IInventory> {
	int inventoryID();

	T getInventory(C container);
}
