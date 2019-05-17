package org.lambdazation.client.gui.widget.view;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import org.lambdazation.client.gui.widget.model.ModelBase;
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
		ctx.minecraft.getTextureManager().bindTexture(resource);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();
		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		// TODO NYI
		bufferBuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
		bufferBuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
		bufferBuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
		bufferBuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
	}
}
