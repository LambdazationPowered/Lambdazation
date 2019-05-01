package org.lambdazation.client.gui.inventory;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.inventory.ContainerReducer;
import org.lambdazation.common.inventory.ContainerReducer.InventoryRefReducer;
import org.lambdazation.common.tileentity.TileEntityReducer;
import org.lambdazation.common.tileentity.TileEntityReducer.InventoryFieldReducer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiReducer extends GuiContainer {
	public static final ResourceLocation REDUCER_RESOURCE = new ResourceLocation("lambdazation", "textures/gui/container/reducer.png");

	public final Lambdazation lambdazation;

	public final ContainerReducer containerReducer;

	public GuiReducer(Lambdazation lambdazation, InventoryPlayer playerInventory,
		TileEntityReducer reducerInventory) {
		super(new ContainerReducer(lambdazation, playerInventory, reducerInventory));

		this.lambdazation = lambdazation;

		this.containerReducer = (ContainerReducer) inventorySlots;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		drawString(fontRenderer, "Aggregate step: " + containerReducer
			.lookupInventoryField(InventoryRefReducer.REDUCER, InventoryFieldReducer.AGGREGATE_STEP),
			4 + 0, 4 + 0, 0xFFFFFF);
		drawString(fontRenderer, "Reduce speed: " + containerReducer
			.lookupInventoryField(InventoryRefReducer.REDUCER, InventoryFieldReducer.REDUCE_SPEED),
			4 + 0, 4 + 8, 0xFFFFFF);
		drawString(fontRenderer, "Reduce time: " + containerReducer
			.lookupInventoryField(InventoryRefReducer.REDUCER, InventoryFieldReducer.REDUCE_TIME),
			4 + 0, 4 + 16, 0xFFFFFF);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(REDUCER_RESOURCE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
