package net.polarizedions.naturesmobs.registry;

import net.minecraft.item.Item;

public interface IModItem extends IModContent {
    default Item getItem() {
        return (Item) this;
    }
}
