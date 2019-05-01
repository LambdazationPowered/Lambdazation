package org.lambdazation.client.gui.widget.input;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class InputKeyboardKey extends InputBase {
	public final int key;
	public final int scancode;
	public final boolean pressed;
	public final int modifiers;

	public InputKeyboardKey(int key, int scancode, boolean pressed, int modifiers) {
		this.key = key;
		this.scancode = scancode;
		this.pressed = pressed;
		this.modifiers = modifiers;
	}
}
