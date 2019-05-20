package org.lambdazation.client.gui.widget;

import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lambdazation.client.gui.widget.view.ViewViewport;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WidgetViewport<M extends ModelBase, V extends ViewViewport<M>> extends WidgetWrapper<M, V> {
	public WidgetViewport(M model, V view) {
		super(model, view);
	}

	// TODO NYI

	@Override
	public Action onKeyboardKeyComponent(WidgetBase<?, ?> component, InputContext ctx, int key, boolean pressed) {
		Action action = Action.CONTINUE;
		action = super.onKeyboardKeyComponent(component, ctx.translate(-getView().getViewX(), -getView().getViewY()), key, pressed);
		return action;
	}

	@Override
	public Action onKeyboardCharComponent(WidgetBase<?, ?> component, InputContext ctx, char input) {
		Action action = Action.CONTINUE;
		action = super.onKeyboardCharComponent(component, ctx.translate(-getView().getViewX(), -getView().getViewY()), input);
		return action;
	}

	@Override
	public Action onMouseButtonComponent(WidgetBase<?, ?> component, InputContext ctx, int button, boolean pressed) {
		Action action = Action.CONTINUE;
		action = super.onMouseButtonComponent(component, ctx.translate(-getView().getViewX(), -getView().getViewY()), button, pressed);
		return action;
	}

	@Override
	public Action onMouseMoveComponent(WidgetBase<?, ?> component, InputContext ctx, double deltaX, double deltaY) {
		Action action = Action.CONTINUE;
		action = super.onMouseMoveComponent(component, ctx.translate(-getView().getViewX(), -getView().getViewY()), deltaX, deltaY);
		return action;
	}

	@Override
	public Action onMouseWheelComponent(WidgetBase<?, ?> component, InputContext ctx, double delta) {
		Action action = Action.CONTINUE;
		action = super.onMouseWheelComponent(component, ctx.translate(-getView().getViewX(), -getView().getViewY()), delta);
		return action;
	}
}
