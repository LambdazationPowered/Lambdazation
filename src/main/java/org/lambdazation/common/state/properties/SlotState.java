package org.lambdazation.common.state.properties;

import net.minecraft.state.EnumProperty;
import net.minecraft.util.IStringSerializable;

public enum SlotState implements IStringSerializable {
	NONE("none"),
	INPUT("input"),
	OUTPUT("output"),
	ALL("all");

	public static final EnumProperty<SlotState> SLOT_STATE_DOWN = EnumProperty.create("down", SlotState.class);
	public static final EnumProperty<SlotState> SLOT_STATE_UP = EnumProperty.create("up", SlotState.class);
	public static final EnumProperty<SlotState> SLOT_STATE_NORTH = EnumProperty.create("north", SlotState.class);
	public static final EnumProperty<SlotState> SLOT_STATE_SOUTH = EnumProperty.create("south", SlotState.class);
	public static final EnumProperty<SlotState> SLOT_STATE_WEST = EnumProperty.create("west", SlotState.class);
	public static final EnumProperty<SlotState> SLOT_STATE_EAST = EnumProperty.create("east", SlotState.class);

	private final String name;

	private SlotState(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}
}
