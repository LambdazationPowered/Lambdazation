package org.lambdazation.client.gui.widget;

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

	public void onKeyboardKey(KeyboardContext ctx, int key, boolean pressed) {

	}

	public void onKeyboardChar(KeyboardContext ctx, char input) {

	}

	public void onMouseButton(MouseContext ctx, int button, boolean pressed) {

	}

	public void onMouseMove(MouseContext ctx, double deltaX, double deltaY) {

	}

	public void onMouseWheel(MouseContext ctx, double delta) {

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
}
