package net.polarizedions.naturesmobs.blocks.tiles;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.polarizedions.naturesmobs.blocks.BlockWrapper;
import net.polarizedions.naturesmobs.registry.ITileEntityProvider;


public abstract class TileEntityProviderWrapper extends BlockWrapper implements ITileEntityProvider {
    protected Class<? extends TileEntity> tileEntityClass;

    public TileEntityProviderWrapper(String name, Material material, Class<? extends TileEntity> tileEntityClass) {
        super(name, material);
        this.tileEntityClass = tileEntityClass;
    }

    public Class<? extends TileEntity> getTileEntityClass() {
        return this.tileEntityClass;
    }

    public abstract TileEntity createNewTileEntity(World world, int meta);

    public boolean hasTESR() {
        return false;
    }

    public TileEntitySpecialRenderer createTESR() {
        return null;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntityWrapper tile = (TileEntityWrapper) world.getTileEntity(pos);
        tile.dropInventory();
        super.breakBlock(world, pos, state);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        TileEntityWrapper tile = (TileEntityWrapper) world.getTileEntity(pos);
        if (tile != null) {
            drops.add(tile.getDrop(state, fortune));
        }
        else {
            super.getDrops(drops, world, pos, state, fortune);
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        TileEntityWrapper tile = (TileEntityWrapper) world.getTileEntity(pos);
        tile.loadDataOnPlace(stack);
    }
}
