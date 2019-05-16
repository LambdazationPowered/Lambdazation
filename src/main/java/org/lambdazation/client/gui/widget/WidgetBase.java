package org.lambdazation.client.gui.widget;

import org.lambdazation.client.gui.widget.model.ModelBase;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WidgetBase<M extends ModelBase> {
	private final M model;
	private boolean isFocused;

	public WidgetBase(M model) {
		this.model = model;
		this.isFocused = false;
	}

	protected M getModelInternal() {
		return model;
	}

	protected boolean isFocusedInternal() {
		return isFocused;
	}

	protected void onFoucsedInternal() {
		isFocused = true;
	}

	protected void onUnfocusedInternal() {
		isFocused = false;
	}

	public void draw(DrawContext ctx) {

	}

	public Action onKeyboardKey(InputContext ctx, int key, boolean pressed) {
		return Action.CONTINUE;
	}

	public Action onKeyboardChar(InputContext ctx, char input) {
		return Action.CONTINUE;
	}

	public Action onMouseButton(InputContext ctx, int button, boolean pressed) {
		return Action.CONTINUE;
	}

	public Action onMouseMove(InputContext ctx, double deltaX, double deltaY) {
		return Action.CONTINUE;
	}

	public Action onMouseWheel(InputContext ctx, double delta) {
		return Action.CONTINUE;
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

		public DrawContext translate(double x, double y) {
			return new DrawContext(partialTicks, keyboardContext.translate(x, y), mouseContext.translate(x, y));
		}
	}

	public static final class InputContext {
		public final KeyboardContext keyboardContext;
		public final MouseContext mouseContext;

		public InputContext(KeyboardContext keyboardContext, MouseContext mouseContext) {
			this.keyboardContext = keyboardContext;
			this.mouseContext = mouseContext;
		}

		public InputContext translate(double x, double y) {
			return new InputContext(keyboardContext.translate(x, y), mouseContext.translate(x, y));
		}
	}

	public static final class KeyboardContext {
		public final int modifiers;
		public final IntSet keyPressed;

		public KeyboardContext(int modifiers, IntSet keyPressed) {
			this.modifiers = modifiers;
			this.keyPressed = keyPressed;
		}

		public KeyboardContext translate(double x, double y) {
			return new KeyboardContext(modifiers, keyPressed);
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

		public MouseContext translate(double x, double y) {
			return new MouseContext(localX - x, localY - y, buttonPressed);
		}
	}

	public static enum Action {
		CONTINUE(false, false), HANDLE(true, false), FOCUS(true, true), UNFOCUS(true, true);

		public final boolean handleInput;
		public final boolean changeFocus;

		Action(boolean handleInput, boolean changeFocus) {
			this.handleInput = handleInput;
			this.changeFocus = changeFocus;
		}
	}
}
