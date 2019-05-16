package org.lambdazation.client.gui.widget.context;

import it.unimi.dsi.fastutil.ints.IntSet;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class MouseContext {
	public final double localX;
	public final double localY;
	public final IntSet buttonPressed;

	public MouseContext(double localX, double localY, IntSet buttonPressed) {
		this.localX = localX;
		this.localY = localY;
		this.buttonPressed = buttonPressed;
	}

	public MouseContext translate(double x, double y) {
		return new MouseContext(localX - x, localY - y, buttonPressed);
	}
}
