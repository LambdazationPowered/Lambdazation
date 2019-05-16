package org.lambdazation.client.gui.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lambdazation.common.util.data.Maybe;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WidgetContainer<M extends ModelBase> extends WidgetBase<M> {
	private final List<Component> components;
	private Maybe<Component> focus;

	public WidgetContainer(M model) {
		super(model);

		this.components = new ArrayList<>();
		this.focus = Maybe.ofNothing();
	}

	protected Component addComponentInternal(WidgetBase<?> widget, double x, double y) {
		Component component = new Component(widget, x, y);
		components.add(component);
		return component;
	}

	protected void removeComponentInternal(Component component) {
		if (component.widget.isFocusedInternal())
			setFocusInternal(Maybe.ofNothing());
		components.remove(component);
	}

	protected Maybe<Component> getFocusInternal() {
		return focus;
	}

	protected void setFocusInternal(Maybe<Component> focus) {
		if (this.focus.isJust()) {
			Component component = this.focus.asJust().value();
			this.focus = null;
			component.widget.onUnfocusedInternal();
		}
		if (focus.isJust()) {
			this.focus = focus;
			Component component = focus.asJust().value();
			component.widget.onFoucsedInternal();
		}
	}

	@Override
	protected void onFoucsedInternal() {
		super.onFoucsedInternal();
	}

	@Override
	protected void onUnfocusedInternal() {
		super.onUnfocusedInternal();
		setFocusInternal(Maybe.ofNothing());
	}

	@Override
	public void draw(DrawContext ctx) {
		super.draw(ctx);
		ListIterator<Component> iterator = components.listIterator();
		while (iterator.hasNext()) {
			Component component = iterator.next();
			GlStateManager.pushMatrix();
			GlStateManager.translated(component.x, component.y, 0.0D);
			component.widget.draw(ctx.translate(component.x, component.y));
			GlStateManager.popMatrix();
		}
	}

	@Override
	public Action onKeyboardKey(InputContext ctx, int key, boolean pressed) {
		Action action = Action.CONTINUE;
		// TODO NYI
		if (!action.handleInput)
			action = super.onKeyboardKey(ctx, key, pressed);
		return action;
	}

	@Override
	public Action onKeyboardChar(InputContext ctx, char input) {
		Action action = Action.CONTINUE;
		// TODO NYI
		if (!action.handleInput)
			action = super.onKeyboardChar(ctx, input);
		return action;
	}

	@Override
	public Action onMouseButton(InputContext ctx, int button, boolean pressed) {
		Action action = Action.CONTINUE;
		// TODO NYI
		if (!action.handleInput)
			action = super.onMouseButton(ctx, button, pressed);
		return action;
	}

	@Override
	public Action onMouseMove(InputContext ctx, double deltaX, double deltaY) {
		Action action = Action.CONTINUE;
		// TODO NYI
		if (!action.handleInput)
			action = super.onMouseMove(ctx, deltaX, deltaY);
		return action;
	}

	@Override
	public Action onMouseWheel(InputContext ctx, double delta) {
		Action action = Action.CONTINUE;
		// TODO NYI
		if (!action.handleInput)
			action = super.onMouseWheel(ctx, delta);
		return action;
	}

	protected static final class Component {
		protected final WidgetBase<?> widget;
		protected double x;
		protected double y;

		protected Component(final WidgetBase<?> widget, double x, double y) {
			this.widget = widget;
			this.x = x;
			this.y = y;
		}
	}
}
