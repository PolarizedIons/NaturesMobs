package net.polarizedions.naturesmobs.blocks.tiles;

import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class TileEntityFeeder extends TileEntityWrapper implements ITickable {

    private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(1, this);

    @Override
    public void update() {
        if (this.world.getTotalWorldTime() % 80 != 0) {
            return;
        }


        if (this.world.isRemote || this.world.getTotalWorldTime() % 40 != 0) {
            return;
        }

        ItemStack stack = this.inventory.getStackInSlot(0);
        if (stack.isEmpty()) {
            return;
        }

        List<EntityAnimal> mobs = this.world.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(this.pos).grow(5, 1, 5));
        if (mobs.isEmpty()) {
            return;
        }

        // Don't sync each individual stack decrease while we breed the animals
        this.inventory.setAutoSync(false);
        boolean bred = false;
        for (EntityAnimal animal : mobs) {
            if (stack.isEmpty()) {
                break;
            }

            // TODO: deal with xp orbs spawning
            // EntityAIMate always spawns it :(

            // growing age also determines next baby-making time apparently?
            if (animal.isBreedingItem(stack) && !animal.isInLove() && !animal.isChild() && animal.getGrowingAge() == 0) {
                bred = true;
                animal.setInLove(null);
                this.inventory.decreaseSlot(0);
            }
        }
        this.inventory.setAutoSync(true);

        if (bred) {
            this.inventory.sync();
        }
    }

    @Override
    public ItemStackHandlerWrapper getInventory(EnumFacing facing) {
        return inventory;
    }


}
