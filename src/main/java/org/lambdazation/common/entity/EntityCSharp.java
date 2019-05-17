package org.lambdazation.common.entity;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBreakBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lambdazation.Lambdazation;

import net.minecraft.entity.ai.EntityAIAttackRanged;

import net.minecraft.entity.monster.EntityMob;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.lambdazation.common.item.ItemJavaEye;

public final class EntityCSharp extends EntityMob implements IEntityMultiPart, IBoss, IRangedAttackMob {
	public static final DataParameter<Integer> PHASE = EntityDataManager.createKey(EntityCSharp.class,
		DataSerializers.VARINT);
	public static final String CSHARP_LAUGH1 = I18n.format("entity.csharp.laugh_word");
	public static final String CSHARP_LAUGH2 = I18n.format("entity.csharp.laugh_word2");
	public static final String CSHARP_DEATH = I18n.format("entity.csharp.death_word");
	public final MultiPartEntityPart octopusHead;
	public final MultiPartEntityPart octupusLeg1_1;
	public final MultiPartEntityPart octupusLeg1_2;
	public final MultiPartEntityPart octupusLeg1_3;
	public final MultiPartEntityPart octupusLeg2_1;
	public final MultiPartEntityPart octupusLeg2_2;
	public final MultiPartEntityPart octupusLeg2_3;
	public final MultiPartEntityPart octupusLeg3_1;
	public final MultiPartEntityPart octupusLeg3_2;
	public final MultiPartEntityPart octupusLeg3_3;
	public final MultiPartEntityPart octupusLeg4_1;
	public final MultiPartEntityPart octupusLeg4_2;
	public final MultiPartEntityPart octupusLeg4_3;
	public final MultiPartEntityPart octupusLeg5_1;
	public final MultiPartEntityPart octupusLeg5_2;
	public final MultiPartEntityPart octupusLeg5_3;
	public final MultiPartEntityPart octupusLeg6_1;
	public final MultiPartEntityPart octupusLeg6_2;
	public final MultiPartEntityPart octupusLeg6_3;
	public final MultiPartEntityPart octupusLeg7_1;
	public final MultiPartEntityPart octupusLeg7_2;
	public final MultiPartEntityPart octupusLeg7_3;
	public final MultiPartEntityPart octupusLeg8_1;
	public final MultiPartEntityPart octupusLeg8_2;
	public final MultiPartEntityPart octupusLeg8_3;



	public final Lambdazation lambdazation;

	public EntityCSharp(Lambdazation lambdazation, World world) {
		super(lambdazation.lambdazationEntityTypes.entityTypeCSharp, world);

		this.lambdazation = lambdazation;
		octopusHead=new MultiPartEntityPart(this,"octopus_head", 15,20);
		octupusLeg1_1=new MultiPartEntityPart(this,"octopus_leg_1_1", 5,10);
		octupusLeg1_2=new MultiPartEntityPart(this,"octopus_leg_1_2", 5,10);
		octupusLeg1_3=new MultiPartEntityPart(this,"octopus_leg_1_3", 5,10);
		octupusLeg2_1=new MultiPartEntityPart(this,"octopus_leg_2_1", 5,10);
		octupusLeg2_2=new MultiPartEntityPart(this,"octopus_leg_2_2", 5,10);
		octupusLeg2_3=new MultiPartEntityPart(this,"octopus_leg_2_3", 5,10);
		octupusLeg3_1=new MultiPartEntityPart(this,"octopus_leg_3_1", 5,10);
		octupusLeg3_2=new MultiPartEntityPart(this,"octopus_leg_3_2", 5,10);
		octupusLeg3_3=new MultiPartEntityPart(this,"octopus_leg_3_3", 5,10);
		octupusLeg4_1=new MultiPartEntityPart(this,"octopus_leg_4_1", 5,10);
		octupusLeg4_2=new MultiPartEntityPart(this,"octopus_leg_4_2", 5,10);
		octupusLeg4_3=new MultiPartEntityPart(this,"octopus_leg_4_3", 5,10);
		octupusLeg5_1=new MultiPartEntityPart(this,"octopus_leg_5_1", 5,10);
		octupusLeg5_2=new MultiPartEntityPart(this,"octopus_leg_5_2", 5,10);
		octupusLeg5_3=new MultiPartEntityPart(this,"octopus_leg_5_3", 5,10);
		octupusLeg6_1=new MultiPartEntityPart(this,"octopus_leg_6_1", 5,10);
		octupusLeg6_2=new MultiPartEntityPart(this,"octopus_leg_6_2", 5,10);
		octupusLeg6_3=new MultiPartEntityPart(this,"octopus_leg_6_3", 5,10);
		octupusLeg7_1=new MultiPartEntityPart(this,"octopus_leg_7_1", 5,10);
		octupusLeg7_2=new MultiPartEntityPart(this,"octopus_leg_7_2", 5,10);
		octupusLeg7_3=new MultiPartEntityPart(this,"octopus_leg_7_3", 5,10);
		octupusLeg8_1=new MultiPartEntityPart(this,"octopus_leg_8_1", 5,10);
		octupusLeg8_2=new MultiPartEntityPart(this,"octopus_leg_8_2", 5,10);
		octupusLeg8_3=new MultiPartEntityPart(this,"octopus_leg_8_3", 5,10);
	}

	public void initEntityAI() {
		this.tasks.addTask(0, new EntityAIAttackRanged(this, 3, 100, 40));
		this.tasks.addTask(5, new EntityAIBreakBlock(Blocks.TURTLE_EGG, this, 1.0D, 3));
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart partEntityPart, DamageSource source, float harm) {
		// TODO NYI
		return true;
	}

	@Override
	public void registerAttributes() {
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2560);
		getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(6);
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {

	}

	@Override
	public void onKillCommand() {
		setHealth(getMaxHealth());
		sendMessage(new TextComponentTranslation(CSHARP_LAUGH1));
	}

	@SubscribeEvent
	public void onAttack(LivingAttackEvent event) {
		if (event
			.getSource()
			.getTrueSource() instanceof FakePlayer) {
			event.setCanceled(true);
			sendMessage(new TextComponentTranslation(CSHARP_LAUGH2));
		}
	}

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		Entity source = event
			.getSource()
			.getTrueSource();
		Entity deathEntity = event.getEntity();
		if (deathEntity instanceof EntityCSharp) {
			if (!(source instanceof EntityPlayer) || source instanceof FakePlayer) {
				event.setCanceled(true);
				((EntityCSharp) deathEntity).setHealth(getMaxHealth());
				sendMessage(new TextComponentTranslation(CSHARP_LAUGH2));
			}
			sendMessage(new TextComponentTranslation(CSHARP_DEATH));
			captureDrops().add(new EntityItem(world, deathEntity.posX, deathEntity.posY, deathEntity.posZ,
				new ItemStack(new ItemJavaEye(lambdazation, new Item.Properties()))));
		}
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {

	}

	@Override
	public boolean isNonBoss() {
		return false;
	}
}
