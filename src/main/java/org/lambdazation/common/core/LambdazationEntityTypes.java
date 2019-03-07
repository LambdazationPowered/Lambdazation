package org.lambdazation.common.core;

import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import org.lambdazation.Lambdazation;
import org.lambdazation.common.entity.EntityCSharp;
import org.lambdazation.common.entity.EntityJava;

public final class LambdazationEntityTypes {
	public final Lambdazation lambdazation;

	public final EntityType<EntityJava> entityTypeJava;
	public final EntityType<EntityCSharp> entityTypeCSharp;

	public LambdazationEntityTypes(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

		entityTypeJava = EntityType.Builder
			.create(EntityJava.class, world -> new EntityJava(lambdazation, world))
			.build("lambdazation:java");
		entityTypeJava.setRegistryName(new ResourceLocation("lambdazation:java"));
		entityTypeCSharp = EntityType.Builder
			.create(EntityCSharp.class, world -> new EntityCSharp(lambdazation, world))
			.build("lambdazation:csharp");
		entityTypeCSharp.setRegistryName(new ResourceLocation("lambdazation:csharp"));
	}

	public void registerEntityTypes(RegistryEvent.Register<EntityType<?>> e) {
		e.getRegistry().register(entityTypeJava);
		e.getRegistry().register(entityTypeCSharp);
	}

	public void finalizeEntityTypes(RegistryEvent.Register<EntityType<?>> e) {

	}
}
