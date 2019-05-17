package org.lambdazation.common.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Timer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lambdazation.Lambdazation;
import org.lambdazation.common.entity.EntityJava;

import java.util.Iterator;

public class EntityAIDestroyWorld extends EntityAIBase {
	public final Lambdazation lambdazation;
	public final World world;
	private final EntityJava entityJava;
	private Timer timer;

	public EntityAIDestroyWorld(Lambdazation lambdazation, EntityJava entityJava, World world, Timer timer) {
		this.lambdazation = lambdazation;
		this.entityJava = entityJava;
		this.world = world;
		this.timer = timer;
	}

	@Override
	public boolean shouldExecute() {
		EntityPlayer closestPlayer = world.getClosestPlayerToEntity(entityJava, 32);
		if (closestPlayer != null && !closestPlayer.abilities.isFlying) {
			timer.updateTimer(0);
			return closestPlayer.getDistance(entityJava) < 8 && Math.random() < 0.2;
		} else
			return false;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
	}

	@Override
	public void resetTask() {
		super.resetTask();
	}

	@Override
	public void tick() {
		EntityPlayer closestPlayer = world.getClosestPlayerToEntity(entityJava, 32);
		if (closestPlayer != null) {
			Iterable<BlockPos> blockPosIterable = BlockPos.getAllInBox(closestPlayer.getPosition(), closestPlayer
				.getPosition()
				.east(15)
				.south(15)
				.down(128));
			Iterator<BlockPos> posIterator = blockPosIterable.iterator();
			while (posIterator.hasNext()) {
				world.setBlockState(posIterator.next(), Blocks.AIR.getDefaultState());
			}
		}
		if (closestPlayer == null && timer.elapsedTicks > 100) {
			BlockPos pos = entityJava.getPosition();
			Iterable<BlockPos> blockPosIterable = BlockPos.getAllInBox(pos
				.west(64)
				.north(64), pos
				.east(64)
				.south(64)
				.down(pos.getY()));
			Iterator<BlockPos> posIterator = blockPosIterable.iterator();
			while (posIterator.hasNext()) {
				BlockPos blockPos = posIterator.next();
				if (world
					.getBlockState(blockPos)
					.getBlock() == Blocks.AIR)
					world.setBlockState(blockPos, Blocks.DIRT.getDefaultState());
			}
		}
	}
}
