package org.lambdazation.client.gui.inventory;

import java.util.Optional;

import org.lambdazation.client.core.LambdazationClientProxy;
import org.lambdazation.common.core.LambdazationTermFactory.TermState;
import org.lambdazation.common.inventory.ContainerCalibrator;
import org.lambdazation.common.item.ItemLambdaCrystal;

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

	public final LambdazationClientProxy proxy;

	public ContainerCalibrator containerCalibrator;
	public ItemStack prevItemStack;
	public Optional<Integer> cachedCapacity;
	public Optional<Integer> cachedEnergy;
	public Optional<TermState> cachedTermState;
	public Optional<Integer> cachedTermSize;
	public Optional<Integer> cachedTermDepth;

	public GuiCalibrator(LambdazationClientProxy proxy, InventoryPlayer playerInventory) {
		super(new ContainerCalibrator(proxy.lambdazation, playerInventory));

		this.proxy = proxy;

		this.containerCalibrator = (ContainerCalibrator) inventorySlots;
		this.prevItemStack = null;
		this.cachedCapacity = Optional.empty();
		this.cachedEnergy = Optional.empty();
		this.cachedTermState = Optional.empty();
		this.cachedTermSize = Optional.empty();
		this.cachedTermDepth = Optional.empty();
	}

	@Override
	protected void initGui() {
		super.initGui();
		containerCalibrator.removeListener(this);
		containerCalibrator.addListener(this);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		cachedCapacity.ifPresent(capacity -> drawString(fontRenderer, "Capacity: " + capacity, 4 + 0, 4 + 0, 0xFFFFFFFF));
		cachedEnergy.ifPresent(energy -> drawString(fontRenderer, "Energy: " + energy, 4 + 0, 4 + 8, 0xFFFFFFFF));
		cachedTermState.ifPresent(termState -> drawString(fontRenderer, "TermState: " + termState, 4 + 0, 4 + 16, 0xFFFFFFFF));
		cachedTermSize.ifPresent(termSize -> drawString(fontRenderer, "TermSize: " + termSize, 4 + 0, 4 + 24, 0xFFFFFFFF));
		cachedTermDepth.ifPresent(termDepth -> drawString(fontRenderer, "TermDepth: " + termDepth, 4 + 0, 4 + 32, 0xFFFFFFFF));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

		mc.getTextureManager().bindTexture(LENS_RESOURCE);
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}

	public void detectChanges(ItemStack itemStack) {
		ItemLambdaCrystal itemLambdaCrystal = proxy.lambdazation.lambdazationItems.itemLambdaCrystal;

		if (prevItemStack == null || !ItemStack.areItemStacksEqual(itemStack, prevItemStack)) {
			if (itemStack.getItem().equals(itemLambdaCrystal)) {
				cachedCapacity = itemLambdaCrystal.getCapacity(itemStack);
				cachedEnergy = itemLambdaCrystal.getEnergy(itemStack);
				cachedTermState = itemLambdaCrystal.getTermState(itemStack);
				cachedTermSize = itemLambdaCrystal.getTermSize(itemStack);
				cachedTermDepth = itemLambdaCrystal.getTermDepth(itemStack);
			} else {
				cachedCapacity = Optional.empty();
				cachedEnergy = Optional.empty();
				cachedTermState = Optional.empty();
				cachedTermSize = Optional.empty();
				cachedTermDepth = Optional.empty();
			}
			prevItemStack = itemStack;
		}
	}

	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
		if (containerToSend.equals(containerCalibrator) && itemsList.size() > 0)
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
