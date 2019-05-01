package org.lambdazation.client.gui.widget.input;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InputTick extends InputBase {
	public final double time;

	public InputTick(double time) {
		this.time = time;
	}
}
