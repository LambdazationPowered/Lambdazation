package org.lambdazation.common.inventory.field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lambdazation.common.utils.GeneralizedEnum;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;

public final class InventoryFieldCache<C extends Container> {
	private final C container;
	private final List<IContainerListener> listeners;
	private final Map<IInventory, Entry> entries;

	private InventoryFieldCache(C container, List<IContainerListener> listeners,
		Map<IInventory, Entry> entries) {
		this.container = container;
		this.listeners = listeners;
		this.entries = entries;
	}

	public void refresh() {
		entries.forEach((inventory, entry) -> {
			int fieldCount = entry.getFieldCount();
			for (int localFieldID = 0; localFieldID < fieldCount; localFieldID++) {
				int globalFieldID = entry.globalize(localFieldID);

				int currentValue = inventory.getField(localFieldID);
				int cachedValue = entry.getField(localFieldID);

				if (currentValue != cachedValue) {
					entry.setField(localFieldID, currentValue);
					for (IContainerListener listener : listeners)
						listener.sendWindowProperty(container, globalFieldID, currentValue);
				}
			}
		});
	}

	public <T extends IInventory> int lookup(InventoryRef<C, T> inventoryRef, InventoryField<T> inventoryField) {
		T inventory = inventoryRef.getInventory(container);
		Entry entry = entries.get(inventory);
		if (entry == null)
			return 0;
		int localFieldID = inventoryField.localFieldID();
		if (!entry.valid(localFieldID))
			return 0;

		int cachedValue = entry.getField(localFieldID);
		return cachedValue;
	}

	public void update(int globalFieldID, int value) {
		entries.forEach((inventory, entry) -> {
			if (entry.contains(globalFieldID)) {
				int localFieldID = entry.localize(globalFieldID);

				int currentValue = inventory.getField(localFieldID);
				int cachedValue = entry.getField(localFieldID);

				if (value != currentValue || value != cachedValue) {
					inventory.setField(localFieldID, value);
					entry.setField(localFieldID, value);
					for (IContainerListener listener : listeners)
						listener.sendWindowProperty(container, globalFieldID, currentValue);
				}
			}
		});
	}

	public static <C extends Container, E extends GeneralizedEnum<E> & InventoryRef<C, ?>> Builder<C, E> builder(
		C container, List<IContainerListener> listeners, GeneralizedEnum.Metadata<E> inventoryRefMetadata) {
		return new Builder<>(container, listeners, inventoryRefMetadata);
	}

	public static final class Builder<C extends Container, E extends GeneralizedEnum<E> & InventoryRef<C, ?>> {
		private final C container;
		private final List<IContainerListener> listeners;
		private final GeneralizedEnum.Metadata<E> inventoryRefMetadata;

		private Builder(C container, List<IContainerListener> listeners, GeneralizedEnum.Metadata<E> inventoryRefMetadata) {
			this.container = container;
			this.listeners = listeners;
			this.inventoryRefMetadata = inventoryRefMetadata;
		}

		public InventoryFieldCache<C> build() {
			Map<IInventory, Entry> entries = new HashMap<>();

			int fieldOffset = 0;
			for (E inventoryRef : inventoryRefMetadata.values()) {
				IInventory inventory = inventoryRef.getInventory(container);
				int fieldCount = inventory.getFieldCount();
				int[] fieldCache = new int[fieldCount];
				for (int localFieldID = 0; localFieldID < fieldCount; localFieldID++)
					fieldCache[localFieldID] = inventory.getField(localFieldID);

				entries.put(inventory, new Entry(fieldOffset, fieldCache));
				fieldOffset += fieldCount;
			}

			return new InventoryFieldCache<C>(container, listeners, entries);
		}
	}

	private static final class Entry {
		private final int fieldOffset;
		private final int[] fieldCache;

		public Entry(int fieldOffset, int[] fieldCache) {
			this.fieldOffset = fieldOffset;
			this.fieldCache = fieldCache;
		}

		public int getFieldOffset() {
			return fieldOffset;
		}

		public int getFieldCount() {
			return fieldCache.length;
		}

		public int getField(int localFieldID) {
			return fieldCache[localFieldID];
		}

		public void setField(int localFieldID, int value) {
			fieldCache[localFieldID] = value;
		}

		public int globalize(int localFieldID) {
			return localFieldID + getFieldOffset();
		}

		public int localize(int globalFieldID) {
			return globalFieldID - getFieldOffset();
		}

		public boolean valid(int localFieldID) {
			return localFieldID >= 0 && localFieldID < getFieldCount();
		}

		public boolean contains(int globalFieldID) {
			return globalFieldID >= getFieldOffset() && globalFieldID < getFieldOffset() + getFieldCount();
		}
	}
}
