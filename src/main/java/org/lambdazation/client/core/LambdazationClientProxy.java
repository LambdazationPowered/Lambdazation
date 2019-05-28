package org.lambdazation.client.core;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.core.LambdazationCommonProxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.ModLoadingContext;

public class LambdazationClientProxy extends LambdazationCommonProxy {
	public final LambdazationGuiFactory lambdazationGuiFactory;
	public final LambdazationFonts lambdazationFonts;

	public LambdazationClientProxy(Lambdazation lambdazation) {
		super(lambdazation);

		lambdazationGuiFactory = new LambdazationGuiFactory(this);
		lambdazationFonts = new LambdazationFonts(this);
	}

	@Override
	public void init() {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> lambdazationGuiFactory::openGui);
		Minecraft.getInstance().addScheduledTask(() -> lambdazationFonts.registerFonts(LogicalSidedProvider.INSTANCE.<Minecraft> get(LogicalSide.CLIENT).textureManager));
		Minecraft.getInstance().addScheduledTask(() -> LogicalSidedProvider.INSTANCE.<Minecraft> get(LogicalSide.CLIENT).getFramebuffer().enableStencil());
	}
}
