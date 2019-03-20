package org.lambdazation.common.util;

import java.util.EnumMap;
import java.util.Map;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;

public final class RelativeFacing {
	private static final RelativeFacing[][] INDEXED_INSTANCES = ValueBuilder
		.build(new RelativeFacing[EnumFacing.values().length][Rotation.values().length], builder -> {
			for (EnumFacing facing : EnumFacing.values())
			for (Rotation rotation : Rotation.values())
				builder[facing.ordinal()][rotation.ordinal()] = new RelativeFacing(facing, rotation);
		});

	private final EnumFacing facing;
	private final Rotation rotation;
	private final Map<EnumFacing, EnumFacing> mapping;

	private RelativeFacing(EnumFacing facing, Rotation rotation) {
		this.facing = facing;
		this.rotation = rotation;
		this.mapping = buildMapping(facing, rotation);
	}

	public EnumFacing getFacing() {
		return facing;
	}

	public Rotation getRotation() {
		return rotation;
	}

	public EnumFacing transform(EnumFacing originalFacing) {
		return mapping.get(originalFacing);
	}

	public static RelativeFacing of(EnumFacing facing, Rotation rotation) {
		return INDEXED_INSTANCES[facing.ordinal()][rotation.ordinal()];
	}

	private static EnumFacing rotateAround(EnumFacing facing, EnumFacing.Axis axis, Rotation rotation) {
		switch (rotation) {
		case NONE:
			return facing;
		case CLOCKWISE_90:
			return facing.rotateAround(axis);
		case CLOCKWISE_180:
			return facing.rotateAround(axis).rotateAround(axis);
		case COUNTERCLOCKWISE_90:
			return facing.rotateAround(axis).rotateAround(axis).rotateAround(axis);
		default:
			throw new IllegalStateException();
		}
	}

	private static Map<EnumFacing, EnumFacing> buildMapping(EnumFacing facing, Rotation rotation) {
		Map<EnumFacing, EnumFacing> mapping = new EnumMap<>(EnumFacing.class);
		for (EnumFacing originalFacing : EnumFacing.values()) {
			EnumFacing rotatedFacing;
			switch (facing) {
			case DOWN:
				rotatedFacing = rotateAround(originalFacing, EnumFacing.Axis.X, Rotation.COUNTERCLOCKWISE_90);
				break;
			case UP:
				rotatedFacing = rotateAround(originalFacing, EnumFacing.Axis.X, Rotation.CLOCKWISE_90);
				break;
			case NORTH:
				rotatedFacing = rotateAround(originalFacing, EnumFacing.Axis.Y, Rotation.CLOCKWISE_180);
				break;
			case SOUTH:
				rotatedFacing = rotateAround(originalFacing, EnumFacing.Axis.Y, Rotation.NONE);
				break;
			case WEST:
				rotatedFacing = rotateAround(originalFacing, EnumFacing.Axis.Y, Rotation.CLOCKWISE_90);
				break;
			case EAST:
				rotatedFacing = rotateAround(originalFacing, EnumFacing.Axis.Y, Rotation.COUNTERCLOCKWISE_90);
				break;
			default:
				throw new IllegalStateException();
			}

			EnumFacing resultFacing = rotateAround(rotatedFacing, facing.getAxis(), rotation);

			mapping.put(originalFacing, resultFacing);
		}
		return mapping;
	}
}
