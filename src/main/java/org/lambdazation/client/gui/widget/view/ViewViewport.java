package org.lambdazation.client.gui.widget.view;

import org.lambdazation.client.gui.widget.WidgetBase;
import org.lambdazation.client.gui.widget.model.ModelBase;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ViewViewport<M extends ModelBase> extends ViewWrapper<M> {
	private double viewX;
	private double viewY;
	private double viewWidth;
	private double viewHeight;

	public ViewViewport(WidgetBase<?, ?> component, double viewX, double viewY, double viewWidth, double viewHeight) {
		super(component);
		this.viewX = viewX;
		this.viewY = viewY;
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
	}

	public double getViewX() {
		return viewX;
	}

	public void setViewX(double viewX) {
		this.viewX = viewX;
	}

	public double getViewY() {
		return viewY;
	}

	public void setViewY(double viewY) {
		this.viewY = viewY;
	}

	public double getViewWidth() {
		return viewWidth;
	}

	public void setViewWidth(double viewWidth) {
		this.viewWidth = viewWidth;
	}

	public double getViewHeight() {
		return viewHeight;
	}

	public void setViewHeight(double viewHeight) {
		this.viewHeight = viewHeight;
	}

	@Override
	public void drawComponent(WidgetBase<?, ?> component, DrawContext ctx, M model) {
		super.drawComponent(component, ctx, model);

		// TODO NYI
	}
}
