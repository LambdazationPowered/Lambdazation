package org.lambdazation.client.core;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.core.LambdazationCommonProxy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;

public class LambdazationClientProxy extends LambdazationCommonProxy {
	public final LambdazationGuiFactory lambdazationGuiFactory;

	public LambdazationClientProxy(Lambdazation lambdazation) {
		super(lambdazation);

		lambdazationGuiFactory = new LambdazationGuiFactory(lambdazation);
	}

	@Override
	public void init() {
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> lambdazationGuiFactory::openGui);
		Minecraft.getInstance().addScheduledTask(() -> Minecraft.getInstance().getFramebuffer().enableStencil());
	}
}
