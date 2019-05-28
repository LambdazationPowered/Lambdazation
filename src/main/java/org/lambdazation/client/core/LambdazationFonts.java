package org.lambdazation.client.core;

import java.util.Collections;

import org.lambdazation.common.util.Uninitialized;
import org.lambdazation.common.util.ValueBuilder;

import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.providers.DefaultGlyphProvider;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class LambdazationFonts {
	public final LambdazationClientProxy proxy;

	public final Uninitialized<Font> defaultFont;

	public LambdazationFonts(LambdazationClientProxy proxy) {
		this.proxy = proxy;

		this.defaultFont = new Uninitialized<>();
	}

	public void registerFonts(TextureManager textureManager) {
		defaultFont.init(ValueBuilder.build(
			new Font(textureManager, new ResourceLocation("lambdazation:default")),
			font -> font.setGlyphProviders(Collections.singletonList(new DefaultGlyphProvider()))));
		// TODO NYI
	}
}
