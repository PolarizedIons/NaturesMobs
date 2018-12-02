package net.polarizedions.naturesmobs.net;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.polarizedions.naturesmobs.NaturesMobs;

public class ModPackets {
    private static SimpleNetworkWrapper network;

    public static void init() {
        network = new SimpleNetworkWrapper(NaturesMobs.MOD_ID);
        network.registerMessage(PacketFeedAnimalParticle.Handler.class, PacketFeedAnimalParticle.class, 0, Side.CLIENT);
    }

    public static void sendAround(World world, BlockPos pos, int range, IMessage message) {
        network.sendToAllAround(message, new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range));
    }
}
