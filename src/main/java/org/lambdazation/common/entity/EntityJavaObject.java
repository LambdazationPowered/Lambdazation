package org.lambdazation.common.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBreakBlock;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lambdazation.Lambdazation;

import java.util.Iterator;

public class EntityJavaObject extends EntityMob {
	public final Lambdazation lambdazation;

	public EntityJavaObject(Lambdazation lambdazation, World world) {
		super(lambdazation.lambdazationEntityTypes.entityTypeJava, world);
		this.lambdazation = lambdazation;
	}

	public void registerAttributes() {
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4);
	}

	public void initEntityAI() {
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
		this.tasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityLiving.class, true));
		this.targetTasks.addTask(3, new EntityAIBreakBlock(Blocks.TURTLE_EGG, this, 1, 3));
	}

	@SubscribeEvent
	public void changeEntity(LivingDamageEvent event) {
		EntityLivingBase entityLiving = event.getEntityLiving();
		if (entityLiving instanceof EntityLiving && event
			.getSource()
			.getTrueSource() instanceof EntityJavaObject) {
			EntityJavaObject entityJavaObject = new EntityJavaObject(lambdazation, world);
			entityJavaObject.setPosition(entityLiving.posX, entityLiving.posZ, entityLiving.posZ);
			this.setNoAI(((EntityLiving) entityLiving).isAIDisabled());
			world.spawnEntity(entityJavaObject);
			entityLiving.remove();
		}
	}

	public void chanceSpawn() {
		Iterator<Biome> iterator = ForgeRegistries.BIOMES.getValues().iterator();
		while (iterator.hasNext())
			iterator.next().getSpawns(EnumCreatureType.CREATURE).
				add(new Biome.SpawnListEntry(lambdazation.lambdazationEntityTypes.entityTypeJavaObject,
					1, 1, 10));
	}

	@Override
	public void onDeath(DamageSource cause) {
		captureDrops().add(new EntityItem(world,posX,posY,posZ,new ItemStack(lambdazation.lambdazationItems.itemOOPSoul)));//todo
	}
}
