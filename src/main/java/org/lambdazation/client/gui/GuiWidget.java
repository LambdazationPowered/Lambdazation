package org.lambdazation.client.gui;

import org.lambdazation.client.gui.widget.ExternalInterface;
import org.lambdazation.client.gui.widget.WidgetBase;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiEventHandler;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiWidget<W extends WidgetBase<?, ?>> extends Gui implements IGuiEventListener {
	private final GuiEventHandler parent;
	private final ExternalInterface<W, ?, ?> externalInterface;

	public GuiWidget(GuiEventHandler parent, W widget, double x, double y) {
		this.parent = parent;
		this.externalInterface = newExternalInterface(widget, x, y);
	}

	private static <W extends WidgetBase<?, ?>> ExternalInterface<W, ?, ?> newExternalInterface(W widget, double x, double y) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		ExternalInterface<W, ?, ?> externalInterface = new ExternalInterface(widget, x, y);
		return externalInterface;
	}

	public W getWidget() {
		return externalInterface.getWidget();
	}

	public double getX() {
		return externalInterface.getX();
	}

	public void setX(double x) {
		externalInterface.setX(x);
	}

	public double getY() {
		return externalInterface.getY();
	}

	public void setY(double y) {
		externalInterface.setY(y);
	}

	public void setPosition(double x, double y) {
		externalInterface.setPosition(x, y);
	}

	public boolean isFocused() {
		return externalInterface.isFocused();
	}

	public void setFocused(boolean isFocused) {
		externalInterface.setFocused(isFocused);

		updateFocus();
	}

	public void render(Minecraft minecraft, float partialTicks) {
		if (!externalInterface.isFocused())
			externalInterface.externalResetControllerState();

		double globalX = minecraft.mouseHelper.getMouseX() * (double) minecraft.mainWindow.getScaledWidth() / (double) minecraft.mainWindow.getWidth();
		double globalY = minecraft.mouseHelper.getMouseY() * (double) minecraft.mainWindow.getScaledHeight() / (double) minecraft.mainWindow.getHeight();

		externalInterface.externalMousePosition(globalX, globalY);
		externalInterface.externalDraw(minecraft, partialTicks);

		updateFocus();
	}

	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
		boolean handled = false;

		handled |= externalInterface.externalMousePosition(p_mouseClicked_1_, p_mouseClicked_3_);
		handled |= externalInterface.externalMouseButton(p_mouseClicked_5_, true);
		handled |= IGuiEventListener.super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);

		updateFocus();

		return handled;
	}

	@Override
	public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
		boolean handled = false;

		handled |= externalInterface.externalMousePosition(p_mouseReleased_1_, p_mouseReleased_3_);
		handled |= externalInterface.externalMouseButton(p_mouseReleased_5_, false);
		handled |= IGuiEventListener.super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);

		updateFocus();

		return handled;
	}

	@Override
	public boolean mouseScrolled(double p_mouseScrolled_1_) {
		boolean handled = false;

		handled |= externalInterface.externalMouseWheel(p_mouseScrolled_1_);
		handled |= IGuiEventListener.super.mouseScrolled(p_mouseScrolled_1_);

		updateFocus();

		return handled;
	}

	@Override
	public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
		boolean handled = false;

		handled |= externalInterface.externalKeyboardKey(p_keyPressed_1_, true, p_keyPressed_3_);
		handled |= IGuiEventListener.super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);

		updateFocus();

		return handled;
	}

	@Override
	public boolean keyReleased(int p_keyReleased_1_, int p_keyReleased_2_, int p_keyReleased_3_) {
		boolean handled = false;

		handled |= externalInterface.externalKeyboardKey(p_keyReleased_1_, false, p_keyReleased_3_);
		handled |= IGuiEventListener.super.keyReleased(p_keyReleased_1_, p_keyReleased_2_, p_keyReleased_3_);

		updateFocus();

		return handled;
	}

	@Override
	public boolean charTyped(char p_charTyped_1_, int p_charTyped_2_) {
		boolean handled = false;

		handled |= externalInterface.externalKeyboardChar(p_charTyped_1_);
		handled |= IGuiEventListener.super.charTyped(p_charTyped_1_, p_charTyped_2_);

		updateFocus();

		return handled;
	}

	@Override
	public void focusChanged(boolean focused) {
		IGuiEventListener.super.focusChanged(focused);

		externalInterface.setFocused(focused);
	}

	@Override
	public boolean canFocus() {
		boolean canFocus = false;

		canFocus |= true;
		canFocus |= IGuiEventListener.super.canFocus();

		return canFocus;
	}

	private void updateFocus() {
		boolean isFocused = parent.getFocused() == this;
		boolean shouldFocused = externalInterface.isFocused();

		if (isFocused != shouldFocused) {
			if (shouldFocused)
				parent.focusOn(this);
			else
				parent.focusOn(null);
		}
	}
}
