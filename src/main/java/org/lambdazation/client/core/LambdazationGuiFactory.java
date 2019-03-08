package org.lambdazation.client.core;

import org.lambdazation.Lambdazation;
import org.lambdazation.client.gui.GuiCrystallizer;
import org.lambdazation.client.gui.GuiLens;
import org.lambdazation.common.inventory.ContainerCrystallizer;
import org.lambdazation.common.inventory.ContainerLens;
import org.lambdazation.common.tileentity.TileEntityCrystallizer;

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
			InventoryPlayer inventoryPlayer = Minecraft.getInstance().player.inventory;

			return new GuiLens(lambdazation, inventoryPlayer);
		} else if (msg.getId().equals(ContainerCrystallizer.GUI_ID)) {
			BlockPos blockPos = msg.getAdditionalData().readBlockPos();
			TileEntity tileEntity = Minecraft.getInstance().world.getTileEntity(blockPos);

			InventoryPlayer inventoryPlayer = Minecraft.getInstance().player.inventory;

			if (!(tileEntity instanceof TileEntityCrystallizer))
				return null;
			TileEntityCrystallizer tileEntityCrystallizer = (TileEntityCrystallizer) tileEntity;

			return new GuiCrystallizer(lambdazation, inventoryPlayer, tileEntityCrystallizer);
		} else
			return null;
	}
}
