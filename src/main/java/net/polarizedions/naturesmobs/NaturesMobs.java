package net.polarizedions.naturesmobs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.polarizedions.naturesmobs.blocks.ModBlocks;
import net.polarizedions.naturesmobs.items.ModItems;
import net.polarizedions.naturesmobs.net.ModPackets;
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
        ModPackets.init();
        MinecraftForge.EVENT_BUS.register(this);

        ModRegistry.onPreInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModRegistry.onInit(event);
    }

//    @SubscribeEvent()
//    public void entityConstruct(EntityEvent.EntityConstructing e) {
//        if (e.getEntity() instanceof EntityCreeper) {
////            if (e.entity.getExtendedProperties(PROP_NAME) == null) {
////                e.entity.registerExtendedProperties(PROP_NAME, new ExampleEntityProperty());
////            }
//            proxy.schedule(() -> {
//                if (((EntityCreeper) e.getEntity()).tasks == null) {
//                    return;
//                }
//                System.out.println("ADDING AI TASK TO CREEPER!!!");
//                System.out.println(e.getEntity());
//                System.out.println(((EntityCreeper) e.getEntity()).tasks);
//                System.out.println(ModItems.CREEPER_PENDANT);
//            ((EntityCreeper) e.getEntity()).tasks.addTask(3, new EntityAIAvoidPlayerWithItem((EntityCreature) e.getEntity(), 15, 1.0D, 1.5D, ModItems.CREEPER_PENDANT));
//            EntityAIBase attackRemove = null;
//            EntityAIBase explodeRemove = null;
//
//            for (EntityAITasks.EntityAITaskEntry task : ((EntityCreeper) e.getEntity()).tasks.taskEntries) {
//                if (task.action.getClass() == EntityAICreeperSwell.class) {
//                    explodeRemove = task.action;
//                }
//            }
//
//            for (EntityAITasks.EntityAITaskEntry task : ((EntityCreeper) e.getEntity()).targetTasks.taskEntries) {
////                System.out.println("Task: " + task.getClass() + " " + task.action.getClass());
//                if (task.action.getClass() == EntityAINearestAttackableTarget.class) {
//                    attackRemove = task.action;
//                }
//            }
//                System.out.println("REMOVING TASKS " + attackRemove + " " + explodeRemove);
//            if (attackRemove != null) {
//                System.out.println("replaced target ai");
//                ((EntityCreeper) e.getEntity()).tasks.removeTask(attackRemove);
//                ((EntityCreeper) e.getEntity()).targetTasks.addTask(4, new EntityAINearestAttackableTargetIgnoringHeldItem((EntityCreature) e.getEntity(), EntityPlayer.class, true, ModItems.CREEPER_PENDANT));
//            }
//            if (explodeRemove != null) {
//                System.out.println("replaced explode ai");
//                ((EntityCreeper) e.getEntity()).tasks.removeTask(explodeRemove);
//                ((EntityCreeper) e.getEntity()).tasks.addTask(2, new EntityAICreeperSwellExceptPlayerWithItem((EntityCreeper) e.getEntity(), ModItems.CREEPER_PENDANT));
//            }
//
//            });
//        }
//    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ModRegistry.onPostInit(event);
    }
}
