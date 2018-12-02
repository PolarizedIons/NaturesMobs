package net.polarizedions.naturesmobs.registry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public interface IModBlock extends IModContent {
    default boolean hasItem() {
        return true;
    }

    default ItemBlock createItemBlock() {
        if (! this.hasItem()) {
            return null;
        }

        return new ItemBlock((Block) this);
    }
}
