package org.lambdazation.client.core;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.gui.fonts.providers.DefaultGlyphProvider;
import net.minecraft.client.gui.fonts.providers.GlyphProviderTypes;
import net.minecraft.client.gui.fonts.providers.IGlyphProvider;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lambdazation.common.util.Uninitialized;
import org.lambdazation.common.util.ValueBuilder;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@OnlyIn(Dist.CLIENT)
public final class LambdazationFonts {
	public final LambdazationClientProxy proxy;

	public final Uninitialized<Font> defaultFont;

	public LambdazationFonts(LambdazationClientProxy proxy) {
		this.proxy = proxy;
		this.defaultFont = new Uninitialized<>();
	}

	public void registerFonts(Minecraft minecraft) {
		defaultFont.init(ValueBuilder.build(
			new Font(minecraft.textureManager, new ResourceLocation("lambdazation:default")),
			font -> font.setGlyphProviders(loadGlyphProviders(minecraft.getResourceManager(), new ResourceLocation("minecraft:font/default.json")))));
	}

	public List<IGlyphProvider> loadGlyphProviders(IResourceManager resourceManager, ResourceLocation resourceLocation) {
		Gson gson = new GsonBuilder().create();

		try {
			IResource resource = resourceManager.getResource(resourceLocation);
			try (JsonReader reader = new JsonReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
				JsonObject jsonObjectFont = gson.fromJson(reader, JsonObject.class);
				JsonArray jsonArrayProviders = jsonObjectFont.getAsJsonArray("providers");

				List<IGlyphProvider> providers = StreamSupport.stream(jsonArrayProviders.spliterator(), false)
					.filter(JsonElement::isJsonObject)
					.map(JsonElement::getAsJsonObject)
					.map(jsonObjectProvider -> GlyphProviderTypes
						.byName(jsonObjectProvider.getAsJsonPrimitive("type").getAsString())
						.getFactory(jsonObjectProvider).create(resourceManager))
					.filter(Objects::nonNull)
					.collect(Collectors.toCollection(ArrayList::new));
				providers.add(new DefaultGlyphProvider());
				return providers;
			}
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
