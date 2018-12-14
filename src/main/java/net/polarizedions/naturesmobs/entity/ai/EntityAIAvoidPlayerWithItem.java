package net.polarizedions.naturesmobs.entity.ai;

import com.google.common.base.Predicate;
import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.polarizedions.naturesmobs.items.ItemCreeperPendant;

public class EntityAIAvoidPlayerWithItem extends EntityAIAvoidEntity<EntityPlayer> {
    protected Item toAvoid;
    protected EntityPlayer lastTarget;
    protected long lastScaredTick = 0;

    public EntityAIAvoidPlayerWithItem(EntityCreature entity, float avoidDistance, double farSpeed, double nearSpeed, Item toAvoid) {
        super(entity, EntityPlayer.class, avoidDistance, farSpeed, nearSpeed);
        this.toAvoid = toAvoid;
        System.out.println("new avoid player!");

        // Monkey patch ftw!
        Predicate<EntityPlayer> oldCheck = ObfuscationReflectionHelper.getPrivateValue(EntityAIAvoidEntity.class, this, "field_179509_a" /*"canBeSeenSelector"*/);

        ObfuscationReflectionHelper.setPrivateValue(EntityAIAvoidEntity.class, this, (Predicate<EntityPlayer>) player -> this.shouldRunAway(player, oldCheck), "field_179509_a" /*"canBeSeenSelector"*/);
    }

    private boolean shouldRunAway(EntityPlayer player, Predicate<EntityPlayer> oldCanSee) {
//        System.out.println(oldShouldRunAway);


        long thisTick = player.world.getTotalWorldTime();
        if (this.lastTarget == player && (this.lastScaredTick + ItemCreeperPendant.TICKS_PER_AURA_DRAIN) > thisTick) {
            System.out.println("DRAIN AURA !!!");
            this.lastScaredTick = thisTick;
        }
        else if (this.lastTarget == null || this.lastTarget != player) {
            this.lastScaredTick = thisTick;
            System.out.println("DRAIN AURA with new player!!!");
            this.lastTarget = player;
        }
        boolean hasitem = this.hasItem(player);
if (! hasitem) {
    return !oldCanSee.apply(player);
}
        boolean success = NaturesAuraAPI.instance().extractAuraFromPlayer(player, ItemCreeperPendant.AURA_DRAIN, false);

        if (success) {

            boolean result = hasitem || !oldCanSee.apply(player);
//            System.out.println("shouldRunAway called with player " + player + ", got " + result + "(" + hasitem + ", " + (!oldCanSee.apply(player)) + ")");
            return result;
        }
        else {
            return !oldCanSee.apply(player);
        }
    }

    private boolean hasItem(EntityPlayer player) {
//        System.out.println("avoid: " + (player instanceof EntityPlayer && player.isEntityAlive() && (player.getHeldItemMainhand().getItem() == toAvoid || player.getHeldItemOffhand().getItem() == toAvoid)));
//        System.out.println("avoiding " + player);
        return player != null && player.isEntityAlive() && (player.getHeldItemMainhand().getItem() == this.toAvoid || player.getHeldItemOffhand().getItem() == this.toAvoid);
    }

}
