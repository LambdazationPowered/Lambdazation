package org.lambdazation.client.core;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;

@OnlyIn(Dist.CLIENT)
public final class LambdazationGuiFactory {
	public GuiScreen openGui(FMLPlayMessages.OpenContainer msg) {
		return null;
	}
}
