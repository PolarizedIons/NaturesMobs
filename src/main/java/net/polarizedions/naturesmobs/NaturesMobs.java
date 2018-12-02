package net.polarizedions.naturesmobs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.polarizedions.naturesmobs.blocks.ModBlocks;
import net.polarizedions.naturesmobs.items.ModItems;
import net.polarizedions.naturesmobs.proxy.IProxy;
import net.polarizedions.naturesmobs.registry.ModRegistry;
import org.apache.logging.log4j.LogManager;
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

    public static Logger logger = LogManager.getLogger(NaturesMobs.MOD_NAME);

    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.RED_FLOWER);
        }
    };

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("{} is loading...", MOD_NAME);

        new ModBlocks();
        new ModItems();

        ModRegistry.onPreInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModRegistry.onInit(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ModRegistry.onPostInit(event);
    }
}
