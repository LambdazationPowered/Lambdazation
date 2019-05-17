package org.lambdazation.common.core;

import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import org.lambdazation.Lambdazation;
import org.lambdazation.common.entity.EntityCSharp;
import org.lambdazation.common.entity.EntityCSharpObject;
import org.lambdazation.common.entity.EntityJava;
import org.lambdazation.common.entity.EntityJavaObject;

public final class LambdazationEntityTypes {
	public final Lambdazation lambdazation;

	public final EntityType<EntityJava> entityTypeJava;
	public final EntityType<EntityCSharp> entityTypeCSharp;
	public final EntityType<EntityJavaObject> entityTypeJavaObject;
	public final EntityType<EntityCSharpObject> entityTypeCSharpObject;

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
		entityTypeJavaObject = EntityType.Builder
			.create(EntityJavaObject.class, world -> new EntityJavaObject(lambdazation, world))
			.build("lambdalazation:java_object");
		entityTypeJavaObject.setRegistryName(new ResourceLocation("lambdalazation:java_object"));
		entityTypeCSharpObject = EntityType.Builder
			.create(EntityCSharpObject.class, world -> new EntityCSharpObject(lambdazation, world))
			.build("lambdalazation:csharp_object");
		entityTypeCSharpObject.setRegistryName(new ResourceLocation("lambdalazation:csharp_object"));
	}

	public void registerEntityTypes(RegistryEvent.Register<EntityType<?>> e) {
		e.getRegistry().register(entityTypeJava);
		e.getRegistry().register(entityTypeCSharp);
		e.getRegistry().register(entityTypeJavaObject);
		e.getRegistry().register(entityTypeCSharpObject);
	}

	public void finalizeEntityTypes(RegistryEvent.Register<EntityType<?>> e) {

	}
}
