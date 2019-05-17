package org.lambdazation.common.core;

import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import org.lambdazation.Lambdazation;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.feature.ReplaceBlockConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraftforge.event.RegistryEvent;
import org.lambdazation.common.biome.BiomeMusicValley;

public final class LambdazationBiomes {
	public final Lambdazation lambdazation;

	public final BiomeMusicValley biomeMusicValley;
	public final SurfaceBuilderConfig lambda_grass;

	public LambdazationBiomes(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

		biomeMusicValley = new BiomeMusicValley(lambdazation);
		biomeMusicValley.setRegistryName(new ResourceLocation("lambdazation:music_valley"));
		lambda_grass=new SurfaceBuilderConfig(
			lambdazation.lambdazationBlocks.blockLambdaGrass.getDefaultState(),
			lambdazation.lambdazationBlocks.blockLambdaGrass.getDefaultState(),
			lambdazation.lambdazationBlocks.blockLambdaGrass.getDefaultState());
	}

	public void registerBiomes(RegistryEvent.Register<Biome> e) {
		e.getRegistry().register(biomeMusicValley);
	}

	public void finalizeBiomes(RegistryEvent.Register<Biome> e) {
		e.getRegistry().forEach(biome -> biome.addFeature(
			GenerationStage.Decoration.UNDERGROUND_ORES,
			Biome.createCompositeFeature(
				Feature.REPLACE_BLOCK,
				new ReplaceBlockConfig(MinableConfig.IS_ROCK, lambdazation.lambdazationBlocks.blockLambdaOre.getDefaultState()),
				Biome.COUNT_RANGE,
				new CountRangeConfig(1, 0, 0, 256))));
	}
}
