package org.lambdazation.client.gui;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.inventory.ContainerLens;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiLens extends GuiContainer {
	public GuiLens(Lambdazation lambdazation, IInventory playerInventory) {
		super(new ContainerLens(lambdazation, playerInventory));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawDefaultBackground();
		drawString(fontRenderer, "Test", 0, 0, 0xFFFFFF);
	}
}
