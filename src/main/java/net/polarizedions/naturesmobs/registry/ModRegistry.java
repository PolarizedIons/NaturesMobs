package net.polarizedions.naturesmobs.registry;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.polarizedions.naturesmobs.NaturesMobs;
import net.polarizedions.naturesmobs.blocks.tiles.TileEntityProviderWrapper;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber
public class ModRegistry {
    private static final List<IModBlock> BLOCKS = new ArrayList<>();
    private static final List<IModItem> ITEMS = new ArrayList<>();

    public static void add(IModContent content) {
        if (content instanceof IModBlock) {
            BLOCKS.add((IModBlock) content);
        }
        else if (content instanceof IModItem) {
            ITEMS.add((IModItem) content);
        }

        else {
            NaturesMobs.logger.error("Error registering mod content {} ({})", content.getBaseName(), content.getClass());
        }
    }

    // RegistryEvents are currently supported for the following types: Block, Item, Potion, Biome, SoundEvent, PotionType, Enchantment, IRecipe, VillagerProfession, EntityEntry

    @SubscribeEvent
    static void onRegisterBlocksEvent(RegistryEvent.Register<Block> event) {
        NaturesMobs.logger.info("Registering {}'s blocks", NaturesMobs.MOD_NAME);
        IForgeRegistry<Block> registry = event.getRegistry();
        for (IModBlock content : BLOCKS) {
            registerBlock(registry, content);
        }
    }

    @SubscribeEvent
    static void onRegisterItemsEvent(RegistryEvent.Register<Item> event) {
        NaturesMobs.logger.info("Registering {}'s items", NaturesMobs.MOD_NAME);
        IForgeRegistry<Item> registry = event.getRegistry();
        for (IModItem content : ITEMS) {
            registerItem(registry, content);
        }
    }

    public static void onPreInit(FMLPreInitializationEvent event) {

    }

    public static void onInit(FMLInitializationEvent event) {
        for (IModBlock content : BLOCKS) {
            if (content instanceof TileEntityProviderWrapper && ((TileEntityProviderWrapper) content).hasTESR()) {
                NaturesMobs.logger.info("Registering TESR for {}", ((Block) content).getRegistryName());
                NaturesMobs.proxy.registerTESR((TileEntityProviderWrapper)content);
            }
        }
    }

    public static void onPostInit(FMLPostInitializationEvent event) {

    }

    private static void registerBlock(IForgeRegistry<Block> registry, IModBlock modBlock) {
        String name = modBlock.getBaseName();
        Block block = (Block) modBlock;
        NaturesMobs.logger.info("   Adding block {}:{}", NaturesMobs.MOD_ID, modBlock.getBaseName());

        block.setTranslationKey(NaturesMobs.MOD_ID + "." + name);
        block.setRegistryName(NaturesMobs.MOD_ID, name);

        if (modBlock.hasItem()) {
            ItemBlock item = modBlock.createItemBlock();
//            item.setRegistryName(Objects.requireNonNull(block.getRegistryName()));
//            NaturesMobs.proxy.registerItemRenderer(new ItemStack(item) , new ModelResourceLocation(new ResourceLocation(NaturesMobs.MOD_ID, name), "inventory"));
//
//            ForgeRegistries.ITEMS.register(item);

            ModRegistry.add(new IModItem() {
                @Override
                public String getBaseName() {
                    return name;
                }

                @Override
                public Item getItem() {
                    return item;
                }
            });
        }

        if (((Block) modBlock).hasTileEntity(((Block) modBlock).getDefaultState())) {
            GameRegistry.registerTileEntity(((TileEntityProviderWrapper) modBlock).getTileEntityClass(), block.getRegistryName());
        }

        if (modBlock.addToCreative()) {
            block.setCreativeTab(NaturesMobs.CREATIVE_TAB);
        }

        registry.register(block);
    }

    private static void registerItem(IForgeRegistry<Item> registry, IModItem modItem) {
        String name = modItem.getBaseName();
        Item item = modItem.getItem();
        NaturesMobs.logger.info("   Adding Item {}:{}", NaturesMobs.MOD_ID, modItem.getBaseName());

        item.setTranslationKey(NaturesMobs.MOD_ID + "." + name);
        item.setRegistryName(NaturesMobs.MOD_ID, name);

        if (modItem.addToCreative()) {
            item.setCreativeTab(NaturesMobs.CREATIVE_TAB);
        }

        registry.register(item);

        NaturesMobs.proxy.registerItemRenderer(new ItemStack(item) , new ModelResourceLocation(new ResourceLocation(NaturesMobs.MOD_ID, name), "inventory"));
    }
}
