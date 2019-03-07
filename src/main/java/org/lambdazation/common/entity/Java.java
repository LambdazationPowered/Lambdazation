package org.lambdazation.common.entity;

import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import static org.lambdazation.common.core.LambdazationEntities.JAVA;

public class Java extends EntityMob implements IEntityMultiPart,IBoss {
    public static final DataParameter<Integer> PHASE = EntityDataManager.createKey(Java.class, DataSerializers.VARINT);
    public Java(World world){
        super(JAVA,world);
    }
    public void registerAttributes(){
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2048);
        getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(3);
    }
    public World getWorld(){
        return this.world;
    }

    @Override
    public boolean attackEntityFromPart(MultiPartEntityPart part, DamageSource source, float damage) {
        //todo
        return true;
    }
}
