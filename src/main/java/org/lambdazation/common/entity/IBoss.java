package org.lambdazation.common.entity;

import net.minecraft.network.play.server.SPacketUpdateBossInfo;

public interface IBoss {
	default void setBoss(SPacketUpdateBossInfo info) {
		info.shouldCreateFog();
		info.shouldDarkenSky();
		info.shouldPlayEndBossMusic();
	}
}
