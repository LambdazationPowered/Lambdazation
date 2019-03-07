package org.lambdazation.common.entity;

import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import static org.lambdazation.common.core.LambdazationEntities.CSHARP;

public class CSharp extends EntityMob implements IEntityMultiPart,IBoss {
    public static final DataParameter<Integer> PHASE = EntityDataManager.createKey(CSharp.class, DataSerializers.VARINT);
    public CSharp(World world){
        super(CSHARP,world);
    }
    public World getWorld(){
        return this.world;
    }
    public boolean attackEntityFromPart(MultiPartEntityPart partEntityPart, DamageSource source,float harm){
        //todo
        return true;
    }
    public void registerAttributes(){
        super.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(2560);
    }
}
