package org.lambdazation.client.gui.widget.view;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ViewButton<M extends ModelBase> extends ViewBase<M> {
	public static final ResourceLocation RESOURCE = new ResourceLocation("lambdazation", "textures/gui/widget/button.png");

	private double width;
	private double height;
	private String text;
	private ResourceLocation resource;
	private int textColor;
	private int textColorPressed;
	private int textColorHovered;
	private int textColorDisabled;
	private int selectionColor;
	private double selectionTopMargin;
	private double selectionLeftMargin;
	private double selectionBottomMargin;
	private double selectionRightMargin;

	public ViewButton(double width, double height, String text) {
		this(width, height, text, RESOURCE, 0xFFE0E0E0, 0xFFFFFFA0, 0xFFFFFFA0, 0xFFA0A0A0, 0xFFFFFFFF, 3.0D, 3.0D, 3.0D, 3.0D);
	}

	public ViewButton(double width, double height, String text, ResourceLocation resource,
		int textColor, int textColorPressed, int textColorHovered, int textColorDisabled,
		int selectionColor, double selectionTopMargin, double selectionLeftMargin, double selectionBottomMargin, double selectionRightMargin) {
		this.width = width;
		this.height = height;
		this.text = text;
		this.resource = resource;
		this.textColor = textColor;
		this.textColorPressed = textColorPressed;
		this.textColorHovered = textColorHovered;
		this.textColorDisabled = textColorDisabled;
		this.selectionColor = selectionColor;
		this.selectionTopMargin = selectionTopMargin;
		this.selectionLeftMargin = selectionLeftMargin;
		this.selectionBottomMargin = selectionBottomMargin;
		this.selectionRightMargin = selectionRightMargin;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public ResourceLocation getResource() {
		return resource;
	}

	public void setResource(ResourceLocation resource) {
		this.resource = resource;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getTextColorPressed() {
		return textColorPressed;
	}

	public void setTextColorPressed(int textColorPressed) {
		this.textColorPressed = textColorPressed;
	}

	public int getTextColorHovered() {
		return textColorHovered;
	}

	public void setTextColorHovered(int textColorHovered) {
		this.textColorHovered = textColorHovered;
	}

	public int getTextColorDisabled() {
		return textColorDisabled;
	}

	public void setTextColorDisabled(int textColorDisabled) {
		this.textColorDisabled = textColorDisabled;
	}

	public int getSelectionColor() {
		return selectionColor;
	}

	public void setSelectionColor(int selectionColor) {
		this.selectionColor = selectionColor;
	}

	public double getSelectionTopMargin() {
		return selectionTopMargin;
	}

	public void setSelectionTopMargin(double selectionTopMargin) {
		this.selectionTopMargin = selectionTopMargin;
	}

	public double getSelectionLeftMargin() {
		return selectionLeftMargin;
	}

	public void setSelectionLeftMargin(double selectionLeftMargin) {
		this.selectionLeftMargin = selectionLeftMargin;
	}

	public double getSelectionBottomMargin() {
		return selectionBottomMargin;
	}

	public void setSelectionBottomMargin(double selectionBottomMargin) {
		this.selectionBottomMargin = selectionBottomMargin;
	}

	public double getSelectionRightMargin() {
		return selectionRightMargin;
	}

	public void setSelectionRightMargin(double selectionRightMargin) {
		this.selectionRightMargin = selectionRightMargin;
	}

	@Override
	public void draw(DrawContext ctx, M model) {
		super.draw(ctx, model);

		boolean isHovered = ctx.mouseContext.localX >= 0.0D && ctx.mouseContext.localX < width &&
			ctx.mouseContext.localY >= 0.0D && ctx.mouseContext.localY < height;
		boolean isPressed = (isHovered && ctx.mouseContext.buttonPressed.contains(GLFW.GLFW_MOUSE_BUTTON_LEFT)) ||
			(isFocused() && ctx.keyboardContext.keyPressed.contains(GLFW.GLFW_KEY_ENTER));

		double xOffset;
		double yOffset;

		if (!isEnable()) {
			xOffset = 0.0D;
			yOffset = 0.0D;
		} else if (isPressed) {
			xOffset = 1.0D;
			yOffset = 1.0D;
		} else if (isHovered) {
			xOffset = 1.0D;
			yOffset = 0.0D;
		} else {
			xOffset = 0.0D;
			yOffset = 1.0D;
		}

		ctx.minecraft.getTextureManager().bindTexture(resource);

		double textureWidth = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		double textureHeight = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

		double centerWidth = width - 1.0D / 4.0D * textureWidth;
		double centerHeight = height - 1.0D / 4.0D * textureHeight;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		bufferBuilder.pos(0.0D /*         */, 0.0D /*          */, 0.0D).tex(0.0D / 8.0D + 3.0D / 8.0D * xOffset, 0.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(0.0D /*         */, 2.0D /*          */, 0.0D).tex(0.0D / 8.0D + 3.0D / 8.0D * xOffset, 1.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D /*         */, 2.0D /*          */, 0.0D).tex(1.0D / 8.0D + 3.0D / 8.0D * xOffset, 1.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D /*         */, 0.0D /*          */, 0.0D).tex(1.0D / 8.0D + 3.0D / 8.0D * xOffset, 0.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();

		bufferBuilder.pos(2.0D /*         */, 0.0D /*          */, 0.0D).tex(1.0D / 8.0D + 3.0D / 8.0D * xOffset, 0.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D /*         */, 2.0D /*          */, 0.0D).tex(1.0D / 8.0D + 3.0D / 8.0D * xOffset, 1.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D + centerWidth, 2.0D /*          */, 0.0D).tex(2.0D / 8.0D + 3.0D / 8.0D * xOffset, 1.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D + centerWidth, 0.0D /*          */, 0.0D).tex(2.0D / 8.0D + 3.0D / 8.0D * xOffset, 0.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();

		bufferBuilder.pos(2.0D + centerWidth, 0.0D /*          */, 0.0D).tex(2.0D / 8.0D + 3.0D / 8.0D * xOffset, 0.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D + centerWidth, 2.0D /*          */, 0.0D).tex(2.0D / 8.0D + 3.0D / 8.0D * xOffset, 1.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(4.0D + centerWidth, 2.0D /*          */, 0.0D).tex(3.0D / 8.0D + 3.0D / 8.0D * xOffset, 1.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(4.0D + centerWidth, 0.0D /*          */, 0.0D).tex(3.0D / 8.0D + 3.0D / 8.0D * xOffset, 0.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();

		bufferBuilder.pos(0.0D /*         */, 2.0D /*          */, 0.0D).tex(0.0D / 8.0D + 3.0D / 8.0D * xOffset, 1.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(0.0D /*         */, 2.0D + centerHeight, 0.0D).tex(0.0D / 8.0D + 3.0D / 8.0D * xOffset, 2.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D /*         */, 2.0D + centerHeight, 0.0D).tex(1.0D / 8.0D + 3.0D / 8.0D * xOffset, 2.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D /*         */, 2.0D /*          */, 0.0D).tex(1.0D / 8.0D + 3.0D / 8.0D * xOffset, 1.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();

		bufferBuilder.pos(2.0D /*         */, 2.0D /*          */, 0.0D).tex(1.0D / 8.0D + 3.0D / 8.0D * xOffset, 1.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D /*         */, 2.0D + centerHeight, 0.0D).tex(1.0D / 8.0D + 3.0D / 8.0D * xOffset, 2.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D + centerWidth, 2.0D + centerHeight, 0.0D).tex(2.0D / 8.0D + 3.0D / 8.0D * xOffset, 2.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D + centerWidth, 2.0D /*          */, 0.0D).tex(2.0D / 8.0D + 3.0D / 8.0D * xOffset, 1.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();

		bufferBuilder.pos(2.0D + centerWidth, 2.0D /*          */, 0.0D).tex(2.0D / 8.0D + 3.0D / 8.0D * xOffset, 1.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D + centerWidth, 2.0D + centerHeight, 0.0D).tex(2.0D / 8.0D + 3.0D / 8.0D * xOffset, 2.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(4.0D + centerWidth, 2.0D + centerHeight, 0.0D).tex(3.0D / 8.0D + 3.0D / 8.0D * xOffset, 2.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(4.0D + centerWidth, 2.0D /*          */, 0.0D).tex(3.0D / 8.0D + 3.0D / 8.0D * xOffset, 1.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();

		bufferBuilder.pos(0.0D /*         */, 2.0D + centerHeight, 0.0D).tex(0.0D / 8.0D + 3.0D / 8.0D * xOffset, 2.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(0.0D /*         */, 4.0D + centerHeight, 0.0D).tex(0.0D / 8.0D + 3.0D / 8.0D * xOffset, 3.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D /*         */, 4.0D + centerHeight, 0.0D).tex(1.0D / 8.0D + 3.0D / 8.0D * xOffset, 3.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D /*         */, 2.0D + centerHeight, 0.0D).tex(1.0D / 8.0D + 3.0D / 8.0D * xOffset, 2.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();

		bufferBuilder.pos(2.0D /*         */, 2.0D + centerHeight, 0.0D).tex(1.0D / 8.0D + 3.0D / 8.0D * xOffset, 2.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D /*         */, 4.0D + centerHeight, 0.0D).tex(1.0D / 8.0D + 3.0D / 8.0D * xOffset, 3.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D + centerWidth, 4.0D + centerHeight, 0.0D).tex(2.0D / 8.0D + 3.0D / 8.0D * xOffset, 3.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D + centerWidth, 2.0D + centerHeight, 0.0D).tex(2.0D / 8.0D + 3.0D / 8.0D * xOffset, 2.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();

		bufferBuilder.pos(2.0D + centerWidth, 2.0D + centerHeight, 0.0D).tex(2.0D / 8.0D + 3.0D / 8.0D * xOffset, 2.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(2.0D + centerWidth, 4.0D + centerHeight, 0.0D).tex(2.0D / 8.0D + 3.0D / 8.0D * xOffset, 3.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(4.0D + centerWidth, 4.0D + centerHeight, 0.0D).tex(3.0D / 8.0D + 3.0D / 8.0D * xOffset, 3.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();
		bufferBuilder.pos(4.0D + centerWidth, 2.0D + centerHeight, 0.0D).tex(3.0D / 8.0D + 3.0D / 8.0D * xOffset, 2.0D / 8.0D + 3.0D / 8.0D * yOffset).endVertex();

		tessellator.draw();

		if (isEnable() && isFocused()) {
			double selectionColorAlpha = (selectionColor >>> 24 & 0xFF) / 255.0D;
			double selectionColorRed = (selectionColor >>> 16 & 0xFF) / 255.0D;
			double selectionColorGreen = (selectionColor >>> 8 & 0xFF) / 255.0D;
			double selectionColorBlue = (selectionColor >>> 0 & 0xFF) / 255.0D;

			double selectionWidth = width - selectionLeftMargin - selectionRightMargin;
			double selectionHeight = height - selectionTopMargin - selectionBottomMargin;

			if (selectionWidth > 0.0D && selectionHeight >= 0.0D) {
				GlStateManager.enableBlend();
				GlStateManager.disableTexture2D();
				GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
				GlStateManager.color4f((float) selectionColorRed, (float) selectionColorGreen, (float) selectionColorBlue, (float) selectionColorAlpha);
				GlStateManager.lineWidth(1.0F);
				GL11.glEnable(GL11.GL_LINE_STIPPLE);
				GL11.glLineStipple(1, (short) 0x5555);

				bufferBuilder.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);

				bufferBuilder.pos(selectionLeftMargin, selectionTopMargin, 0.0D).endVertex();
				bufferBuilder.pos(selectionLeftMargin, selectionTopMargin + selectionHeight, 0.0D).endVertex();
				bufferBuilder.pos(selectionLeftMargin + selectionWidth, selectionTopMargin + selectionHeight, 0.0D).endVertex();
				bufferBuilder.pos(selectionLeftMargin + selectionWidth, selectionTopMargin, 0.0D).endVertex();

				tessellator.draw();

				GL11.glDisable(GL11.GL_LINE_STIPPLE);
				GlStateManager.disableBlend();
				GlStateManager.enableTexture2D();
			}
		}

		int textColor;

		if (!isEnable())
			textColor = this.textColorDisabled;
		else if (isPressed)
			textColor = this.textColorPressed;
		else if (isHovered)
			textColor = this.textColorHovered;
		else
			textColor = this.textColor;

		double textWidth = ctx.minecraft.fontRenderer.getStringWidth(text);
		double textHeight = ctx.minecraft.fontRenderer.FONT_HEIGHT;
		double textX = (width - textWidth) / 2.0D;
		double textY = (height - textHeight) / 2.0D;

		ctx.minecraft.fontRenderer.drawStringWithShadow(text, (float) textX, (float) textY, textColor);
	}
}
