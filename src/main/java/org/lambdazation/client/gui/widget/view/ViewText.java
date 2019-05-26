package org.lambdazation.client.gui.widget.view;

import org.lambdazation.client.gui.widget.model.ModelText;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ViewText<M extends ModelText> extends ViewBase<M> {
	private int textColor = 0xFFFFFFFF;
	private int backgroundColor = 0xFF000000;
	private int cursorColor = 0xFFFFFFFF;
	private int selectionTextColor = 0xFF000000;
	private int selectionBackgroundColor = 0xFF0000FF;

	private int selectedFromIndex;
	private int selectedToIndex;
	private int cursorIndex;

	@Override
	public void draw(DrawContext ctx, M model) {
		super.draw(ctx, model);
		// TODO NYI
	}
}
