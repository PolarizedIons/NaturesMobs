package net.polarizedions.naturesmobs.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.polarizedions.naturesmobs.blocks.tiles.TileEntityProviderWrapper;

public interface IProxy {
    void registerItemRenderer(ItemStack stack, ModelResourceLocation location);
    void registerTESR(TileEntityProviderWrapper tesrProvider);
    void schedule(Runnable runnable);
}
