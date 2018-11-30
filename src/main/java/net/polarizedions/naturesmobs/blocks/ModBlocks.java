package net.polarizedions.naturesmobs.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
    public static BlockFeeder FEEDER = new BlockFeeder();

    public static void register(IForgeRegistry<Block> registry) {
        registry.registerAll(
                FEEDER
        );
//        GameRegistry.registerTileEntity(TileEntityFeeder.class, FEEDER.getRegistryName().toString());
    }

    public static void registerItemBlocks(IForgeRegistry<Item> registry) {
        registry.registerAll(
                FEEDER.createItemBlock()
        );
    }

    public static void registerModels() {
        FEEDER.registerItemModel(Item.getItemFromBlock(FEEDER));
    }
}
