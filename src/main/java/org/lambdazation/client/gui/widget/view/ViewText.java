package org.lambdazation.client.gui.widget.view;

import org.lambdazation.client.gui.widget.model.ModelText;
import org.lambdazation.common.util.data.Maybe;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.TexturedGlyph;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ViewText<M extends ModelText> extends ViewBase<M> {
	private Maybe<Font> preferredFont;
	private int textColor;
	private int backgroundColor;
	private int selectionTextColor;
	private int selectionBackgroundColor;
	private int cursorColor;
	private int selectedFromIndex;
	private int selectedToIndex;
	private int cursorIndex;

	public ViewText() {
		this(Maybe.ofNothing(), 0xFFFFFFFF, 0xFF000000, 0xFF000000, 0xFF0000FF, 0xFFFFFFFF, 0, 0, 0);
	}

	public ViewText(Maybe<Font> preferredFont, int textColor, int backgroundColor, int selectionTextColor, int selectionBackgroundColor, int cursorColor,
					int selectedFromIndex, int selectedToIndex, int cursorIndex) {
		this.preferredFont = preferredFont;
		this.textColor = textColor;
		this.backgroundColor = backgroundColor;
		this.selectionTextColor = selectionTextColor;
		this.selectionBackgroundColor = selectionBackgroundColor;
		this.cursorColor = cursorColor;
		this.selectedFromIndex = selectedFromIndex;
		this.selectedToIndex = selectedToIndex;
		this.cursorIndex = cursorIndex;
	}

	@Override
	public void draw(DrawContext ctx, M model) {
		super.draw(ctx, model);

		Font font = preferredFont.orElse(() -> ctx.proxy.lambdazationFonts.defaultFont.get());

		// TODO NYI

		TexturedGlyph glyph = font.getGlyph('i');
		ResourceLocation resource = glyph.getTextureLocation();

		if (resource != null) {
			ctx.minecraft.getTextureManager().bindTexture(resource);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferBuilder = tessellator.getBuffer();

			bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
			glyph.render(ctx.minecraft.getTextureManager(), false, 0.0F, 0.0F, bufferBuilder, 1.0F, 1.0F, 1.0F, 1.0F);

			tessellator.draw();
		}
	}
}
