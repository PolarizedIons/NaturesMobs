package net.polarizedions.naturesmobs.items;

import net.minecraft.item.Item;
import net.polarizedions.naturesmobs.registry.IModItem;


public class ItemBase extends Item implements IModItem {
    protected String name;

    public ItemBase(String name) {
        this.name = name;
    }

    @Override
    public String getBaseName() {
        return name;
    }
}
