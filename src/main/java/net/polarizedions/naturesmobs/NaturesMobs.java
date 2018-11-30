package net.polarizedions.naturesmobs;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.polarizedions.naturesmobs.blocks.ModBlocks;
import net.polarizedions.naturesmobs.items.ModItems;
import net.polarizedions.naturesmobs.proxy.IProxy;
import org.apache.logging.log4j.Logger;

@Mod(modid = NaturesMobs.MOD_ID, name = NaturesMobs.MOD_NAME, version = NaturesMobs.VERSION, dependencies = NaturesMobs.DEPS)
public class NaturesMobs {
    public static final String MOD_ID = "naturesmobs";
    public static final String MOD_NAME = "Nature's Mobs";
    public static final String VERSION = "@VERSION@";
    public static final String DEPS = "required-after:naturesaura@b4;required-after:patchouli;";

    @Mod.Instance(MOD_ID)
    public static NaturesMobs instance;

    @SidedProxy(serverSide = "net.polarizedions.naturesmobs.proxy.ServerProxy", clientSide = "net.polarizedions.naturesmobs.proxy.ClientProxy")
    public static IProxy proxy;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("{} is loading...", MOD_NAME);

        proxy.registerTESRs();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            ModItems.register(event.getRegistry());
            ModBlocks.registerItemBlocks(event.getRegistry());
        }

        @SubscribeEvent
        public static void registerModels(ModelRegistryEvent event) {
            ModItems.registerModels();
            ModBlocks.registerModels();
        }

        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            ModBlocks.register(event.getRegistry());
        }
    }
}
