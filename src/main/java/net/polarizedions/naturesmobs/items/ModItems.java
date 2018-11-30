package net.polarizedions.naturesmobs.items;

import de.ellpeck.naturesaura.NaturesAura;
import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {
    public static ItemBase SUMMONING_SPIRIT = new ItemBase("summoning_spirit").setCreativeTab(NaturesAura.CREATIVE_TAB);

    public static void register(IForgeRegistry<Item> registry) {
        registry.registerAll(
                SUMMONING_SPIRIT
        );
    }

    public static void registerModels() {
        SUMMONING_SPIRIT.registerItemModel();
    }
}
