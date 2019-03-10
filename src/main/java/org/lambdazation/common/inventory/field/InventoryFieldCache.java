package org.lambdazation.common.inventory.field;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public static <C extends Container> Builder<C> builder(C container, List<IContainerListener> listeners) {
		return new Builder<C>(container, listeners);
	}

	public static final class Builder<C extends Container> {
		private final C container;
		private final List<IContainerListener> listeners;
		private final Map<IInventory, Entry> entries;
		private int fieldOffset;

		private Builder(C container, List<IContainerListener> listeners) {
			this.container = container;
			this.listeners = listeners;
			this.entries = new HashMap<>();
			this.fieldOffset = 0;
		}

		public Builder<C> withInventory(IInventory inventory) {
			int fieldCount = inventory.getFieldCount();
			int[] fieldCache = new int[fieldCount];
			for (int localFieldID = 0; localFieldID < fieldCount; localFieldID++)
				fieldCache[localFieldID] = inventory.getField(localFieldID);

			entries.put(inventory, new Entry(fieldOffset, fieldCache));
			fieldOffset += fieldCount;

			return this;
		}

		public InventoryFieldCache<C> build() {
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
