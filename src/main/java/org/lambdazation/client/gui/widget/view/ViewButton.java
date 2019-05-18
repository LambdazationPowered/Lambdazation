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
	private boolean selectionVisible;
	private double selectionEdge;

	public ViewButton(ResourceLocation resource, double width, double height) {
		this(resource, width, height, true, 3.0D);
	}

	public ViewButton(ResourceLocation resource, double width, double height, boolean selectionVisible, double selectionEdge) {
		this.resource = resource;
		this.width = width;
		this.height = height;
		this.selectionVisible = selectionVisible;
		this.selectionEdge = selectionEdge;
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

	public boolean isSelectionVisible() {
		return selectionVisible;
	}

	public void setSelectionVisible(boolean selectionVisible) {
		this.selectionVisible = selectionVisible;
	}

	public double getSelectionEdge() {
		return selectionEdge;
	}

	public void setSelectionEdge(double selectionEdge) {
		this.selectionEdge = selectionEdge;
	}

	@Override
	public void draw(DrawContext ctx, M model) {
		super.draw(ctx, model);

		boolean isHover = ctx.mouseContext.localX >= 0.0D && ctx.mouseContext.localX < width &&
			ctx.mouseContext.localY >= 0.0D && ctx.mouseContext.localY < height;
		boolean isPressed = (isHover && ctx.mouseContext.buttonPressed.contains(GLFW.GLFW_MOUSE_BUTTON_LEFT)) ||
			ctx.keyboardContext.keyPressed.contains(GLFW.GLFW_KEY_ENTER);
		double xOffset;
		double yOffset;

		if (!isEnable()) {
			xOffset = 0.0D;
			yOffset = 0.0D;
		} else if (isPressed) {
			xOffset = 1.0D;
			yOffset = 1.0D;
		} else if (isHover) {
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

		if (selectionVisible && isEnable() && isFocused()) {
			double selectionWidth = width - selectionEdge * 2.0D;
			double selectionHeight = height - selectionEdge * 2.0D;

			if (selectionWidth > 0.0D && selectionHeight >= 0.0D) {
				GlStateManager.disableTexture2D();
				GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.lineWidth(1.0F);
				GL11.glEnable(GL11.GL_LINE_STIPPLE);
				GL11.glLineStipple(1, (short) 0x5555);

				bufferBuilder.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);

				bufferBuilder.pos(selectionEdge, selectionEdge, 0.0D).endVertex();
				bufferBuilder.pos(selectionEdge, selectionEdge + selectionHeight, 0.0D).endVertex();
				bufferBuilder.pos(selectionEdge + selectionWidth, selectionEdge + selectionHeight, 0.0D).endVertex();
				bufferBuilder.pos(selectionEdge + selectionWidth, selectionEdge, 0.0D).endVertex();

				tessellator.draw();

				GL11.glDisable(GL11.GL_LINE_STIPPLE);
				GlStateManager.enableTexture2D();
			}
		}
	}
}
