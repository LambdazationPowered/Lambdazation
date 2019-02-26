package org.lambdazation.client.core;

import org.lambdazation.Lambdazation;
import org.lambdazation.client.gui.GuiLens;
import org.lambdazation.common.inventory.ContainerLens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;

@OnlyIn(Dist.CLIENT)
public final class LambdazationGuiFactory {
	public final Lambdazation lambdazation;

	public LambdazationGuiFactory(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;
	}

	public GuiScreen openGui(FMLPlayMessages.OpenContainer msg) {
		if (msg.getId().equals(ContainerLens.GUI_ID))
			return new GuiLens(lambdazation, Minecraft.getInstance().player.inventory);
		else
			return null;
	}
}
