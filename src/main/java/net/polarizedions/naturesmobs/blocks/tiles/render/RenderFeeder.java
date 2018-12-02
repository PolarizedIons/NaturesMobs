package net.polarizedions.naturesmobs.blocks.tiles.render;

import de.ellpeck.naturesaura.Helper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.MathHelper;
import net.polarizedions.naturesmobs.blocks.tiles.TileEntityFeeder;

import java.util.Random;

public class RenderFeeder extends TileEntitySpecialRenderer<TileEntityFeeder> {

    private final Random rand = new Random();

    @Override
    public void render(TileEntityFeeder tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        ItemStack stack = tile.getInventory().getStackInSlot(0);
        if (!stack.isEmpty()) {
            this.rand.setSeed(Item.getIdFromItem(stack.getItem()) + stack.getMetadata());

            int amount = MathHelper.ceil(stack.getCount() / 1.5F);
            for (int i = 0; i < amount; i++) {
                GlStateManager.pushMatrix();
                Item item = stack.getItem();

                float scale;
                float yOff;
                if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().getRenderLayer() == BlockRenderLayer.SOLID) {
                    scale = 0.4F* 2;
                    yOff = 0.08F + i * 0.08F;
                } else {
                    scale = 0.25F * 2;
                    yOff = 0F + i * (1F / 256F);
                }

                GlStateManager.translate(
                        x + scale / 4 + this.rand.nextFloat() * (1 - scale / 2),
                        y + (2F / 16) + yOff + (i * 0.001F),
                        z + scale / 4 + this.rand.nextFloat() * (1  - scale / 2)
                );
                GlStateManager.rotate(this.rand.nextFloat() * 360F, 0F, 1F, 0F);
                GlStateManager.rotate(90F, 1F, 0F, 0F);
                GlStateManager.scale(scale, scale, scale);

                Helper.renderItemInWorld(stack);
                GlStateManager.popMatrix();
            }
        }
    }

}
