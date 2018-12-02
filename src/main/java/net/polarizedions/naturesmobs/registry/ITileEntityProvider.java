package net.polarizedions.naturesmobs.registry;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITileEntityProvider extends net.minecraft.block.ITileEntityProvider {
    @SideOnly(Side.CLIENT)
    TileEntity createNewTileEntity(World world, int meta);

    @SideOnly(Side.CLIENT)
    Class<? extends TileEntity> getTileEntityClass();
}
