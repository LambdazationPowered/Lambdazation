package org.lambdazation.client.gui.widget.input;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InputMouseMove extends InputBase {
	public final double globalX;
	public final double globalY;
	public final double deltaX;
	public final double deltaY;

	public InputMouseMove(double globalX, double globalY, double deltaX, double deltaY) {
		this.globalX = globalX;
		this.globalY = globalY;
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
}
