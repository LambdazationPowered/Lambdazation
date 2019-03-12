package org.lambdazation.common.block;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.state.properties.SlotState;
import org.lambdazation.common.tileentity.TileEntityCrystallizer;
import org.lambdazation.common.utils.RelativeFacing;
import org.lambdazation.common.utils.ValueBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer.Builder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public final class BlockCrystallizer extends BlockContainer {
	public static final EnumProperty<SlotState> DOWN = SlotState.SLOT_STATE_DOWN;
	public static final EnumProperty<SlotState> UP = SlotState.SLOT_STATE_UP;
	public static final EnumProperty<SlotState> NORTH = SlotState.SLOT_STATE_NORTH;
	public static final EnumProperty<SlotState> SOUTH = SlotState.SLOT_STATE_SOUTH;
	public static final EnumProperty<SlotState> WEST = SlotState.SLOT_STATE_WEST;
	public static final EnumProperty<SlotState> EAST = SlotState.SLOT_STATE_EAST;
	public static final Map<EnumFacing, EnumProperty<SlotState>> FACING_PROPERTY_MAP = ValueBuilder
		.<Map<EnumFacing, EnumProperty<SlotState>>> build(new EnumMap<>(EnumFacing.class), builder -> {
			builder.put(EnumFacing.DOWN, DOWN);
			builder.put(EnumFacing.UP, UP);
			builder.put(EnumFacing.NORTH, NORTH);
			builder.put(EnumFacing.SOUTH, SOUTH);
			builder.put(EnumFacing.WEST, WEST);
			builder.put(EnumFacing.EAST, EAST);
		}, Collections::unmodifiableMap);

	public final Lambdazation lambdazation;

	public BlockCrystallizer(Lambdazation lambdazation, Properties properties) {
		super(properties);

		this.lambdazation = lambdazation;

		setDefaultState(stateContainer.getBaseState()
			.with(DOWN, SlotState.OUTPUT)
			.with(UP, SlotState.OUTPUT)
			.with(NORTH, SlotState.INPUT)
			.with(SOUTH, SlotState.INPUT)
			.with(WEST, SlotState.INPUT)
			.with(EAST, SlotState.INPUT));
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

	@Override
	public IBlockState getStateForPlacement(BlockItemUseContext context) {
		EnumFacing placementFacing = context.getNearestLookingDirection();
		EnumFacing placementHorizontalFacing = context.getPlacementHorizontalFacing();
		RelativeFacing relativeFacing = RelativeFacing.of(placementFacing.getOpposite(),
			placementFacing.getAxis().equals(EnumFacing.Axis.Y)
				? Rotation.values()[placementHorizontalFacing.getHorizontalIndex()]
				: Rotation.NONE);

		IBlockState resultState = getDefaultState();
		for (EnumFacing facing : EnumFacing.values())
			resultState = resultState.with(FACING_PROPERTY_MAP.get(facing),
				getDefaultState().get(FACING_PROPERTY_MAP.get(relativeFacing.transform(facing))));
		return resultState;
	}

	@Override
	public IBlockState rotate(IBlockState state, Rotation rot) {
		IBlockState resultState = state;
		for (EnumFacing facing : EnumFacing.values())
			resultState = resultState.with(FACING_PROPERTY_MAP.get(facing),
				state.get(FACING_PROPERTY_MAP.get(rot.rotate(facing))));
		return resultState;
	}

	@Override
	public IBlockState mirror(IBlockState state, Mirror mirrorIn) {
		IBlockState resultState = state;
		for (EnumFacing facing : EnumFacing.values())
			resultState = resultState.with(FACING_PROPERTY_MAP.get(facing),
				state.get(FACING_PROPERTY_MAP.get(mirrorIn.mirror(facing))));
		return resultState;
	}

	@Override
	protected void fillStateContainer(Builder<Block, IBlockState> builder) {
		FACING_PROPERTY_MAP.values().forEach(builder::add);
	}
}
