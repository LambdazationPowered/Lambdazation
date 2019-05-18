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
	private ResourceLocation resource;
	private double width;
	private double height;

	public ViewButton(ResourceLocation resource, double width, double height) {
		this.resource = resource;
		this.width = width;
		this.height = height;
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

	@Override
	public void draw(DrawContext ctx, M model) {
		super.draw(ctx, model);

		boolean isHover = ctx.mouseContext.localX >= 0.0D && ctx.mouseContext.localX < width &&
			ctx.mouseContext.localY >= 0.0D && ctx.mouseContext.localY < height;
		double xOffset;
		double yOffset;
		if (isEnable()) {
			if (isHover) {
				if (ctx.mouseContext.buttonPressed.contains(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
					xOffset = 1.0D;
					yOffset = 1.0D;
				} else {
					xOffset = 1.0D;
					yOffset = 0.0D;
				}
			} else {
				xOffset = 0.0D;
				yOffset = 1.0D;
			}
		} else {
			xOffset = 0.0D;
			yOffset = 0.0D;
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
	}
}
