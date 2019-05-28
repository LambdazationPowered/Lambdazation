package org.lambdazation.client.gui.inventory;

import java.util.Optional;

import org.lambdazation.client.core.LambdazationClientProxy;
import org.lambdazation.common.inventory.ContainerLens;
import org.lambdazation.common.item.ItemLambdaCrystal;
import org.lamcalcj.pretty.PrettyPrint;

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
public final class GuiLens extends GuiContainer implements IContainerListener {
	public static final ResourceLocation LENS_RESOURCE = new ResourceLocation("lambdazation", "textures/gui/container/lens.png");

	public final LambdazationClientProxy proxy;

	public ContainerLens containerLens;
	public ItemStack prevItemStack;
	public Optional<String> cachedTermSource;

	public GuiLens(LambdazationClientProxy proxy, InventoryPlayer playerInventory) {
		super(new ContainerLens(proxy.lambdazation, playerInventory));

		this.proxy = proxy;

		this.containerLens = (ContainerLens) inventorySlots;
		this.prevItemStack = null;
		this.cachedTermSource = Optional.empty();
	}

	@Override
	protected void initGui() {
		super.initGui();
		containerLens.removeListener(this);
		containerLens.addListener(this);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		super.render(mouseX, mouseY, partialTicks);
		renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		cachedTermSource.ifPresent(termSource -> drawString(fontRenderer, "TermSource: " + termSource, 4 + 0, 4 + 0, 0xFFFFFFFF));
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
				cachedTermSource = itemLambdaCrystal.getTerm(itemStack)
					.map(term -> PrettyPrint.printLambda(term, PrettyPrint.printLambda$default$2(),
						PrettyPrint.printLambda$default$3(), PrettyPrint.printLambda$default$4(),
						PrettyPrint.printLambda$default$5(), PrettyPrint.printLambda$default$6()));
			} else {
				cachedTermSource = Optional.empty();
			}
			prevItemStack = itemStack;
		}
	}

	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList) {
		if (containerToSend.equals(containerLens) && itemsList.size() > 0)
			detectChanges(itemsList.get(0));
	}

	@Override
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
		if (containerToSend.equals(containerLens) && slotInd == 0)
			detectChanges(stack);
	}

	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue) {

	}

	@Override
	public void sendAllWindowProperties(Container containerIn, IInventory inventory) {

	}
}
