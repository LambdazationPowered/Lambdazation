package org.lambdazation.client.core;

import java.util.Collections;

import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.providers.DefaultGlyphProvider;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class LambdazationFonts {
	public final LambdazationClientProxy proxy;

	private Font defaultFont;

	public LambdazationFonts(LambdazationClientProxy proxy) {
		this.proxy = proxy;
	}

	public void registerFonts(TextureManager textureManager) {
		defaultFont = new Font(textureManager, new ResourceLocation("lambdazation:default"));
		defaultFont.setGlyphProviders(Collections.singletonList(new DefaultGlyphProvider()));
		// TODO NYI
	}

	public Font getDefaultFont() {
		if (defaultFont == null)
			throw new IllegalStateException("fonts not registered");
		return defaultFont;
	}
}
