package net.polarizedions.naturesmobs.items;

import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.polarizedions.naturesmobs.entity.ai.EntityAIAvoidPlayerWithItem;


public class ItemCreeperPendant extends ItemBase {
    public static final int CREEPER_AVOID_DISTANCE = 32;
    public static final int TICKS_PER_AURA_DRAIN = 10;
    public static final int AURA_DRAIN = 300;

    public ItemCreeperPendant() {
        super("creeper_pendant");

        // Need to do this, because for whatever reason, annotating the class with @Mod.EventBusSubscriber, the
        // onCreeperCreation event is called waaay too early or something, the entity isn't initialized after waiting
        // a few ticks *shrug*
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCreeperCreation(EntityJoinWorldEvent event) {
        if (event.getEntity().world.isRemote) {
            return;
        }
        if (! (event.getEntity() instanceof EntityCreeper)) {
            return;
        }
//        System.out.println("beep");
        EntityCreeper creeper = (EntityCreeper) event.getEntity();
        // Need to wait till the creeper is initialized before be do something
        // Technically something could go wrong in this one tick...
//        NaturesMobs.proxy.schedule(() -> {
            // Add avoiding player

            if (creeper.tasks == null) {
                System.err.println("NOOOOOOoooooo");
                return;
            }

            creeper.tasks.addTask(3, new EntityAIAvoidPlayerWithItem(creeper, CREEPER_AVOID_DISTANCE, 1D, 1.5D, this));

//        });
    }

}
