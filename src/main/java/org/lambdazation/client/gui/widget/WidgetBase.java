package org.lambdazation.client.gui.widget;

import org.lambdazation.client.gui.widget.context.KeyboardContext;
import org.lambdazation.client.gui.widget.context.MouseContext;
import org.lambdazation.client.gui.widget.model.ModelBase;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lambdazation.client.gui.widget.view.ViewBase;

@OnlyIn(Dist.CLIENT)
public class WidgetBase<M extends ModelBase, V extends ViewBase<M>> {
	private final M model;
	private final V view;

	public WidgetBase(M model, V view) {
		this.model = model;
		this.view = view;
	}

	public M getModel() {
		return model;
	}

	public V getView() {
		return view;
	}

	public void draw(ViewBase.DrawContext ctx) {
		view.draw(ctx, model);
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

	public static final class InputContext {
		public final KeyboardContext keyboardContext;
		public final MouseContext mouseContext;

		public InputContext(KeyboardContext keyboardContext, MouseContext mouseContext) {
			this.keyboardContext = keyboardContext;
			this.mouseContext = mouseContext;
		}

		public InputContext translate(double x, double y) {
			return new InputContext(keyboardContext, mouseContext.translate(x, y));
		}
	}

	public enum Action {
		CONTINUE(false, false), HANDLE(true, false), FOCUS(true, true), UNFOCUS(true, true);

		public final boolean handleInput;
		public final boolean changeFocus;

		Action(boolean handleInput, boolean changeFocus) {
			this.handleInput = handleInput;
			this.changeFocus = changeFocus;
		}
	}
}
