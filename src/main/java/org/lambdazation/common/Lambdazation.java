package org.lambdazation.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

@Mod("lambdazation")
public class Lambdazation {
	public Lambdazation() {
		MinecraftForge.EVENT_BUS.register(this);
	}
}
