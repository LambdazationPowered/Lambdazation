package org.lambdazation.client.gui.widget.view;

import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ViewSprite<M extends ModelBase> extends ViewBase<M> {
	private ResourceLocation resource;

	public ViewSprite(ResourceLocation resource) {
		this.resource = resource;
	}

	@Override
	public void draw(DrawContext ctx, M model) {
		super.draw(ctx, model);

		ctx.minecraft.getTextureManager().bindTexture(resource);

		double textureWidth = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		double textureHeight = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		bufferBuilder.pos(0.0D, 0.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
		bufferBuilder.pos(0.0D, textureHeight, 0.0D).tex(0.0D, 1.0D).endVertex();
		bufferBuilder.pos(textureWidth, textureHeight, 0.0D).tex(1.0D, 1.0D).endVertex();
		bufferBuilder.pos(textureWidth, 0.0D, 0.0D).tex(1.0D, 0.0D).endVertex();

		tessellator.draw();
	}
}
