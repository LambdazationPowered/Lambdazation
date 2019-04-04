package org.lambdazation.common.block;

import org.lambdazation.Lambdazation;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;

public final class BlockLambdaBlock extends Block {
	public final Lambdazation lambdazation;

	public BlockLambdaBlock(Lambdazation lambdazation, Properties properties) {
		super(properties);
		this.lambdazation = lambdazation;
	}

	@Override
	public boolean isBeaconBase(IBlockState state, IWorldReader world, BlockPos pos, BlockPos beacon) {
		return true;
	}
}
