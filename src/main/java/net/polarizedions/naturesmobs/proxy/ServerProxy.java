package net.polarizedions.naturesmobs.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.polarizedions.naturesmobs.blocks.tiles.TileEntityProviderWrapper;

public class ServerProxy implements IProxy {
    @Override
    public void registerItemRenderer(ItemStack stack, ModelResourceLocation location) {
        // NOOP
    }

    @Override
    public void registerTESR(TileEntityProviderWrapper tesrProvider) {
        // NOOP
    }

    @Override
    public void schedule(Runnable runnable) {
        FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(runnable);
    }
}
