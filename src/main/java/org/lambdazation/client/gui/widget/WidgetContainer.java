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

	@Override
	public Action onKeyboardKey(InputContext ctx, int key, boolean pressed) {
		Action action = Action.CONTINUE;
		if (!action.handleInput && getView().hasFocus()) {
			ViewContainer.Component component = getView().getFocus().asJust().value();
			if (component.getWidget().getView().isEnable()) {
				action = component.getWidget().onKeyboardKey(ctx.translate(component.getX(), component.getY()), key, pressed);
				handleFocusAction(component, action);
			}
		}
		List<ViewContainer.Component> components = getView().getComponents();
		ListIterator<ViewContainer.Component> iterator = components.listIterator(components.size());
		while (!action.handleInput && iterator.hasPrevious()) {
			ViewContainer.Component component = iterator.previous();
			if (!component.getWidget().getView().isFocused() && component.getWidget().getView().isEnable()) {
				action = component.getWidget().onKeyboardKey(ctx.translate(component.getX(), component.getY()), key, pressed);
				handleFocusAction(component, action);
			}
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
			if (component.getWidget().getView().isEnable()) {
				action = component.getWidget().onKeyboardChar(ctx.translate(component.getX(), component.getY()), input);
				handleFocusAction(component, action);
			}
		}
		List<ViewContainer.Component> components = getView().getComponents();
		ListIterator<ViewContainer.Component> iterator = components.listIterator(components.size());
		while (!action.handleInput && iterator.hasPrevious()) {
			ViewContainer.Component component = iterator.previous();
			if (!component.getWidget().getView().isFocused() && component.getWidget().getView().isEnable()) {
				action = component.getWidget().onKeyboardChar(ctx.translate(component.getX(), component.getY()), input);
				handleFocusAction(component, action);
			}
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
			if (component.getWidget().getView().isEnable()) {
				action = component.getWidget().onMouseButton(ctx.translate(component.getX(), component.getY()), button, pressed);
				handleFocusAction(component, action);
			}
		}
		List<ViewContainer.Component> components = getView().getComponents();
		ListIterator<ViewContainer.Component> iterator = components.listIterator(components.size());
		while (!action.handleInput && iterator.hasPrevious()) {
			ViewContainer.Component component = iterator.previous();
			if (!component.getWidget().getView().isFocused() && component.getWidget().getView().isEnable()) {
				action = component.getWidget().onMouseButton(ctx.translate(component.getX(), component.getY()), button, pressed);
				handleFocusAction(component, action);
			}
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
			if (component.getWidget().getView().isEnable()) {
				action = component.getWidget().onMouseMove(ctx.translate(component.getX(), component.getY()), deltaX, deltaY);
				handleFocusAction(component, action);
			}
		}
		List<ViewContainer.Component> components = getView().getComponents();
		ListIterator<ViewContainer.Component> iterator = components.listIterator(components.size());
		while (!action.handleInput && iterator.hasPrevious()) {
			ViewContainer.Component component = iterator.previous();
			if (!component.getWidget().getView().isFocused() && component.getWidget().getView().isEnable()) {
				action = component.getWidget().onMouseMove(ctx.translate(component.getX(), component.getY()), deltaX, deltaY);
				handleFocusAction(component, action);
			}
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
			if (component.getWidget().getView().isEnable()) {
				action = component.getWidget().onMouseWheel(ctx.translate(component.getX(), component.getY()), delta);
				handleFocusAction(component, action);
			}
		}
		List<ViewContainer.Component> components = getView().getComponents();
		ListIterator<ViewContainer.Component> iterator = components.listIterator(components.size());
		while (!action.handleInput && iterator.hasPrevious()) {
			ViewContainer.Component component = iterator.previous();
			if (!component.getWidget().getView().isFocused() && component.getWidget().getView().isEnable()) {
				action = component.getWidget().onMouseWheel(ctx.translate(component.getX(), component.getY()), delta);
				handleFocusAction(component, action);
			}
		}
		if (!action.handleInput)
			action = super.onMouseWheel(ctx, delta);
		return action;
	}
}
