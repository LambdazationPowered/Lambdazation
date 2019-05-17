package org.lambdazation.common.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.lambdazation.Lambdazation;
import org.lambdazation.common.entity.EntityJava;
import org.lambdazation.common.entity.EntityJavaObject;

public class EntityAIMemoryError extends EntityAIBase {
	public final Lambdazation lambdazation;
	public final EntityJava entityJava;

	public EntityAIMemoryError(Lambdazation lambdazation, EntityJava entityJava) {
		this.entityJava = entityJava;
		this.lambdazation = lambdazation;
	}

	@Override
	public boolean shouldExecute() {
		BlockPos playerPos = entityJava.world
			.getClosestPlayerToEntity(entityJava, 16)
			.getPosition();
		if (playerPos != null)
			return entityJava.world
				.getEntitiesWithinAABB(EntityJavaObject.class, new AxisAlignedBB(entityJava.getPosition(), playerPos))
				.size() > 32;
		else
			return false;
	}

	@Override
	public void tick() {
		entityJava.sendMessage(new TextComponentString("java.lang.OutOfMemoryError: Java heap space"));
	}
}
