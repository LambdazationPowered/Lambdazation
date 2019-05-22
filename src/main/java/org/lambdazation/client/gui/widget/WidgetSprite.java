package org.lambdazation.client.gui.widget;

import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lambdazation.client.gui.widget.view.ViewSprite;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WidgetSprite<M extends ModelBase, V extends ViewSprite<M>> extends WidgetBase<M, V> {
	public WidgetSprite(M model, V view) {
		super(model, view);
	}
}
