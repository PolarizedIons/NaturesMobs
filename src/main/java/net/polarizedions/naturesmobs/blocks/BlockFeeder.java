package net.polarizedions.naturesmobs.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.polarizedions.naturesmobs.blocks.tiles.TileEntityFeeder;
import net.polarizedions.naturesmobs.blocks.tiles.TileEntityProviderWrapper;
import net.polarizedions.naturesmobs.blocks.tiles.render.RenderFeeder;

import javax.annotation.Nullable;
import java.util.List;

public class BlockFeeder extends TileEntityProviderWrapper {
    private static final AxisAlignedBB AABB_FULL_BLOCK = new AxisAlignedBB(0, 0, 0, 1, 11/16D, 1);
    private static final AxisAlignedBB AABB_FEEDER = new AxisAlignedBB(0, 0, 0, 1, 7/16D, 1);
    private static final AxisAlignedBB AABB_POLE = new AxisAlignedBB(6/16D, 1/16D, 6/16D, 10/16D, 11/16D, 10/16D);

    public BlockFeeder() {
        super("feeder", Material.WOOD, TileEntityFeeder.class);

        this.setHardness(3F);
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
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        TileEntityFeeder feeder = (TileEntityFeeder) world.getTileEntity(pos);
        ItemStack currInventory = feeder.getInventory().getStackInSlot(0);
        ItemStack playerStack = player.getHeldItem(hand).copy();

        // Extract
        if (playerStack.isEmpty() || (! currInventory.isEmpty() && !currInventory.isItemEqual(playerStack))) {
            feeder.getInventory().setStackInSlot(0, ItemStack.EMPTY);
            if (playerStack.isEmpty()) {
                player.setHeldItem(hand, currInventory);
            }
            else if (! player.inventory.addItemStackToInventory(currInventory)) {
                BlockPos spawnPos = pos.offset(facing);
                if (world.isRemote) {
                    world.spawnEntity(new EntityItem(world, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), currInventory));
                }
            }
        }
        // Insert (dick joke here)
        else {
            ItemStack remain = feeder.getInventory().insertItem(0, playerStack, false);
            if (!player.isCreative()) {
                player.setHeldItem(hand, remain);
            }
        }
        return true;
    }

    @Override
    public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        BlockPos down = pos.down();
        if (! world.getBlockState(down).isSideSolid(world, down, EnumFacing.UP)) {
            return false;
        }

        return super.canPlaceBlockAt(world, pos);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasTESR() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TileEntitySpecialRenderer createTESR() {
        return new RenderFeeder();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityFeeder();
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }
}
