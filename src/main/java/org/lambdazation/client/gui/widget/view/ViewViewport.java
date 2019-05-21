package org.lambdazation.client.gui.widget.view;

import org.lambdazation.client.gui.widget.WidgetBase;
import org.lambdazation.client.gui.widget.model.ModelBase;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ViewViewport<M extends ModelBase> extends ViewWrapper<M> {
	private double viewX;
	private double viewY;
	private double viewWidth;
	private double viewHeight;

	public ViewViewport(WidgetBase<?, ?> component, double viewX, double viewY, double viewWidth, double viewHeight) {
		super(component);
		this.viewX = viewX;
		this.viewY = viewY;
		this.viewWidth = viewWidth;
		this.viewHeight = viewHeight;
	}

	public double getViewX() {
		return viewX;
	}

	public void setViewX(double viewX) {
		this.viewX = viewX;
	}

	public double getViewY() {
		return viewY;
	}

	public void setViewY(double viewY) {
		this.viewY = viewY;
	}

	public double getViewWidth() {
		return viewWidth;
	}

	public void setViewWidth(double viewWidth) {
		this.viewWidth = viewWidth;
	}

	public double getViewHeight() {
		return viewHeight;
	}

	public void setViewHeight(double viewHeight) {
		this.viewHeight = viewHeight;
	}

	@Override
	public void drawComponent(WidgetBase<?, ?> component, DrawContext ctx, M model) {
		GL11.glPushAttrib(GL11.GL_STENCIL_BUFFER_BIT);
		if (!GL11.glIsEnabled(GL11.GL_STENCIL_TEST)) {
			GL11.glEnable(GL11.GL_STENCIL_TEST);
			GL11.glClearStencil(0);
			GlStateManager.clear(GL11.GL_STENCIL_BUFFER_BIT);
		}

		GL11.glStencilFunc(GL11.GL_NEVER, 1, 1);
		GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_KEEP, GL11.GL_KEEP);
		GL11.glStencilMask(1);

		GlStateManager.disableTexture2D();
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = tessellator.getBuffer();

		bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);

		bufferBuilder.pos(0.0D, 0.0D, 0.0D).endVertex();
		bufferBuilder.pos(0.0D, viewHeight, 0.0D).endVertex();
		bufferBuilder.pos(viewWidth, viewHeight, 0.0D).endVertex();
		bufferBuilder.pos(viewWidth, 0.0D, 0.0D).endVertex();

		tessellator.draw();

		GlStateManager.enableTexture2D();

		GL11.glStencilFunc(GL11.GL_EQUAL, 1, 1);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		GL11.glStencilMask(0);

		GlStateManager.pushMatrix();
		GlStateManager.translated(-viewX, -viewY, 0.0D);
		if (ctx.mouseContext.localX >= 0.0D && ctx.mouseContext.localX < viewWidth &&
			ctx.mouseContext.localY >= 0.0D && ctx.mouseContext.localY < viewHeight)
			ctx = ctx.translate(-viewX, -viewY);
		else
			ctx = ctx.translate(Double.NaN, Double.NaN);

		super.drawComponent(component, ctx, model);
		GlStateManager.popMatrix();

		GL11.glPopAttrib();
	}
}
