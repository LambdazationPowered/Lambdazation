package org.lambdazation.common.block;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.tileentity.TileEntityCrystallizer;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public final class BlockCrystallizer extends BlockContainer {
	public final Lambdazation lambdazation;

	public BlockCrystallizer(Lambdazation lambdazation, Properties properties) {
		super(properties);

		this.lambdazation = lambdazation;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = worldIn.getTileEntity(pos);
			if (tileentity instanceof TileEntityCrystallizer) {
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityCrystallizer) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}

			super.onReplaced(state, worldIn, pos, newState, isMoving);
		}
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand,
		EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			return true;

		if (!(player instanceof EntityPlayerMP))
			return false;
		EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;

		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (!(tileEntity instanceof TileEntityCrystallizer))
			return false;
		TileEntityCrystallizer tileEntityCrystallizer = (TileEntityCrystallizer) tileEntity;

		NetworkHooks.openGui(entityPlayerMP, tileEntityCrystallizer, pos);

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
		return new TileEntityCrystallizer(lambdazation);
	}
}
