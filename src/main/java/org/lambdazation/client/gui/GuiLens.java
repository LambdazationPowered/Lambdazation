package org.lambdazation.client.gui;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.inventory.ContainerLens;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiLens extends GuiContainer {
	public static final ResourceLocation LENS_RESOURCE = new ResourceLocation("lambdazation", "textures/gui/container/lens.png");

	public final Lambdazation lambdazation;

	public GuiLens(Lambdazation lambdazation, InventoryPlayer inventoryPlayer) {
		super(new ContainerLens(lambdazation, inventoryPlayer));

		this.lambdazation = lambdazation;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawDefaultBackground();

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(LENS_RESOURCE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
