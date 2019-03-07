package org.lambdazation.common.entity;

import org.lambdazation.Lambdazation;

import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public final class EntityJava extends EntityMob implements IEntityMultiPart, IBoss {
	public static final DataParameter<Integer> PHASE = EntityDataManager.createKey(EntityJava.class,
		DataSerializers.VARINT);

	public final Lambdazation lambdazation;

	public EntityJava(Lambdazation lambdazation, World world) {
		super(lambdazation.lambdazationEntityTypes.entityTypeJava, world);

		this.lambdazation = lambdazation;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage) {
		// TODO NYI
		return true;
	}

	@Override
	public void registerAttributes() {
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2048);
		getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(3);
	}
}
