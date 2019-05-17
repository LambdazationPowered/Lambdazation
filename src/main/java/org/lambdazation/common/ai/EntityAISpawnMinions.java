package org.lambdazation.common.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.world.World;
import org.lambdazation.Lambdazation;
import org.lambdazation.common.entity.EntityJava;
import org.lambdazation.common.entity.EntityJavaObject;

public class EntityAISpawnMinions extends EntityAIBase {
	public final Lambdazation lambdazation;
	private final EntityJava entityJava;
	private World world;

	public EntityAISpawnMinions(Lambdazation lambdazation, EntityJava entityJava, World world) {
		this.lambdazation = lambdazation;
		this.entityJava = entityJava;
		this.world = world;
	}

	@Override
	public boolean shouldExecute() {
		return entityJava.getHealth() / entityJava.getMaxHealth() < 0.55;
	}

	@Override
	public void tick() {
		long dayTime = entityJava
			.getWorld()
			.getDayTime();
		if (dayTime % 1000 == 0) {
			EntityWither entityWither = new EntityWither(world);
			entityWither.setPosition(entityJava.posX + Math.random() * 64, entityJava.posY + Math.random() * 8,
				entityJava.posZ + Math.random() * 32);
			world.spawnEntity(entityWither);//todo although I want to spawn Wither
		}
		if (entityJava.getHealth() < entityJava.getMaxHealth() * 0.5) {
			if (dayTime % 200 == 0)
				for (int i = 0; i <= 3 + 2 * Math.random(); i++) {
					EntityJavaObject entityJavaObject = new EntityJavaObject(lambdazation, world);
					entityJavaObject.setPosition(entityJava.posX + Math.random() * 8,
						entityJava.posY + Math.random() * 2, entityJava.posZ + Math.random() * 4);
					world.spawnEntity(entityJavaObject);
				}
		}
	}
}
