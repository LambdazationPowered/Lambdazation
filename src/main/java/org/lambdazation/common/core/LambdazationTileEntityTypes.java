package org.lambdazation.common.core;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.tileentity.TileEntityCrystallizer;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public final class LambdazationTileEntityTypes {
	public final Lambdazation lambdazation;

	public final TileEntityType<TileEntityCrystallizer> tileEntityTypeCrystallizer;

	public LambdazationTileEntityTypes(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

		tileEntityTypeCrystallizer = TileEntityType.Builder
			.create(() -> new TileEntityCrystallizer(lambdazation))
			.build(null);
		tileEntityTypeCrystallizer.setRegistryName(new ResourceLocation("lambdazation:crystallizer"));
	}

	public void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> e) {
		e.getRegistry().register(tileEntityTypeCrystallizer);
	}

	public void finalizeTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> e) {

	}
}
