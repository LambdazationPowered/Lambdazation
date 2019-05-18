package org.lambdazation.client.gui.inventory;

import org.lambdazation.Lambdazation;
import org.lambdazation.client.gui.widget.ExternalInterface;
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
	public final ExternalInterface<WidgetButton<ModelBase, ViewButton<ModelBase>>, ModelBase, ViewButton<ModelBase>> button;

	public GuiTest(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;
		this.button = new ExternalInterface<>(
			new WidgetButton<>(
				new ModelBase(),
				new ViewButton<>(BUTTON_RESOURCE, 6.0D, 6.0D),
				() -> System.out.println("Clicked")),
			10.0D, 10.0D);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);

		double globalX = mc.mouseHelper.getMouseX() * (double) mc.mainWindow.getScaledWidth() / (double) mc.mainWindow.getWidth();
		double globalY = mc.mouseHelper.getMouseY() * (double) mc.mainWindow.getScaledHeight() / (double) mc.mainWindow.getHeight();

		button.externalMousePosition(globalX, globalY);
		button.externalDraw(mc, partialTicks);
	}

	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		boolean handled = false;

		handled |= button.externalMousePosition(p_mouseClicked_1_, p_mouseClicked_3_);
		handled |= button.externalMouseButton(p_mouseClicked_5_, true);
		handled |= super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);

		return handled;
	}

	@Override
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		boolean handled = false;

		handled |= button.externalMousePosition(p_mouseReleased_1_, p_mouseReleased_3_);
		handled |= button.externalMouseButton(p_mouseReleased_5_, false);
		handled |= super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);

		return handled;
	}

	@Override
	public boolean mouseScrolled(double p_mouseScrolled_1_) {
		boolean handled = false;

		handled |= button.externalMouseWheel(p_mouseScrolled_1_);
		handled |= super.mouseScrolled(p_mouseScrolled_1_);

		return handled;

	}

	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		boolean handled = false;

		handled |= button.externalKeyboardKey(p_keyPressed_1_, true, p_keyPressed_3_);
		handled |= super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);

		return handled;

	}

	@Override
	public boolean keyReleased(int p_keyReleased_1_, int p_keyReleased_2_, int p_keyReleased_3_) {
		boolean handled = false;

		handled |= button.externalKeyboardKey(p_keyReleased_1_, false, p_keyReleased_3_);
		handled |= super.keyReleased(p_keyReleased_1_, p_keyReleased_2_, p_keyReleased_3_);

		return handled;

	}

	@Override
	public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
		boolean handled = false;

		handled |= button.externalKeyboardChar(p_charTyped_1_);
		handled |= super.charTyped(p_charTyped_1_, p_charTyped_2_);

		return handled;

	}
}
