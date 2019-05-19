package org.lambdazation.client.gui.widget.view;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lambdazation.client.gui.widget.WidgetBase;
import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lambdazation.common.util.data.Maybe;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

@OnlyIn(Dist.CLIENT)
public class ViewContainer<M extends ModelBase> extends ViewBase<M> {
	private final List<Component> components;
	private Maybe<Component> focus;

	public ViewContainer() {
		this.components = new CopyOnWriteArrayList<>();
		this.focus = Maybe.ofNothing();
	}

	public List<Component> getComponents() {
		return components;
	}

	public Component addComponent(WidgetBase<?, ?> widget, double x, double y) {
		Component component = new Component(widget, x, y, true);
		components.add(component);
		return component;
	}

	public void removeComponent(Component component) {
		if (component.getWidget().getView().isFocused())
			setFocus(Maybe.ofNothing());
		components.remove(component);
		component.invalidate();
	}

	public Maybe<Component> getFocus() {
		return focus;
	}

	public void setFocus(Maybe<Component> focus) {
		if (this.focus.isJust()) {
			Component component = this.focus.asJust().value();
			this.focus = null;
			component.getWidget().getView().onUnfocused();
		}
		if (focus.isJust()) {
			Component component = focus.asJust().value();
			this.focus = focus;
			component.getWidget().getView().onFocused();
		}
	}

	public boolean hasFocus() {
		return focus.isJust();
	}

	@Override
	public void onFocused() {
		super.onFocused();
	}

	@Override
	public void onUnfocused() {
		super.onUnfocused();

		setFocus(Maybe.ofNothing());
	}

	public void drawComponent(DrawContext ctx, M model, Component component) {
		if (component.getWidget().getView().isVisible()) {
			GlStateManager.pushMatrix();
			GlStateManager.translated(component.getX(), component.getY(), 0.0D);
			component.getWidget().draw(ctx.translate(component.getX(), component.getY()));
			GlStateManager.popMatrix();
		}
	}

	@Override
	public void draw(DrawContext ctx, M model) {
		super.draw(ctx, model);

		ListIterator<Component> iterator = components.listIterator();
		while (iterator.hasNext()) {
			Component component = iterator.next();
			if (!component.getWidget().getView().isFocused())
				drawComponent(ctx, model, component);
		}
		if (hasFocus()) {
			Component component = focus.asJust().value();
			drawComponent(ctx, model, component);
		}
	}

	public static final class Component {
		private final WidgetBase<?, ?> widget;
		private double x;
		private double y;
		private boolean isValid;

		Component(final WidgetBase<?, ?> widget, double x, double y, boolean isValid) {
			this.widget = widget;
			this.x = x;
			this.y = y;
			this.isValid = isValid;
		}

		public WidgetBase<?, ?> getWidget() {
			return widget;
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

		public boolean isValid() {
			return isValid;
		}

		public void invalidate() {
			this.isValid = false;
		}
	}
}
