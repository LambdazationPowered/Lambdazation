package org.lambdazation.client.gui.inventory;

import org.lambdazation.Lambdazation;
import org.lambdazation.client.gui.GuiWidget;
import org.lambdazation.client.gui.widget.WidgetButton;
import org.lambdazation.client.gui.widget.WidgetContainer;
import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lambdazation.client.gui.widget.view.ViewButton;
import org.lambdazation.client.gui.widget.view.ViewContainer;
import org.lambdazation.common.util.ValueBuilder;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiTest extends GuiScreen {
	public static final ResourceLocation BUTTON_RESOURCE = new ResourceLocation("lambdazation", "textures/gui/widget/button.png");

	public final Lambdazation lambdazation;
	public final GuiWidget<WidgetContainer<ModelBase, ViewContainer<ModelBase>>> container;

	public GuiTest(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

		WidgetButton<ModelBase, ViewButton<ModelBase>> widgetButton1 = new WidgetButton<>(
			new ModelBase(),
			new ViewButton<>(BUTTON_RESOURCE, 200.0D, 20.0D, "Test1"),
			() -> System.out.println("Test1"));

		WidgetButton<ModelBase, ViewButton<ModelBase>> widgetButton2 = new WidgetButton<>(
			new ModelBase(),
			new ViewButton<>(BUTTON_RESOURCE, 200.0D, 20.0D, "Test2"),
			() -> System.out.println("Test2"));

		WidgetContainer<ModelBase, ViewContainer<ModelBase>> widgetContainer = new WidgetContainer<>(
			new ModelBase(),
			ValueBuilder.build(new ViewContainer<>(), viewContainer -> {
				viewContainer.addComponent(widgetButton1, 10.0D, 10.0D);
				viewContainer.addComponent(widgetButton2, 10.0D, 40.0D);
			}));

		this.container = new GuiWidget<>(this, widgetContainer, 0.0D, 0.0D);
	}

	@Override
	protected void initGui() {
		super.initGui();

		children.add(container);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);

		container.render(mc, partialTicks);
	}
}
