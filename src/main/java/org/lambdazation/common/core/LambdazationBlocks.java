package org.lambdazation.common.core;

import org.lambdazation.Lambdazation;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;

public final class LambdazationBlocks {
	public final Lambdazation lambdazation;

	public LambdazationBlocks(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;
	}

	public void registerBlocks(RegistryEvent.Register<Block> registry) {

	}
}
