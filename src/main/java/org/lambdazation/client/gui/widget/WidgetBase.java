package org.lambdazation.client.gui.widget;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class WidgetBase {
	public interface Model {

	}

	public interface View<M extends Model> {
		void draw();
	}

	public interface Controller<M extends Model> {
		void onKeyboardChar(int modifiers, char input);

		void onKeyboardKey(int modifiers, int key, int scancode, boolean pressed);

		void onMouseButton(double globalX, double globalY, int button, boolean pressed);

		void onMouseMove(double globalX, double globalY, double deltaX, double deltaY);

		void onMouseWheel(double globalX, double globalY, double delta);
	}
}
