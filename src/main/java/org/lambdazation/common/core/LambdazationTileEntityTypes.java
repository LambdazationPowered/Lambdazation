package org.lambdazation.common.core;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.tileentity.TileEntityCharger;
import org.lambdazation.common.tileentity.TileEntityCrystallizer;
import org.lambdazation.common.tileentity.TileEntityReducer;
import org.lambdazation.common.tileentity.TileEntityTransformer;

import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public final class LambdazationTileEntityTypes {
	public final Lambdazation lambdazation;

	public final TileEntityType<TileEntityCrystallizer> tileEntityTypeCrystallizer;
	public final TileEntityType<TileEntityTransformer> tileEntityTypeTransformer;
	public final TileEntityType<TileEntityCharger> tileEntityTypeCharger;
	public final TileEntityType<TileEntityReducer> tileEntityTypeReducer;

	public LambdazationTileEntityTypes(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

		tileEntityTypeCrystallizer = TileEntityType.Builder
			.create(() -> new TileEntityCrystallizer(lambdazation))
			.build(null);
		tileEntityTypeCrystallizer.setRegistryName(new ResourceLocation("lambdazation:crystallizer"));
		tileEntityTypeTransformer = TileEntityType.Builder
			.create(() -> new TileEntityTransformer(lambdazation))
			.build(null);
		tileEntityTypeTransformer.setRegistryName(new ResourceLocation("lambdazation:transformer"));
		tileEntityTypeCharger = TileEntityType.Builder
			.create(() -> new TileEntityCharger(lambdazation))
			.build(null);
		tileEntityTypeCharger.setRegistryName(new ResourceLocation("lambdazation:charger"));
		tileEntityTypeReducer = TileEntityType.Builder
			.create(() -> new TileEntityReducer(lambdazation))
			.build(null);
		tileEntityTypeReducer.setRegistryName(new ResourceLocation("lambdazation:reducer"));
	}

	public void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> e) {
		e.getRegistry().register(tileEntityTypeCrystallizer);
		e.getRegistry().register(tileEntityTypeTransformer);
		e.getRegistry().register(tileEntityTypeCharger);
		e.getRegistry().register(tileEntityTypeReducer);
	}

	public void finalizeTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> e) {

	}
}
