package org.lambdazation.client.gui;

import org.lambdazation.client.core.LambdazationClientProxy;
import org.lambdazation.client.gui.widget.WidgetButton;
import org.lambdazation.client.gui.widget.WidgetContainer;
import org.lambdazation.client.gui.widget.WidgetLabel;
import org.lambdazation.client.gui.widget.WidgetSprite;
import org.lambdazation.client.gui.widget.WidgetText;
import org.lambdazation.client.gui.widget.WidgetViewport;
import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lambdazation.client.gui.widget.model.ModelText;
import org.lambdazation.client.gui.widget.view.ViewButton;
import org.lambdazation.client.gui.widget.view.ViewContainer;
import org.lambdazation.client.gui.widget.view.ViewLabel;
import org.lambdazation.client.gui.widget.view.ViewSprite;
import org.lambdazation.client.gui.widget.view.ViewText;
import org.lambdazation.client.gui.widget.view.ViewViewport;
import org.lambdazation.common.util.ValueBuilder;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiTest extends GuiScreen {
	public final LambdazationClientProxy proxy;

	public final GuiWidget<WidgetContainer<ModelBase, ViewContainer<ModelBase>>> container;

	public GuiTest(LambdazationClientProxy proxy) {
		this.proxy = proxy;

		WidgetButton<ModelBase, ViewButton<ModelBase>> widgetButton1 = new WidgetButton<>(
			new ModelBase(),
			new ViewButton<>(200.0D, 20.0D, "Test1"),
			() -> System.out.println("Test1"));

		WidgetButton<ModelBase, ViewButton<ModelBase>> widgetButton2 = new WidgetButton<>(
			new ModelBase(),
			new ViewButton<>(200.0D, 20.0D, "Test2"),
			() -> System.out.println("Test2"));

		WidgetViewport<ModelBase, ViewViewport<ModelBase>> widgetViewPort = new WidgetViewport<>(
			new ModelBase(),
			new ViewViewport<>(widgetButton2, 1.0D, 1.0D, 198.0D, 18.0D));

		WidgetLabel<ModelBase, ViewLabel<ModelBase>> widgetLabel = new WidgetLabel<>(
			new ModelBase(),
			new ViewLabel<>("Test3"));

		WidgetSprite<ModelBase, ViewSprite<ModelBase>> widgetSprite = new WidgetSprite<>(
			new ModelBase(),
			new ViewSprite<>(new ResourceLocation("minecraft", "textures/block/oak_planks.png")));

		WidgetText<ModelText, ViewText<ModelText>> widgetText = new WidgetText<>(
			new ModelText(""),
			new ViewText<>());

		WidgetContainer<ModelBase, ViewContainer<ModelBase>> widgetContainer = new WidgetContainer<>(
			new ModelBase(),
			ValueBuilder.build(new ViewContainer<>(), viewContainer -> {
				viewContainer.addComponent(widgetButton1, 10.0D, 10.0D);
				viewContainer.addComponent(widgetViewPort, 10.0D, 40.0D);
				viewContainer.addComponent(widgetLabel, 10.0D, 70.0D);
				viewContainer.addComponent(widgetSprite, 10.0D, 100.0D);
				viewContainer.addComponent(widgetText, 10.0D, 130.0D);
			}));

		this.container = new GuiWidget<>(proxy, this, widgetContainer, 0.0D, 0.0D);
	}

	@Override
	protected void initGui() {
		super.initGui();

		children.add(container);
		focusOn(container);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);

		container.render(mc, partialTicks);
	}

	@Override
	public void tick() {
		super.tick();

		container.tick();
	}
}
