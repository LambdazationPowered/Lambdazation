package org.lambdazation.common.block;

import java.util.Random;

import org.lambdazation.Lambdazation;

import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public final class BlockLambdaOre extends BlockOre {
	public final Lambdazation lambdazation;

	public BlockLambdaOre(Lambdazation lambdazation, Properties properties) {
		super(properties);

		this.lambdazation = lambdazation;
	}

	@Override
	public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
		if (this == lambdazation.lambdazationBlocks.blockLambdaOre)
			return lambdazation.lambdazationItems.itemLambdaCrystal;
		else
			return this;
	}

	@Override
	public int quantityDropped(IBlockState state, Random random) {
		return 1;
	}

	@Override
	public int getExpDrop(IBlockState state, IWorldReader reader, BlockPos pos, int fortune) {
		if (this == lambdazation.lambdazationBlocks.blockLambdaOre)
			return MathHelper.nextInt(RANDOM, 550, 2920);
		else
			return 0;
	}
}
