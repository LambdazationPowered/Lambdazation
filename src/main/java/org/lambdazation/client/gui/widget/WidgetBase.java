package org.lambdazation.client.gui.widget;

import org.lambdazation.client.gui.widget.model.ModelBase;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WidgetBase<M extends ModelBase> {
	public final M model;

	public WidgetBase(M model) {
		this.model = model;
	}

	public void draw(DrawContext ctx) {

	}

	public Action onKeyboardKey(KeyboardContext ctx, int key, boolean pressed) {
		return Action.NONE;
	}

	public Action onKeyboardChar(KeyboardContext ctx, char input) {
		return Action.NONE;
	}

	public Action onMouseButton(MouseContext ctx, int button, boolean pressed) {
		return Action.NONE;
	}

	public Action onMouseMove(MouseContext ctx, double deltaX, double deltaY) {
		return Action.NONE;
	}

	public Action onMouseWheel(MouseContext ctx, double delta) {
		return Action.NONE;
	}

	public static final class DrawContext {
		public final double partialTicks;
		public final KeyboardContext keyboardContext;
		public final MouseContext mouseContext;

		public DrawContext(double partialTicks, KeyboardContext keyboardContext, MouseContext mouseContext) {
			this.partialTicks = partialTicks;
			this.keyboardContext = keyboardContext;
			this.mouseContext = mouseContext;
		}
	}

	public static final class KeyboardContext {
		public final int modifiers;
		public final IntSet keyPressed;

		public KeyboardContext(int modifiers, IntSet keyPressed) {
			this.modifiers = modifiers;
			this.keyPressed = keyPressed;
		}
	}

	public static final class MouseContext {
		public final double localX;
		public final double localY;
		public final IntSet buttonPressed;

		public MouseContext(double localX, double localY, IntSet buttonPressed) {
			this.localX = localX;
			this.localY = localY;
			this.buttonPressed = buttonPressed;
		}
	}

	public static enum Action {
		NONE, FOCUS, UNFOCUS
	}
}
