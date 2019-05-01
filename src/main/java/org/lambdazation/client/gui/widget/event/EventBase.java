package org.lambdazation.client.gui.widget.event;

import org.lambdazation.client.gui.widget.WidgetComponent;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class EventBase<W extends WidgetComponent> {
	public final W target;

	public EventBase(W target) {
		this.target = target;
	}
}
