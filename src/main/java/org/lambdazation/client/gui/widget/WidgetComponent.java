package org.lambdazation.client.gui.widget;

import org.lambdazation.client.gui.widget.input.InputBase;
import org.lambdazation.common.util.data.Maybe;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class WidgetComponent {
	public final Maybe<WidgetComponent> parent;

	public WidgetComponent(Maybe<WidgetComponent> parent) {
		this.parent = parent;
	}

	public abstract void propagate(InputBase input);
}
