package org.lambdazation.common.core;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.block.BlockCharger;
import org.lambdazation.common.block.BlockCrystallizer;
import org.lambdazation.common.block.BlockLambdaOre;
import org.lambdazation.common.block.BlockReducer;
import org.lambdazation.common.block.BlockTransformer;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

public final class LambdazationBlocks {
	public final Lambdazation lambdazation;

	public final BlockLambdaOre blockLambdaOre;
	public final BlockCrystallizer blockCrystallizer;
	public final BlockTransformer blockTransformer;
	public final BlockCharger blockCharger;
	public final BlockReducer blockReducer;

	public LambdazationBlocks(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

		blockLambdaOre = new BlockLambdaOre(lambdazation, Block.Properties
			.create(Material.ROCK)
			.hardnessAndResistance(3.0F, 3.0F));
		blockLambdaOre.setRegistryName(new ResourceLocation("lambdazation:lambda_ore"));
		blockCrystallizer = new BlockCrystallizer(lambdazation, Block.Properties
			.create(Material.ROCK)
			.hardnessAndResistance(3.5F, 3.5F));
		blockCrystallizer.setRegistryName(new ResourceLocation("lambdazation:crystallizer"));
		blockTransformer = new BlockTransformer(lambdazation, Block.Properties
			.create(Material.ROCK)
			.hardnessAndResistance(3.5F, 3.5F));
		blockTransformer.setRegistryName(new ResourceLocation("lambdazation:transformer"));
		blockCharger = new BlockCharger(lambdazation, Block.Properties
			.create(Material.ROCK)
			.hardnessAndResistance(3.5F, 3.5F));
		blockCharger.setRegistryName(new ResourceLocation("lambdazation:charger"));
		blockReducer = new BlockReducer(lambdazation, Block.Properties
			.create(Material.ROCK)
			.hardnessAndResistance(3.5F, 3.5F));
		blockReducer.setRegistryName(new ResourceLocation("lambdazation:reducer"));
	}

	public void registerBlocks(RegistryEvent.Register<Block> e) {
		e.getRegistry().register(blockLambdaOre);
		e.getRegistry().register(blockCrystallizer);
		e.getRegistry().register(blockTransformer);
		e.getRegistry().register(blockCharger);
		e.getRegistry().register(blockReducer);
	}

	public void finalizeBlocks(RegistryEvent.Register<Block> e) {

	}
}
