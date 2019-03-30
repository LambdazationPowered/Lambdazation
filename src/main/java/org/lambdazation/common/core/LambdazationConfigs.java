package org.lambdazation.common.core;

import org.apache.commons.lang3.tuple.Pair;
import org.lambdazation.Lambdazation;
import org.lambdazation.common.config.ConfigClient;
import org.lambdazation.common.config.ConfigCommon;
import org.lambdazation.common.config.ConfigServer;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class LambdazationConfigs {
	public final Lambdazation lambdazation;

	public final ForgeConfigSpec commonConfigSpec;
	public final ConfigCommon configCommon;
	public final ForgeConfigSpec clientConfigSpec;
	public final ConfigClient configClient;
	public final ForgeConfigSpec serverConfigSpec;
	public final ConfigServer configServer;

	public LambdazationConfigs(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;

		Pair<ConfigCommon, ForgeConfigSpec> commonConfigPair = new ForgeConfigSpec.Builder()
			.configure(ConfigCommon::new);
		commonConfigSpec = commonConfigPair.getRight();
		configCommon = commonConfigPair.getLeft();
		Pair<ConfigClient, ForgeConfigSpec> clientConfigPair = new ForgeConfigSpec.Builder()
			.configure(ConfigClient::new);
		clientConfigSpec = clientConfigPair.getRight();
		configClient = clientConfigPair.getLeft();
		Pair<ConfigServer, ForgeConfigSpec> serverConfigPair = new ForgeConfigSpec.Builder()
			.configure(ConfigServer::new);
		serverConfigSpec = serverConfigPair.getRight();
		configServer = serverConfigPair.getLeft();
	}

	public void init() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonConfigSpec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, clientConfigSpec);
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverConfigSpec);
	}
}
