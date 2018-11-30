package net.polarizedions.naturesmobs.proxy;

import net.minecraft.item.Item;

public interface IProxy {
    void registerItemRenderer(Item item, int meta, String id);
    void registerTESRs();
}
