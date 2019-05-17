package org.lambdazation.client.core;

import net.minecraft.client.gui.GuiCreateWorld;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.lambdazation.Lambdazation;

public class MusicValleyWorldType extends WorldType {
    private Lambdazation lambdazation;
    public WorldType MUSIC_VALLEY=new WorldType("MUSIC_VALLEY").setCustomOptions(true);
    private Biome musicValley=ForgeRegistries.BIOMES.getValue(new ResourceLocation("lambdazation:music_valley"));
    public MusicValleyWorldType(){
        super("MUSIC_VALLEY");
    }


    @Override
    public boolean canBeCreated() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onPreInitCreateWorld(GuiScreenEvent.InitGuiEvent.Pre event)
    {
        GuiScreen screenGui = event.getGui();

        if (screenGui instanceof GuiCreateWorld)
        {
            GuiCreateWorld createWorldGui = (GuiCreateWorld)screenGui;
            //todo WIP
        }
    }

}
