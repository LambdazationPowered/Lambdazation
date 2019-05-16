package org.lambdazation.client.gui.widget.view;

import net.minecraft.util.ResourceLocation;
import org.lambdazation.client.gui.widget.model.ModelBase;

public class ViewButton<M extends ModelBase> extends ViewBase<M> {
	private ResourceLocation resource;
	private double width;
	private double height;

	public ViewButton(ResourceLocation resource, double width, double height) {
		this.resource = resource;
		this.width = width;
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	@Override
	public void draw(DrawContext ctx, M model) {
		super.draw(ctx, model);
		// TODO NYI
	}
}
