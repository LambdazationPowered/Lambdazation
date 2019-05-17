package org.lambdazation.common.entity;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBreakBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Timer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lambdazation.Lambdazation;
import org.lambdazation.common.ai.EntityAIDestroyWorld;
import org.lambdazation.common.ai.EntityAIMemoryError;
import org.lambdazation.common.ai.EntityAISpawnMinions;
import org.lambdazation.common.item.ItemJavaEye;

public final class EntityJava extends EntityMob implements IEntityMultiPart, IBoss, IRangedAttackMob {
	public static final DataParameter<Integer> PHASE = EntityDataManager.createKey(EntityJava.class,
		DataSerializers.VARINT);

	public final Lambdazation lambdazation;
	public static final String JAVA_LAUGH = I18n.format("entity.java.laugh_word");
	public static final String JAVA_LAUGH2 = I18n.format("entity.java.laugh_word2");
	public static final String JAVA_DEATH = I18n.format("entity.java.death_word");
	private final MultiPartEntityPart scorpionHead;
	private final MultiPartEntityPart scorpionPliers1;
	private final MultiPartEntityPart scorpionPliers2;
	private final MultiPartEntityPart scorpionTail1;
	private final MultiPartEntityPart scorpionTail2;
	private final MultiPartEntityPart scorpionTail3;
	private final MultiPartEntityPart scorpionLeg1;
	private final MultiPartEntityPart scorpionLeg2;
	private final MultiPartEntityPart scorpionLeg3;
	private final MultiPartEntityPart scorpionLeg4;
	private final MultiPartEntityPart scorpionLeg5;
	private final MultiPartEntityPart scorpionLeg6;
	private final MultiPartEntityPart scorpionBody1;
	private final MultiPartEntityPart scorpionBody2;
	private final MultiPartEntityPart scorpionBody3;

	public EntityJava(Lambdazation lambdazation, World world) {
		super(lambdazation.lambdazationEntityTypes.entityTypeJava, world);
		this.lambdazation = lambdazation;
		this.setSize(0.9F, 3.5F);
		this.isImmuneToFire = true;
		this
			.getNavigator()
			.setCanSwim(true);
		this.scorpionHead = new MultiPartEntityPart(this, "head", 6.0F, 6.0F);
		this.scorpionPliers1 = new MultiPartEntityPart(this, "pliers1", 8.0F, 8.0F);
		this.scorpionPliers2 = new MultiPartEntityPart(this, "pliers2", 8.0F, 8.0F);
		this.scorpionBody1 = new MultiPartEntityPart(this, "body1", 12, 10);
		this.scorpionBody2 = new MultiPartEntityPart(this, "body1", 12, 10);
		this.scorpionBody3 = new MultiPartEntityPart(this, "body1", 12, 10);
		this.scorpionTail1 = new MultiPartEntityPart(this, "tail", 4.0F, 6.0F);
		this.scorpionTail2 = new MultiPartEntityPart(this, "tail", 4.0F, 6.0F);
		this.scorpionTail3 = new MultiPartEntityPart(this, "tail", 4.0F, 6.0F);
		this.scorpionLeg1 = new MultiPartEntityPart(this, "leg1", 4, 4);
		this.scorpionLeg2 = new MultiPartEntityPart(this, "leg2", 4, 4);
		this.scorpionLeg3 = new MultiPartEntityPart(this, "leg3", 4, 4);
		this.scorpionLeg4 = new MultiPartEntityPart(this, "leg4", 4, 4);
		this.scorpionLeg5 = new MultiPartEntityPart(this, "leg5", 4, 4);
		this.scorpionLeg6 = new MultiPartEntityPart(this, "leg6", 4, 4);
		//this.scorpionArray = new MultiPartEntityPart[]{this.scorpionHead, this.scorpionNeck, this.scorpionBody, this.scorpionTail1, this.scorpionTail2, this.scorpionTail3, this.scorpionWing1, this.scorpionWing2};
	}

	@Override
	public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage) {
		// TODO NYI
		return true;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public void registerAttributes() {
		super.registerAttributes();
		getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2048);
		getAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(5);
		getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(3);
	}

	public void initEntityAI() {
		this.tasks.addTask(1, new EntityAIDestroyWorld(lambdazation, this, this.world, new Timer(20, 0)));
		this.tasks.addTask(5, new EntityAIBreakBlock(Blocks.TURTLE_EGG, this, 1.0D, 3));
		this.tasks.addTask(2, new EntityAISpawnMinions(lambdazation, this, world));
		this.tasks.addTask(3,new EntityAIMemoryError(lambdazation,this));
	}

	@Override
	public void onKillCommand() {
		setHealth(getMaxHealth());
		sendMessage(new TextComponentTranslation(JAVA_LAUGH));
	}

	@SubscribeEvent
	public void onAttack(LivingAttackEvent event) {
		if (event
			.getSource()
			.getTrueSource() instanceof FakePlayer) {
			event.setCanceled(true);
			sendMessage(new TextComponentTranslation(JAVA_LAUGH2));
		}
	}

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		Entity source = event
			.getSource()
			.getTrueSource();
		Entity deathEntity = event.getEntity();
		if (deathEntity instanceof EntityJava) {
			if (!(source instanceof EntityPlayer) || source instanceof FakePlayer) {
				event.setCanceled(true);
				((EntityJava) deathEntity).setHealth(getMaxHealth());
				sendMessage(new TextComponentTranslation(JAVA_LAUGH));
			}
			sendMessage(new TextComponentTranslation(JAVA_DEATH));
			captureDrops().add(new EntityItem(world, deathEntity.posX, deathEntity.posY, deathEntity.posZ,
				new ItemStack(new ItemJavaEye(lambdazation, new Item.Properties()))));
		}
	}

	@Override
	public boolean isNonBoss() {
		return false;
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		//todo NYI
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {

	}
}
