package org.lambdazation.client.gui.widget.view;

import org.lambdazation.client.gui.widget.model.ModelBase;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ViewLabel<M extends ModelBase> extends ViewBase<M> {
	private String text;
	private int textColor;

	public ViewLabel(String text) {
		this(text, 0xFFE0E0E0);
	}

	public ViewLabel(String text, int textColor) {
		this.text = text;
		this.textColor = textColor;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void draw(DrawContext ctx, M model) {
		super.draw(ctx, model);

		ctx.minecraft.fontRenderer.drawStringWithShadow(text, 0.0F, 0.0F, textColor);
	}
}
