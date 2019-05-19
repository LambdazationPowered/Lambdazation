package org.lambdazation.client.gui.widget;

import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lambdazation.client.gui.widget.view.ViewLabel;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WidgetLabel<M extends ModelBase, V extends ViewLabel<M>> extends WidgetBase<M, V> {
	public WidgetLabel(M model, V view) {
		super(model, view);
	}
}
