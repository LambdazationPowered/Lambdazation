package org.lambdazation.client.gui.widget.view;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lambdazation.client.gui.widget.context.KeyboardContext;
import org.lambdazation.client.gui.widget.context.MouseContext;
import org.lambdazation.client.gui.widget.model.ModelBase;

@OnlyIn(Dist.CLIENT)
public class ViewBase<M extends ModelBase> {
	private boolean isFocused;
	private boolean isEnable;
	private boolean isVisible;

	public ViewBase() {
		this.isFocused = false;
		this.isEnable = true;
		this.isVisible = true;
	}

	public boolean isFocused() {
		return isFocused;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
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
