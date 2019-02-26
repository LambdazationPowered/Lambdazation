package org.lambdazation.common.core;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.block.BlockLambdaOre;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public final class LambdazationBlocks {
	public final Lambdazation lambdazation;

	public final BlockLambdaOre blockLambdaOre;

	public LambdazationBlocks(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

		blockLambdaOre = new BlockLambdaOre(lambdazation, Block.Properties.create(Material.ROCK)
			.hardnessAndResistance(3.0F, 3.0F));
		blockLambdaOre.setRegistryName(new ResourceLocation("lambdazation:lambda_ore"));
	}

	public void registerBlocks(RegistryEvent.Register<Block> registry) {
		registry.getRegistry().register(blockLambdaOre);
	}
}
