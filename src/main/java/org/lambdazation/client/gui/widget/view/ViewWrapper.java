package org.lambdazation.client.gui.widget.view;

import org.lambdazation.client.gui.widget.WidgetBase;
import org.lambdazation.client.gui.widget.model.ModelBase;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ViewWrapper<M extends ModelBase> extends ViewBase<M> {
	private WidgetBase<?, ?> component;

	public ViewWrapper(WidgetBase<?, ?> component) {
		this.component = component;
	}

	public WidgetBase<?, ?> getComponent() {
		return component;
	}

	public void setComponent(WidgetBase<?, ?> component) {
		this.component = component;
	}

	public void drawComponent(DrawContext ctx, M model, WidgetBase<?, ?> component) {
		if (component.getView().isVisible())
			component.draw(ctx);
	}

	@Override
	public void onFocused() {
		super.onFocused();

		component.getView().onFocused();
	}

	@Override
	public void onUnfocused() {
		super.onUnfocused();

		component.getView().onUnfocused();
	}

	@Override
	public void draw(DrawContext ctx, M model) {
		super.draw(ctx, model);

		drawComponent(ctx, model, component);
	}
}
