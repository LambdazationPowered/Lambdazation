package org.lambdazation.client.gui.inventory;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.inventory.ContainerTransformer;
import org.lambdazation.common.inventory.ContainerTransformer.InventoryRefTransformer;
import org.lambdazation.common.tileentity.TileEntityTransformer;
import org.lambdazation.common.tileentity.TileEntityTransformer.InventoryFieldTransformer;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiTransformer extends GuiContainer {
	public static final ResourceLocation TRANSFORMER_RESOURCE = new ResourceLocation("lambdazation", "textures/gui/container/transformer.png");

	public final Lambdazation lambdazation;

	public final ContainerTransformer containerTransformer;

	public GuiTransformer(Lambdazation lambdazation, InventoryPlayer playerInventory,
		TileEntityTransformer transformerInventory) {
		super(new ContainerTransformer(lambdazation, playerInventory, transformerInventory));

		this.lambdazation = lambdazation;

		this.containerTransformer = (ContainerTransformer) inventorySlots;
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		drawString(fontRenderer, "Total time: " + containerTransformer
			.lookupInventoryField(InventoryRefTransformer.TRANSFORMER, InventoryFieldTransformer.TOTAL_TIME),
			4 + 0, 4 + 0, 0xFFFFFFFF);
		drawString(fontRenderer, "Transforme time: " + containerTransformer
			.lookupInventoryField(InventoryRefTransformer.TRANSFORMER, InventoryFieldTransformer.TRANSFORM_TIME),
			4 + 0, 4 + 8, 0xFFFFFFFF);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.getTextureManager().bindTexture(TRANSFORMER_RESOURCE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}
