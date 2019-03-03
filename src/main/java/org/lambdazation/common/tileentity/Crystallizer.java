package org.lambdazation.common.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class Crystallizer extends TileEntity {
    public static TileEntityType crystallizer=TileEntityType.register("crystal_chest",TileEntityType.Builder.create(Crystallizer::new));
    public Crystallizer(){
        super(crystallizer);
    }
}
