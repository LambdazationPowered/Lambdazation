package org.lambdazation.client.gui.widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WidgetBase<M extends ModelBase> {
	public final M model;

	public WidgetBase(M model) {
		this.model = model;
	}

	public void draw() {

	}

	public void onKeyboardChar(int modifiers, char input) {

	}

	public void onKeyboardKey(int modifiers, int key, int scancode, boolean pressed) {

	}

	public void onMouseButton(double localX, double localY, int button, boolean pressed) {

	}

	public void onMouseMove(double localX, double localY, double deltaX, double deltaY) {

	}

	public void onMouseWheel(double localX, double localY, double delta) {

	}
}
