package org.lambdazation.client.gui.widget.input;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InputMouseButton extends InputBase {
	public final int globalX;
	public final int globalY;
	public final int button;
	public final boolean pressed;

	public InputMouseButton(int globalX, int globalY, int button, boolean pressed) {
		this.globalX = globalX;
		this.globalY = globalY;
		this.button = button;
		this.pressed = pressed;
	}
}
