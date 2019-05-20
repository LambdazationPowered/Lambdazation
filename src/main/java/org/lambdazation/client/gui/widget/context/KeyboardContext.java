package org.lambdazation.client.gui.widget.context;

import it.unimi.dsi.fastutil.ints.IntSet;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class KeyboardContext {
	public final int modifiers;
	public final IntSet keyPressed;

	public KeyboardContext(int modifiers, IntSet keyPressed) {
		this.modifiers = modifiers;
		this.keyPressed = keyPressed;
	}
}
