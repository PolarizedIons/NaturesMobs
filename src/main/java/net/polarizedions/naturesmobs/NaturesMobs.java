package net.polarizedions.naturesmobs;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = NaturesMobs.MOD_ID, name = NaturesMobs.MOD_NAME, version = NaturesMobs.VERSION, dependencies = NaturesMobs.DEPS)
public class NaturesMobs {
    public static final String MOD_ID = "naturesmobs";
    public static final String MOD_NAME = "Nature's Mobs";
    public static final String VERSION = "@VERSION@";
    public static final String DEPS = "required-after:naturesaura@b4;required-after:patchouli;";

    @Mod.Instance(MOD_ID)
    public static NaturesMobs instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("{} is loading...", MOD_NAME);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {

    }
}
