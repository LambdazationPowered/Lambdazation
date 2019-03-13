package org.lambdazation.client.gui;

import java.util.Optional;

import org.lambdazation.Lambdazation;
import org.lambdazation.common.inventory.ContainerCalibrator;
import org.lambdazation.common.item.ItemLambdaCrystal;
import org.lambdazation.common.item.ItemLambdaCrystal.TermState;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GuiCalibrator extends GuiContainer implements IContainerListener {
	public static final ResourceLocation LENS_RESOURCE = new ResourceLocation("lambdazation", "textures/gui/container/calibrator.png");

	public final Lambdazation lambdazation;

	public ContainerCalibrator containerCalibrator;
	public ItemStack prevItemStack;
	public Optional<TermState> cachedTermState;
	public Optional<Integer> cachedTermSize;
	public Optional<Integer> cachedCapacity;
	public Optional<Integer> cachedEnergy;

	public GuiCalibrator(Lambdazation lambdazation, InventoryPlayer playerInventory) {
		super(new ContainerCalibrator(lambdazation, playerInventory));

		this.lambdazation = lambdazation;

		this.containerCalibrator = (ContainerCalibrator) inventorySlots;
		this.prevItemStack = null;
		this.cachedTermState = Optional.empty();
		this.cachedCapacity = Optional.empty();
		this.cachedEnergy = Optional.empty();
	}

	@Override
	protected void initGui() {
		super.initGui();
		containerCalibrator.removeListener(this);
		containerCalibrator.addListener(this);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawDefaultBackground();

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.getTextureManager().bindTexture(LENS_RESOURCE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
		cachedTermState.ifPresent(termState -> drawString(fontRenderer, "TermState: " + termState.toString(), 0, 0, 0xFFFFFF));
		cachedCapacity.ifPresent(capacity -> drawString(fontRenderer, "Capacity: " + capacity, 0, 8, 0xFFFFFF));
		cachedEnergy.ifPresent(energy -> drawString(fontRenderer, "Energy: " + energy, 0, 16, 0xFFFFFF));
	}

	public void detectChanges(ItemStack itemStack) {
		ItemLambdaCrystal itemLambdaCrystal = lambdazation.lambdazationItems.itemLambdaCrystal;

		if (prevItemStack == null || !ItemStack.areItemStacksEqual(itemStack, prevItemStack)) {
			if (itemStack.getItem().equals(itemLambdaCrystal)) {
				cachedTermState = itemLambdaCrystal.getTermState(itemStack);
				cachedCapacity = itemLambdaCrystal.getCapacity(itemStack);
				cachedEnergy = itemLambdaCrystal.getEnergy(itemStack);
			} else {
				cachedTermState = Optional.empty();
				cachedCapacity = Optional.empty();
				cachedEnergy = Optional.empty();
			}
			prevItemStack = itemStack;
		}
	}

	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
		if (containerToSend.equals(containerCalibrator) && itemsList.size() >= 1)
			detectChanges(itemsList.get(0));
	}

	@Override
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
		if (containerToSend.equals(containerCalibrator) && slotInd == 0)
			detectChanges(stack);
	}

	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {
		
	}

	@Override
	public void sendAllWindowProperties(Container containerIn, IInventory inventory) {
		
	}
}
