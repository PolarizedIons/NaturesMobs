package net.polarizedions.naturesmobs.blocks.tiles;

import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerWrapper extends ItemStackHandler {
    private final TileEntityWrapper tile;
    private boolean autoSync;
    private int[] stackSizes;

    public ItemStackHandlerWrapper(int size, TileEntityWrapper tile) {
        this(size, tile, true);
    }

    public ItemStackHandlerWrapper(int size, TileEntityWrapper tile, boolean autoSync) {
        super(size);
        this.tile = tile;
        this.autoSync = autoSync;
        this.stackSizes = new int[size];

        for (int i = 0; i < size; i++) {
            this.stackSizes[i] = 64;
        }
    }

    public void increaseSlot(int slot) {
        this.increaseSlot(slot, 1);
    }

    public void increaseSlot(int slot, int amount) {
        this.getStackInSlot(slot).grow(amount);
        this.onContentsChanged(slot);
    }

    public void decreaseSlot(int slot) {
        this.decreaseSlot(slot, 1);
    }

    public void decreaseSlot(int slot, int amount) {
        this.getStackInSlot(slot).shrink(amount);
        this.onContentsChanged(slot);
    }

    public ItemStackHandlerWrapper setSlotLimit(int slot, int size) {
        if (slot < this.stackSizes.length) {
            this.stackSizes[slot] = size;
        }
        return this;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return slot >= this.stackSizes.length ? 0 : this.stackSizes[slot];
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (this.autoSync) {
            this.sync();
        }
    }

    public void sync() {
        if (!this.tile.getWorld().isRemote) {
            this.tile.updateNearbyClients();
        }
    }

    public boolean isAutoSync() {
        return autoSync;
    }

    public void setAutoSync(boolean shouldAutoSync) {
        this.autoSync = shouldAutoSync;
    }
}
