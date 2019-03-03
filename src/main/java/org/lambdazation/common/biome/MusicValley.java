package org.lambdazation.common.biome;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;

public class MusicValley extends Biome {
    public MusicValley(){
        super(new BiomeBuilder().temperature(3).waterColor(255*105*180));
    }

    @Override
    public void decorate(GenerationStage.Decoration stage, IChunkGenerator<? extends IChunkGenSettings> chunkGenerator, IWorld worldIn,
                         long p_203608_4_, SharedSeedRandom random, BlockPos pos) {
        super.decorate(stage, chunkGenerator, worldIn, p_203608_4_, random, pos);
    }
    public void playSound(EntityPlayer player){
        if(player.world.getBiome(player.getPosition()) instanceof MusicValley)
            player.world.playSound(player,player.getPosition(),new SoundEvent(new ResourceLocation("angel")),
                    SoundCategory.MUSIC,3,7);
    }
}
