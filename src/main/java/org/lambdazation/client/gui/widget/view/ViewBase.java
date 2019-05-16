package org.lambdazation.client.gui.widget.view;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lambdazation.client.gui.widget.WidgetBase;
import org.lambdazation.client.gui.widget.context.KeyboardContext;
import org.lambdazation.client.gui.widget.context.MouseContext;
import org.lambdazation.client.gui.widget.model.ModelBase;

@OnlyIn(Dist.CLIENT)
public class ViewBase<M extends ModelBase> {
	private boolean isFocused;

	public ViewBase() {
		this.isFocused = false;
	}

	public boolean isFocused() {
		return isFocused;
	}

	public void onFocused() {
		isFocused = true;
	}

	public void onUnfocused() {
		isFocused = false;
	}

	public void draw(DrawContext ctx, M model) {

	}

	public static final class DrawContext {
		public final double partialTicks;
		public final KeyboardContext keyboardContext;
		public final MouseContext mouseContext;

		public DrawContext(double partialTicks, KeyboardContext keyboardContext, MouseContext mouseContext) {
			this.partialTicks = partialTicks;
			this.keyboardContext = keyboardContext;
			this.mouseContext = mouseContext;
		}

		public DrawContext translate(double x, double y) {
			return new DrawContext(partialTicks, keyboardContext.translate(x, y), mouseContext.translate(x, y));
		}
	}
}
