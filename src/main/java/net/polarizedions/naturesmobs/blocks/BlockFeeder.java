package net.polarizedions.naturesmobs.blocks;

import de.ellpeck.naturesaura.Helper;
import de.ellpeck.naturesaura.NaturesAura;
import de.ellpeck.naturesaura.blocks.BlockContainerImpl;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.polarizedions.naturesmobs.NaturesMobs;
import net.polarizedions.naturesmobs.blocks.tiles.TileEntityFeeder;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFeeder extends BlockContainerImpl {
    private static final AxisAlignedBB AABB_FULL_BLOCK = new AxisAlignedBB(0, 0, 0, 1, 11/16D, 1);
    private static final AxisAlignedBB AABB_FEEDER = new AxisAlignedBB(0, 0, 0, 1, 7/16D, 1);
    private static final AxisAlignedBB AABB_POLE = new AxisAlignedBB(6/16D, 1/16D, 6/16D, 10/16D, 11/16D, 10/16D);

    public BlockFeeder() {
//        super("feeder", Material.WOOD);
        super(Material.WOOD, "feeder", TileEntityFeeder.class, "feeder");

        this.setHardness(3F);
        this.setCreativeTab(NaturesAura.CREATIVE_TAB);
        this.setTranslationKey("feeder");
        this.setRegistryName("feeder");
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_FEEDER);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_POLE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB_FULL_BLOCK;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isSideSolid(IBlockState baseState, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return side == EnumFacing.DOWN;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return Helper.putStackOnTile(playerIn, hand, pos, 0, true);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityFeeder();
    }

    public void registerItemModel(Item itemBlock) {
        NaturesMobs.proxy.registerItemRenderer(itemBlock, 0, "feeder");
    }

    public Item createItemBlock() {
        return new ItemBlock(this).setRegistryName(this.getRegistryName());
    }

    @Override
    public BlockFeeder setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }


}
