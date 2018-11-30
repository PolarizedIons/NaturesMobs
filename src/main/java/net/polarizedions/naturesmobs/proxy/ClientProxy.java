package net.polarizedions.naturesmobs.proxy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.polarizedions.naturesmobs.NaturesMobs;
import net.polarizedions.naturesmobs.blocks.tiles.TileEntityFeeder;
import net.polarizedions.naturesmobs.blocks.tiles.render.RenderFeeder;

public class ClientProxy implements IProxy {
    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(NaturesMobs.MOD_ID + ":" + id, "inventory"));
    }

    @Override
    public void registerTESRs() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFeeder.class, new RenderFeeder());
    }
}
