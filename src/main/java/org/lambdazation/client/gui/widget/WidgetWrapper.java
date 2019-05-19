package org.lambdazation.client.gui.widget;

import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lambdazation.client.gui.widget.view.ViewWrapper;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WidgetWrapper<M extends ModelBase, V extends ViewWrapper<M>> extends WidgetBase<M, V> {
	public WidgetWrapper(M model, V view) {
		super(model, view);
	}

	@Override
	public Action onKeyboardKey(InputContext ctx, int key, boolean pressed) {
		Action action = Action.CONTINUE;
		if (!action.handleInput) {
			WidgetBase<?, ?> component = getView().getComponent();
			if (component.getView().isEnable())
				action = component.onKeyboardKey(ctx, key, pressed);
		}
		if (!action.handleInput)
			action = super.onKeyboardKey(ctx, key, pressed);
		return action;
	}

	@Override
	public Action onKeyboardChar(InputContext ctx, char input) {
		Action action = Action.CONTINUE;
		if (!action.handleInput) {
			WidgetBase<?, ?> component = getView().getComponent();
			if (component.getView().isEnable())
				action = component.onKeyboardChar(ctx, input);
		}
		if (!action.handleInput)
			action = super.onKeyboardChar(ctx, input);
		return action;
	}

	@Override
	public Action onMouseButton(InputContext ctx, int button, boolean pressed) {
		Action action = Action.CONTINUE;
		if (!action.handleInput) {
			WidgetBase<?, ?> component = getView().getComponent();
			if (component.getView().isEnable())
				action = component.onMouseButton(ctx, button, pressed);
		}
		if (!action.handleInput)
			action = super.onMouseButton(ctx, button, pressed);
		return action;
	}

	@Override
	public Action onMouseMove(InputContext ctx, double deltaX, double deltaY) {
		Action action = Action.CONTINUE;
		if (!action.handleInput) {
			WidgetBase<?, ?> component = getView().getComponent();
			if (component.getView().isEnable())
				action = component.onMouseMove(ctx, deltaX, deltaY);
		}
		if (!action.handleInput)
			action = super.onMouseMove(ctx, deltaX, deltaY);
		return action;
	}

	@Override
	public Action onMouseWheel(InputContext ctx, double delta) {
		Action action = Action.CONTINUE;
		if (!action.handleInput) {
			WidgetBase<?, ?> component = getView().getComponent();
			if (component.getView().isEnable())
				action = component.onMouseWheel(ctx, delta);
		}
		if (!action.handleInput)
			action = super.onMouseWheel(ctx, delta);
		return action;
	}
}
