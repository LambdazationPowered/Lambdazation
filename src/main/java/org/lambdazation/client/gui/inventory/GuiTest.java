package org.lambdazation.client.gui.inventory;

import org.lambdazation.Lambdazation;
import org.lambdazation.client.gui.GuiWidget;
import org.lambdazation.client.gui.widget.WidgetButton;
import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lambdazation.client.gui.widget.view.ViewButton;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiTest extends GuiScreen {
	public static final ResourceLocation BUTTON_RESOURCE = new ResourceLocation("lambdazation", "textures/gui/widget/button.png");

	public final Lambdazation lambdazation;
	public final GuiWidget<WidgetButton<ModelBase, ViewButton<ModelBase>>> button;

	public GuiTest(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;
		this.button = new GuiWidget<>(this, new WidgetButton<>(
			new ModelBase(),
			new ViewButton<>(BUTTON_RESOURCE, 200.0D, 20.0D, "Test"),
			() -> System.out.println("Clicked")), 10.0D, 10.0D);
	}

	@Override
	protected void initGui() {
		super.initGui();

		children.add(button);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);

		button.render(mc, partialTicks);
	}
}
