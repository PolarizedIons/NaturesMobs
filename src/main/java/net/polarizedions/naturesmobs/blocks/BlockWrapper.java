package net.polarizedions.naturesmobs.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.polarizedions.naturesmobs.registry.IModBlock;

public class BlockWrapper extends Block implements IModBlock {
    protected String name;

    public BlockWrapper(String name, Material material) {
        super(material);
        this.name = name;
    }

    @Override
    public String getBaseName() {
        return name;
    }
}
