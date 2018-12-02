package net.polarizedions.naturesmobs.blocks.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public abstract class TileEntityWrapper extends TileEntity {

    public boolean hasInventory(@Nullable EnumFacing facing) {
        return this.getInventory(facing) != null;
    }

    public boolean hasInventory() {
        return this.hasInventory(null);
    }

    public abstract ItemStackHandlerWrapper getInventory(@Nullable EnumFacing facing);

    public ItemStackHandlerWrapper getInventory() {
        return this.getInventory(null);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.hasInventory()) || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.hasInventory()) ? (T) getInventory() : super.getCapability(capability, facing);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    // NETWORK SYNC (TE based): to client
    @Override
    public final SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        this.writeNBT(compound, SaveType.NETWORK);
        return new SPacketUpdateTileEntity(this.pos, 0, compound);
    }

    // NETWORK SYNC (TE based): from server
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readNBT(pkt.getNbtCompound(), SaveType.NETWORK);
    }

    // NETWORK SYNC (chunk based): to client
    @Override
    public NBTTagCompound getUpdateTag() {
//        NBTTagCompound compound = super.getUpdateTag();
        NBTTagCompound compound = new NBTTagCompound();
        this.writeNBT(compound, SaveType.NETWORK);
        return compound;
    }

    // NETWORK SYNC (chunk based): from server
    @Override
    public void handleUpdateTag(NBTTagCompound compound) {
//        super.handleUpdateTag(compound);
        this.readNBT(compound, SaveType.NETWORK);
    }

    public void updateNearbyClients() {
        WorldServer world = (WorldServer) this.getWorld();
        PlayerChunkMapEntry entry = world.getPlayerChunkMap().getEntry(this.getPos().getX() >> 4, this.getPos().getZ() >> 4);
        if (entry != null) {
            entry.sendPacket(this.getUpdatePacket());
        }
    }


    // Forge - don't override this
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        this.writeNBT(compound, SaveType.DISK);
        return compound;
    }

    // Forge - don't override this
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.readNBT(compound, SaveType.DISK);
    }


    // My own - override this (but call super!)
    protected void readNBT(NBTTagCompound compound, SaveType type) {
        if ((type == SaveType.NETWORK || type == SaveType.DISK) && this.hasInventory()) {
            this.getInventory().deserializeNBT(compound.getCompoundTag("inventory"));
        }

        super.readFromNBT(compound);
    }

    // My own - override this (but call super!)
    protected void writeNBT(NBTTagCompound compound, SaveType type) {
        if ((type == SaveType.NETWORK || type == SaveType.DISK) && this.hasInventory()) {
            compound.setTag("inventory", this.getInventory().serializeNBT());
        }

        super.writeToNBT(compound);
    }

    public void dropInventory() {
        IItemHandler handler = this.getInventory();
        if (handler != null) {
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack stack = handler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    EntityItem item = new EntityItem(this.world, this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5, stack);
                    this.world.spawnEntity(item);
                }
            }
        }
    }

    public ItemStack getDrop(IBlockState state, int fortune) {
        Block block = state.getBlock();
        ItemStack stack = new ItemStack(
                block.getItemDropped(state, this.world.rand, fortune),
                block.quantityDropped(state, fortune, this.world.rand),
                block.damageDropped(state));

        NBTTagCompound compound = new NBTTagCompound();
        this.writeNBT(compound, SaveType.ITEM);

        if (!compound.isEmpty()) {
            stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setTag("data", compound);
        }

        return stack;
    }

    public void loadDataOnPlace(ItemStack stack) {
        if (stack.hasTagCompound()) {
            NBTTagCompound compound = stack.getTagCompound().getCompoundTag("data");
            if (compound != null) {
                this.readNBT(compound, SaveType.ITEM);
            }
        }
    }


    public enum SaveType {
        NETWORK,
        DISK,
        ITEM,
    }
}
