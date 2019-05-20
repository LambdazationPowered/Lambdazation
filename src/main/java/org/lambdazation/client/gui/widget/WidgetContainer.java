package org.lambdazation.client.gui.widget;

import java.util.List;
import java.util.ListIterator;

import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lambdazation.client.gui.widget.view.ViewContainer;
import org.lambdazation.common.util.data.Maybe;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WidgetContainer<M extends ModelBase, V extends ViewContainer<M>> extends WidgetBase<M, V> {
	public WidgetContainer(M model, V view) {
		super(model, view);
	}

	public void handleFocusAction(ViewContainer.Component component, Action action) {
		if (!component.isValid())
			return;
		switch (action) {
		case FOCUS:
			if (!component.getWidget().getView().isFocused())
				getView().setFocus(Maybe.ofJust(component));
			break;
		case UNFOCUS:
			if (component.getWidget().getView().isFocused())
				getView().setFocus(Maybe.ofNothing());
			break;
		default:
			break;
		}
	}

	public Action onKeyboardKeyComponent(ViewContainer.Component component, InputContext ctx, int key, boolean pressed) {
		Action action = Action.CONTINUE;
		if (component.getWidget().getView().isEnable()) {
			action = component.getWidget().onKeyboardKey(ctx.translate(component.getX(), component.getY()), key, pressed);
			handleFocusAction(component, action);
		}
		return action;
	}

	public Action onKeyboardCharComponent(ViewContainer.Component component, InputContext ctx, char input) {
		Action action = Action.CONTINUE;
		if (component.getWidget().getView().isEnable()) {
			action = component.getWidget().onKeyboardChar(ctx.translate(component.getX(), component.getY()), input);
			handleFocusAction(component, action);
		}
		return action;
	}

	public Action onMouseButtonComponent(ViewContainer.Component component, InputContext ctx, int button, boolean pressed) {
		Action action = Action.CONTINUE;
		if (component.getWidget().getView().isEnable()) {
			action = component.getWidget().onMouseButton(ctx.translate(component.getX(), component.getY()), button, pressed);
			handleFocusAction(component, action);
		}
		return action;
	}

	public Action onMouseMoveComponent(ViewContainer.Component component, InputContext ctx, double deltaX, double deltaY) {
		Action action = Action.CONTINUE;
		if (component.getWidget().getView().isEnable()) {
			action = component.getWidget().onMouseMove(ctx.translate(component.getX(), component.getY()), deltaX, deltaY);
			handleFocusAction(component, action);
		}
		return action;
	}

	public Action onMouseWheelComponent(ViewContainer.Component component, InputContext ctx, double delta) {
		Action action = Action.CONTINUE;
		if (component.getWidget().getView().isEnable()) {
			action = component.getWidget().onMouseWheel(ctx.translate(component.getX(), component.getY()), delta);
			handleFocusAction(component, action);
		}
		return action;
	}

	@Override
	public Action onKeyboardKey(InputContext ctx, int key, boolean pressed) {
		Action action = Action.CONTINUE;
		if (!action.handleInput && getView().hasFocus()) {
			ViewContainer.Component component = getView().getFocus().asJust().value();
			onKeyboardKeyComponent(component, ctx, key, pressed);
		}
		List<ViewContainer.Component> components = getView().getComponents();
		ListIterator<ViewContainer.Component> iterator = components.listIterator(components.size());
		while (!action.handleInput && iterator.hasPrevious()) {
			ViewContainer.Component component = iterator.previous();
			if (!component.getWidget().getView().isFocused())
				onKeyboardKeyComponent(component, ctx, key, pressed);
		}
		if (!action.handleInput)
			action = super.onKeyboardKey(ctx, key, pressed);
		return action;
	}

	@Override
	public Action onKeyboardChar(InputContext ctx, char input) {
		Action action = Action.CONTINUE;
		if (!action.handleInput && getView().hasFocus()) {
			ViewContainer.Component component = getView().getFocus().asJust().value();
			onKeyboardCharComponent(component, ctx, input);
		}
		List<ViewContainer.Component> components = getView().getComponents();
		ListIterator<ViewContainer.Component> iterator = components.listIterator(components.size());
		while (!action.handleInput && iterator.hasPrevious()) {
			ViewContainer.Component component = iterator.previous();
			if (!component.getWidget().getView().isFocused())
				onKeyboardCharComponent(component, ctx, input);
		}
		if (!action.handleInput)
			action = super.onKeyboardChar(ctx, input);
		return action;
	}

	@Override
	public Action onMouseButton(InputContext ctx, int button, boolean pressed) {
		Action action = Action.CONTINUE;
		if (!action.handleInput && getView().hasFocus()) {
			ViewContainer.Component component = getView().getFocus().asJust().value();
			onMouseButtonComponent(component, ctx, button, pressed);
		}
		List<ViewContainer.Component> components = getView().getComponents();
		ListIterator<ViewContainer.Component> iterator = components.listIterator(components.size());
		while (!action.handleInput && iterator.hasPrevious()) {
			ViewContainer.Component component = iterator.previous();
			if (!component.getWidget().getView().isFocused())
				onMouseButtonComponent(component, ctx, button, pressed);
		}
		if (!action.handleInput)
			action = super.onMouseButton(ctx, button, pressed);
		return action;
	}

	@Override
	public Action onMouseMove(InputContext ctx, double deltaX, double deltaY) {
		Action action = Action.CONTINUE;
		if (!action.handleInput && getView().hasFocus()) {
			ViewContainer.Component component = getView().getFocus().asJust().value();
			onMouseMoveComponent(component, ctx, deltaX, deltaY);
		}
		List<ViewContainer.Component> components = getView().getComponents();
		ListIterator<ViewContainer.Component> iterator = components.listIterator(components.size());
		while (!action.handleInput && iterator.hasPrevious()) {
			ViewContainer.Component component = iterator.previous();
			if (!component.getWidget().getView().isFocused())
				onMouseMoveComponent(component, ctx, deltaX, deltaY);
		}
		if (!action.handleInput)
			action = super.onMouseMove(ctx, deltaX, deltaY);
		return action;
	}

	@Override
	public Action onMouseWheel(InputContext ctx, double delta) {
		Action action = Action.CONTINUE;
		if (!action.handleInput && getView().hasFocus()) {
			ViewContainer.Component component = getView().getFocus().asJust().value();
			onMouseWheelComponent(component, ctx, delta);
		}
		List<ViewContainer.Component> components = getView().getComponents();
		ListIterator<ViewContainer.Component> iterator = components.listIterator(components.size());
		while (!action.handleInput && iterator.hasPrevious()) {
			ViewContainer.Component component = iterator.previous();
			if (!component.getWidget().getView().isFocused())
				onMouseWheelComponent(component, ctx, delta);
		}
		if (!action.handleInput)
			action = super.onMouseWheel(ctx, delta);
		return action;
	}
}
