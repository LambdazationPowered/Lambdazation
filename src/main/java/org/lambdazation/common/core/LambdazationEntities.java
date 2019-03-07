package org.lambdazation.common.core;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import org.lambdazation.Lambdazation;
import org.lambdazation.common.entity.CSharp;
import org.lambdazation.common.entity.Java;

public class LambdazationEntities {
    public final Lambdazation lambdazation;
    public static EntityType<Java> JAVA= EntityType.Builder.create(Java.class,null).build("Java");
    public static EntityType<CSharp> CSHARP=EntityType.Builder.create(CSharp.class,null).build("CSharp");

    public LambdazationEntities(Lambdazation lambdazation) {
        this.lambdazation = lambdazation;
    }

    public void registerEntities(RegistryEvent.Register<EntityType<?>> e) {
        e.getRegistry().register(JAVA);
    }

    public void finalizeEntities(RegistryEvent.Register<EntityType<?>> e) {

    }
}
