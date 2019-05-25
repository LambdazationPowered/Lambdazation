package org.lambdazation.client.gui.widget.view;

import org.lambdazation.client.gui.widget.model.ModelTextField;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ViewTextField<M extends ModelTextField> extends ViewBase<M> {
	@Override
	public void draw(DrawContext ctx, M model) {
		super.draw(ctx, model);
		// TODO NYI
	}
}
