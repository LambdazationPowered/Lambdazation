package org.lambdazation.client.core;

import org.lambdazation.Lambdazation;
import org.lambdazation.client.gui.inventory.GuiCalibrator;
import org.lambdazation.client.gui.inventory.GuiCharger;
import org.lambdazation.client.gui.inventory.GuiCrystallizer;
import org.lambdazation.client.gui.inventory.GuiLens;
import org.lambdazation.client.gui.inventory.GuiReducer;
import org.lambdazation.client.gui.inventory.GuiTransformer;
import org.lambdazation.common.inventory.ContainerCalibrator;
import org.lambdazation.common.inventory.ContainerCharger;
import org.lambdazation.common.inventory.ContainerCrystallizer;
import org.lambdazation.common.inventory.ContainerLens;
import org.lambdazation.common.inventory.ContainerReducer;
import org.lambdazation.common.inventory.ContainerTransformer;
import org.lambdazation.common.tileentity.TileEntityCharger;
import org.lambdazation.common.tileentity.TileEntityCrystallizer;
import org.lambdazation.common.tileentity.TileEntityReducer;
import org.lambdazation.common.tileentity.TileEntityTransformer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;

@OnlyIn(Dist.CLIENT)
public final class LambdazationGuiFactory {
	public final Lambdazation lambdazation;

	public LambdazationGuiFactory(Lambdazation lambdazation) {
		this.lambdazation = lambdazation;
	}

	public GuiScreen openGui(FMLPlayMessages.OpenContainer msg) {
		if (msg.getId().equals(ContainerLens.GUI_ID)) {
			InventoryPlayer playerInventory = Minecraft.getInstance().player.inventory;

			return new GuiLens(lambdazation, playerInventory);
		} else if (msg.getId().equals(ContainerCalibrator.GUI_ID)) {
			InventoryPlayer playerInventory = Minecraft.getInstance().player.inventory;

			return new GuiCalibrator(lambdazation, playerInventory);
		} else if (msg.getId().equals(ContainerCrystallizer.GUI_ID)) {
			BlockPos blockPos = msg.getAdditionalData().readBlockPos();
			TileEntity tileEntity = Minecraft.getInstance().world.getTileEntity(blockPos);

			InventoryPlayer playerInventory = Minecraft.getInstance().player.inventory;

			if (!(tileEntity instanceof TileEntityCrystallizer))
				return null;
			TileEntityCrystallizer crystallizerInventory = (TileEntityCrystallizer) tileEntity;

			return new GuiCrystallizer(lambdazation, playerInventory, crystallizerInventory);
		} else if (msg.getId().equals(ContainerTransformer.GUI_ID)) {
			BlockPos blockPos = msg.getAdditionalData().readBlockPos();
			TileEntity tileEntity = Minecraft.getInstance().world.getTileEntity(blockPos);

			InventoryPlayer playerInventory = Minecraft.getInstance().player.inventory;

			if (!(tileEntity instanceof TileEntityTransformer))
				return null;
			TileEntityTransformer transformerInventory = (TileEntityTransformer) tileEntity;

			return new GuiTransformer(lambdazation, playerInventory, transformerInventory);
		} else if (msg.getId().equals(ContainerCharger.GUI_ID)) {
			BlockPos blockPos = msg.getAdditionalData().readBlockPos();
			TileEntity tileEntity = Minecraft.getInstance().world.getTileEntity(blockPos);

			InventoryPlayer playerInventory = Minecraft.getInstance().player.inventory;

			if (!(tileEntity instanceof TileEntityCharger))
				return null;
			TileEntityCharger chargerInventory = (TileEntityCharger) tileEntity;

			return new GuiCharger(lambdazation, playerInventory, chargerInventory);
		} else if (msg.getId().equals(ContainerReducer.GUI_ID)) {
			BlockPos blockPos = msg.getAdditionalData().readBlockPos();
			TileEntity tileEntity = Minecraft.getInstance().world.getTileEntity(blockPos);

			InventoryPlayer playerInventory = Minecraft.getInstance().player.inventory;

			if (!(tileEntity instanceof TileEntityReducer))
				return null;
			TileEntityReducer reducerInventory = (TileEntityReducer) tileEntity;

			return new GuiReducer(lambdazation, playerInventory, reducerInventory);
		} else
			return null;
	}
}
