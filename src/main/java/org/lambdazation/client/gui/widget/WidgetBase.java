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

	public void onMouseButton(double globalX, double globalY, int button, boolean pressed) {

	}

	public void onMouseMove(double globalX, double globalY, double deltaX, double deltaY) {

	}

	public void onMouseWheel(double globalX, double globalY, double delta) {

	}
}
