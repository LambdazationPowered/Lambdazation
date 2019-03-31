package org.lambdazation.client.gui;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.inventory.ContainerCharger;
import org.lambdazation.common.inventory.ContainerCharger.InventoryRefCharger;
import org.lambdazation.common.tileentity.TileEntityCharger;
import org.lambdazation.common.tileentity.TileEntityCharger.InventoryFieldCharger;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiCharger extends GuiContainer {
	public static final ResourceLocation CHARGER_RESOURCE = new ResourceLocation("lambdazation", "textures/gui/container/charger.png");

	public final Lambdazation lambdazation;

	public final ContainerCharger containerCharger;

	public GuiCharger(Lambdazation lambdazation, InventoryPlayer playerInventory,
		TileEntityCharger chargerInventory) {
		super(new ContainerCharger(lambdazation, playerInventory, chargerInventory));

		this.lambdazation = lambdazation;

		this.containerCharger = (ContainerCharger) inventorySlots;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		drawString(fontRenderer, "Capacity: " + containerCharger
			.lookupInventoryField(InventoryRefCharger.CHARGER, InventoryFieldCharger.CAPACITY),
			4 + 0, 4 + 0, 0xFFFFFF);
		drawString(fontRenderer, "Energy: " + containerCharger
			.lookupInventoryField(InventoryRefCharger.CHARGER, InventoryFieldCharger.ENERGY),
			4 + 0, 4 + 8, 0xFFFFFF);
		drawString(fontRenderer, "Charge speed: " + containerCharger
			.lookupInventoryField(InventoryRefCharger.CHARGER, InventoryFieldCharger.CHARGE_SPEED),
			4 + 0, 4 + 16, 0xFFFFFF);
		drawString(fontRenderer, "Burn speed: " + containerCharger
			.lookupInventoryField(InventoryRefCharger.CHARGER, InventoryFieldCharger.BURN_SPEED),
			4 + 0, 4 + 24, 0xFFFFFF);
		drawString(fontRenderer, "Burn time: " + containerCharger
			.lookupInventoryField(InventoryRefCharger.CHARGER, InventoryFieldCharger.BURN_TIME),
			4 + 0, 4 + 32, 0xFFFFFF);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(CHARGER_RESOURCE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
