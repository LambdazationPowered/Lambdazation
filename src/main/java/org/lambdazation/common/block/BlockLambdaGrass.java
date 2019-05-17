package org.lambdazation.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import org.lambdazation.Lambdazation;

public class BlockLambdaGrass extends Block {
    public final Lambdazation lambdazation;

    public BlockLambdaGrass(Lambdazation lambdazation,Properties properties){
        super(properties);
        this.lambdazation=lambdazation;
    }

    public boolean canSustainPlant(IBlockState state, IBlockReader world, BlockPos pos, EnumFacing facing, net.minecraftforge.common.IPlantable plantable){
        return true;
    }

}
