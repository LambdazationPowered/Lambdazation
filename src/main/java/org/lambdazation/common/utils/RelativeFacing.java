package org.lambdazation.common.utils;

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

	private static Map<EnumFacing, EnumFacing> buildMapping(EnumFacing facing, Rotation rotation) {
		Map<EnumFacing, EnumFacing> mapping = new EnumMap<>(EnumFacing.class);
		for (EnumFacing originalFacing : EnumFacing.values()) {
			EnumFacing rotatedFacing;
			switch (rotation) {
			case NONE:
				rotatedFacing = originalFacing;
				break;
			case CLOCKWISE_90:
				rotatedFacing = originalFacing.rotateAround(EnumFacing.Axis.Z);
				break;
			case CLOCKWISE_180:
				rotatedFacing = originalFacing.rotateAround(EnumFacing.Axis.Z).rotateAround(EnumFacing.Axis.Z);
				break;
			case COUNTERCLOCKWISE_90:
				rotatedFacing = originalFacing.rotateAround(EnumFacing.Axis.Z).rotateAround(EnumFacing.Axis.Z).rotateAround(EnumFacing.Axis.Z);
				break;
			default:
				throw new IllegalStateException();
			}

			EnumFacing resultFacing;
			switch (facing) {
			case DOWN:
				resultFacing = rotatedFacing.rotateAround(EnumFacing.Axis.X).rotateAround(EnumFacing.Axis.X).rotateAround(EnumFacing.Axis.X);
				break;
			case UP:
				resultFacing = rotatedFacing.rotateAround(EnumFacing.Axis.X);
				break;
			case NORTH:
				resultFacing = rotatedFacing.rotateAround(EnumFacing.Axis.Y).rotateAround(EnumFacing.Axis.Y);
				break;
			case SOUTH:
				resultFacing = rotatedFacing;
				break;
			case WEST:
				resultFacing = rotatedFacing.rotateAround(EnumFacing.Axis.Y);
				break;
			case EAST:
				resultFacing = rotatedFacing.rotateAround(EnumFacing.Axis.Y).rotateAround(EnumFacing.Axis.Y).rotateAround(EnumFacing.Axis.Y);
				break;
			default:
				throw new IllegalStateException();
			}

			mapping.put(originalFacing, resultFacing);
		}
		return mapping;
	}
}
