package net.polarizedions.naturesmobs.proxy;

import net.minecraft.item.Item;

public class ServerProxy implements IProxy {
    @Override
    public void registerItemRenderer(Item item, int meta, String id) {
        // NOOP
    }

    @Override
    public void registerTESRs() {
        // NOOP
    }
}
