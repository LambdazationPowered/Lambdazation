package org.lambdazation.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lambdazation.Lambdazation;

import java.util.Iterator;

public class EntityCSharpObject extends EntityMob {
	public final Lambdazation lambdazation;

	public EntityCSharpObject(Lambdazation lambdazation, World world) {
		super(lambdazation.lambdazationEntityTypes.entityTypeCSharpObject, world);
		this.lambdazation = lambdazation;
		setAIMoveSpeed(6);
	}

	public void initEntityAI() {
		this.tasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
		this.tasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityJavaObject.class, true));
		this.tasks.addTask(3, new EntityAIBreakBlock(Blocks.TURTLE_EGG, this, 1, 3));
		//todo NYI
	}

	@SubscribeEvent
	public void chanceSpawn(LivingDeathEvent event) {
		if (event.getEntity() instanceof EntityCSharp) {
			Iterator<Biome> iterator = ForgeRegistries.BIOMES.getValues().iterator();
			while (iterator.hasNext())
				iterator.next().getSpawns(EnumCreatureType.CREATURE).
					add(new Biome.SpawnListEntry(lambdazation.lambdazationEntityTypes.entityTypeCSharpObject,
						1, 1, 10));
		}
	}

	@SubscribeEvent
	public void changeCreatureToMobs(LivingAttackEvent event){
		if(event.getSource().getTrueSource()==this){
			Entity entity=event.getEntity();
			if(entity instanceof EntityAnimal){
				((EntityAnimal) entity).setAttackTarget(world.getClosestPlayerToEntity(entity,32));
				((EntityAnimal) entity).targetTasks.addTask(1,new EntityAIAttackMelee((EntityAnimal)entity,1,true));
			}
		}
	}
}
