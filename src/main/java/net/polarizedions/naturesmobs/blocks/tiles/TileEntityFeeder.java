package net.polarizedions.naturesmobs.blocks.tiles;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import de.ellpeck.naturesaura.api.aura.chunk.IAuraChunk;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.polarizedions.naturesmobs.net.ModPackets;
import net.polarizedions.naturesmobs.net.PacketFeedAnimalParticle;

import java.util.List;

public class TileEntityFeeder extends TileEntityWrapper implements ITickable {
    private static final int REQUIRED_AURA = 1000;
    private static final int AURA_USE_PER_MOB = 100;
    private static final int AURA_SCAN_RADIUS = 32;

    private ItemStackHandlerWrapper inventory = new ItemStackHandlerWrapper(1, this);


    @Override
    public void update() {
        int aura = IAuraChunk.getAuraInArea(this.world, this.pos, AURA_SCAN_RADIUS);
        boolean enoughAura = aura > REQUIRED_AURA ;
        if (! enoughAura) {
            return;
        }

        ItemStack stack = this.inventory.getStackInSlot(0);
        if (stack.isEmpty()) {
            return;
        }

        if (this.world.isRemote && this.world.getTotalWorldTime() % 4 == 0) {
            NaturesAuraAPI.instance().spawnMagicParticle(
                    this.pos.getX() + 0.5,
                    this.pos.getY() + 0.7,
                    this.pos.getZ() + 0.5,
                    world.rand.nextGaussian() * 0.01,
                    world.rand.nextGaussian() * 0.05,
                    world.rand.nextGaussian() * 0.01,
                    0x48f442,
                    world.rand.nextFloat() * 2 + 1f,
                    world.rand.nextInt(80) + 60,
                    0f,
                    false,
                    true
            );
        }

        if (world.isRemote || this.world.getTotalWorldTime() % 40 != 0) {
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
            enoughAura = aura > REQUIRED_AURA ;
            if (stack.isEmpty() || ! enoughAura) {
                break;
            }

            // TODO: deal with xp orbs spawning
            // EntityAIMate always spawns it :(

            // growing age also determines next baby-making time apparently?
            if (animal.isBreedingItem(stack) && !animal.isInLove() && !animal.isChild() && animal.getGrowingAge() == 0) {
                bred = true;
                animal.setInLove(null);
                this.inventory.decreaseSlot(0);
                aura -= AURA_USE_PER_MOB;
                BlockPos spot = IAuraChunk.getHighestSpot(this.world, this.pos, AURA_SCAN_RADIUS, this.pos);
                IAuraChunk.getAuraChunk(this.world, spot).drainAura(spot, AURA_USE_PER_MOB);
                ModPackets.sendAround(world, pos, 32, new PacketFeedAnimalParticle(animal));
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
