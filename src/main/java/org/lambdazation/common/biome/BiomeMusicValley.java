package org.lambdazation.common.biome;

import org.lambdazation.Lambdazation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;

public final class BiomeMusicValley extends Biome {
	public final Lambdazation lambdazation;

	public BiomeMusicValley(Lambdazation lambdazation) {
		super(new BiomeBuilder()
			.surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
			.precipitation(Biome.RainType.RAIN)
			.category(Biome.Category.NONE)
			.depth(0.2F)
			.scale(1.0F)
			.temperature(3.0F)
			.downfall(0.0F)
			.waterColor(0xFF69B4)
			.waterFogColor(0x00FF00)
			.parent(null));

		this.lambdazation = lambdazation;
	}

	public void playSound(EntityPlayer player) {
		if (player.world.getBiome(player.getPosition()) instanceof BiomeMusicValley)
			player.world.playSound(player, player.getPosition(), new SoundEvent(new ResourceLocation("angel")),
				SoundCategory.MUSIC, 3, 7);
	}
}
