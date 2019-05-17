package org.lambdazation.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public interface IBoss {
	default void setBoss(SPacketUpdateBossInfo info) {
		info.shouldCreateFog();
		info.shouldDarkenSky();
		info.shouldPlayEndBossMusic();
	}

	@SubscribeEvent
	default void killPlayer(EntityEvent.EnteringChunk event, EntityLiving entityLiving) {
		Entity entity = event.getEntity();
		if (entityLiving instanceof IBoss && entityLiving.getDistanceSq(entity) < 192 && entity instanceof EntityPlayer)
			if (((EntityPlayer) entity).abilities.isCreativeMode
				&& entityLiving.getHealth() / entityLiving.getMaxHealth() < 0.85)
				entity.onKillCommand();
	}
}
