package org.lambdazation.client.gui.widget.input;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InputChar extends InputBase {
	public final char input;
	public final int modifiers;

	public InputChar(char input, int modifiers) {
		this.input = input;
		this.modifiers = modifiers;
	}
}
