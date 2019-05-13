package org.lambdazation.client.gui.widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class WidgetBase<M extends ModelBase> {
	public abstract void draw();

	public abstract void onKeyboardChar(int modifiers, char input);

	public abstract void onKeyboardKey(int modifiers, int key, int scancode, boolean pressed);

	public abstract void onMouseButton(double globalX, double globalY, int button, boolean pressed);

	public abstract void onMouseMove(double globalX, double globalY, double deltaX, double deltaY);

	public abstract void onMouseWheel(double globalX, double globalY, double delta);
}
