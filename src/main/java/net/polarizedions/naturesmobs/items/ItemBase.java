package net.polarizedions.naturesmobs.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.polarizedions.naturesmobs.NaturesMobs;


public class ItemBase extends Item {
    protected String name;

    public ItemBase(String name) {
        this.name = name;

        this.setTranslationKey(name);
        this.setRegistryName(name);
    }

    public void registerItemModel() {
        NaturesMobs.proxy.registerItemRenderer(this, 0, name);
    }

    @Override
    public ItemBase setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }
}
