package net.polarizedions.naturesmobs.proxy;


import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.polarizedions.naturesmobs.blocks.tiles.TileEntityProviderWrapper;

public class ClientProxy implements IProxy {
    @Override
    public void registerItemRenderer(ItemStack stack, ModelResourceLocation location) {
        ModelLoader.setCustomModelResourceLocation(stack.getItem(), stack.getItemDamage(), location);
//        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(NaturesMobs.MOD_ID + ":" + id, "inventory"));
    }

    @Override
    public void registerTESR(TileEntityProviderWrapper tesrProvider) {
        ClientRegistry.bindTileEntitySpecialRenderer(tesrProvider.getTileEntityClass(), tesrProvider.createTESR());
    }
}
