package org.lambdazation.client.gui.widget;

import org.lambdazation.client.gui.widget.WidgetBase.InputContext;
import org.lambdazation.client.gui.widget.context.KeyboardContext;
import org.lambdazation.client.gui.widget.context.MouseContext;
import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lambdazation.client.gui.widget.view.ViewBase;
import org.lambdazation.client.gui.widget.view.ViewBase.DrawContext;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ExternalInterface<W extends WidgetBase<M, V>, M extends ModelBase, V extends ViewBase<M>> {
	private final W widget;
	private double x;
	private double y;

	private int keyboardModifiers;
	private IntSet keyboardKeyPressed;
	private double mouseLocalX;
	private double mouseLocalY;
	private IntSet mouseButtonPressed;

	public ExternalInterface(W widget, double x, double y) {
		this.widget = widget;
		this.x = x;
		this.y = y;

		this.keyboardModifiers = 0;
		this.keyboardKeyPressed = new IntOpenHashSet();
		this.mouseLocalX = 0;
		this.mouseLocalY = 0;
		this.mouseButtonPressed = new IntOpenHashSet();
	}

	public W getWidget() {
		return widget;
	}

	public M getModel() {
		return widget.getModel();
	}

	public V getView() {
		return widget.getView();
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public KeyboardContext getKeyboardContext() {
		return new KeyboardContext(keyboardModifiers, IntSets.unmodifiable(keyboardKeyPressed));
	}

	public MouseContext getMouseContext() {
		return new MouseContext(mouseLocalX, mouseLocalY, IntSets.unmodifiable(mouseButtonPressed));
	}

	public void externalDraw(Minecraft minecraft, double partialTicks) {
		if (getView().isVisible()) {
			GlStateManager.pushMatrix();
			GlStateManager.translated(x, y, 0.0D);
			DrawContext drawContext = new DrawContext(minecraft, partialTicks, getKeyboardContext(), getMouseContext());
			widget.draw(drawContext);
			GlStateManager.popMatrix();
		}
	}

	public void externalKeyboardKey(int key, boolean pressed, int modifiers) {
		keyboardModifiers = modifiers;
		if (pressed)
			keyboardKeyPressed.add(key);
		else
			keyboardKeyPressed.remove(key);

		if (getView().isEnable()) {
			InputContext inputContext = new InputContext(getKeyboardContext(), getMouseContext());
			widget.onKeyboardKey(inputContext, key, pressed);
		}
	}

	public void externalKeyboardChar(char input) {
		if (getView().isEnable()) {
			InputContext inputContext = new InputContext(getKeyboardContext(), getMouseContext());
			widget.onKeyboardChar(inputContext, input);
		}
	}

	public void externalMouseButton(int button, boolean pressed) {
		if (pressed)
			mouseButtonPressed.add(button);
		else
			mouseButtonPressed.remove(button);

		if (getView().isEnable()) {
			InputContext inputContext = new InputContext(getKeyboardContext(), getMouseContext());
			widget.onMouseButton(inputContext, button, pressed);
		}
	}

	public void externalMousePosition(double globalX, double globalY) {
		double localX = globalX - x;
		double localY = globalY - y;
		double deltaX = localX - mouseLocalX;
		double deltaY = localY - mouseLocalY;
		mouseLocalX = localX;
		mouseLocalY = localY;
		if (deltaX == 0.0D && deltaY == 0.0D)
			return;

		if (getView().isEnable()) {
			InputContext inputContext = new InputContext(getKeyboardContext(), getMouseContext());
			widget.onMouseMove(inputContext, deltaX, deltaY);
		}
	}

	public void externalMouseWheel(double delta) {
		if (delta == 0.0D)
			return;

		if (getView().isEnable()) {
			InputContext inputContext = new InputContext(getKeyboardContext(), getMouseContext());
			widget.onMouseWheel(inputContext, delta);
		}
	}
}
