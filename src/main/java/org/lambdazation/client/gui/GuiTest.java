package org.lambdazation.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiTest extends GuiScreen {
	public GuiTest() {

	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawString(fontRenderer, "Test", 0, 0, 0xFFFFFF);
		super.render(mouseX, mouseY, partialTicks);
	}
}
