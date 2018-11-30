package net.polarizedions.naturesmobs.blocks.tiles;

import de.ellpeck.naturesaura.blocks.tiles.ItemStackHandlerNA;
import de.ellpeck.naturesaura.blocks.tiles.TileEntityImpl;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import java.util.List;

public class TileEntityFeeder extends TileEntityImpl implements ITickable {

    public TileEntityFeeder() {
        System.out.println("NEW FEEDER TE");
    }

    public final ItemStackHandlerNA items = new ItemStackHandlerNA(1, this, true);

    @Override
    public void sendToClients() {
        super.sendToClients();
        System.out.println("sent to clients");
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        System.out.println("received data");
        System.out.println(packet.getNbtCompound());
    }

    @Override
    public void readNBT(NBTTagCompound compound, SaveType type) {
        super.readNBT(compound, type);
        this.items.deserializeNBT(compound.getCompoundTag("items"));
        System.out.println("read nbt " + type + " " + compound);
    }

    @Override
    public void writeNBT(NBTTagCompound compound, SaveType type) {
        super.writeNBT(compound, type);
        compound.setTag("items", this.items.serializeNBT());
        System.out.println("wrote nbt " + type + " " + compound);
    }

    @Override
    public void update() {
//        if (this.world.getTotalWorldTime() % 80 == 0)
//            System.out.println("[" + (this.world.isRemote ? "Client" : "Server") + "] " + this.items.getSlots() + " " + this.items.getStackInSlot(0));


        if (this.world.isRemote || this.world.getTotalWorldTime() % 40 != 0) {
            return;
        }

        ItemStack stack = this.items.getStackInSlot(0);
        if (stack.isEmpty()) {
            return;
        }

        List<EntityAnimal> mobs = this.world.getEntitiesWithinAABB(EntityAnimal.class, new AxisAlignedBB(this.pos).grow(5));
        if (mobs.isEmpty()) {
            return;
        }

        FakePlayer fake = FakePlayerFactory.getMinecraft((WorldServer) world);
        fake.setHeldItem(EnumHand.MAIN_HAND, stack.copy());
        for (EntityAnimal animal : mobs) {
            if (stack.isEmpty()) {
                break;
            }

            if (animal.isBreedingItem(stack) && !animal.isInLove() && !animal.isChild()) {
//                animal.setInLove(fake);
                animal.processInteract(fake, EnumHand.MAIN_HAND);
            }
        }

        if (fake.getHeldItemMainhand().getCount() != stack.getCount()) {
            items.setStackInSlot(0, fake.getHeldItemMainhand());
        }
    }

    @Override
    public IItemHandlerModifiable getItemHandler(EnumFacing facing) {
        return this.items;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", items.serializeNBT());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        items.deserializeNBT(compound.getCompoundTag("inventory"));
        super.readFromNBT(compound);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)items : super.getCapability(capability, facing);
    }
}
