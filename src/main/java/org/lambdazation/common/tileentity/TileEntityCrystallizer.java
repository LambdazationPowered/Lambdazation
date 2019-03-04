package org.lambdazation.common.tileentity;

import org.lambdazation.Lambdazation;

import net.minecraft.tileentity.TileEntity;

public final class TileEntityCrystallizer extends TileEntity {
	public final Lambdazation lambdazation;

	public TileEntityCrystallizer(Lambdazation lambdazation) {
		super(lambdazation.lambdazationTileEntityTypes.tileEntityTypeCrystallizer);

		this.lambdazation = lambdazation;
	}
}
