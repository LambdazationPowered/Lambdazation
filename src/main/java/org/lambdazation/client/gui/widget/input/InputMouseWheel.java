package org.lambdazation.client.gui.widget.input;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InputMouseWheel extends InputBase {
	public final double delta;

	public InputMouseWheel(double delta) {
		this.delta = delta;
	}
}
