package net.polarizedions.naturesmobs.net;

import de.ellpeck.naturesaura.api.NaturesAuraAPI;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.polarizedions.naturesmobs.NaturesMobs;

public class PacketFeedAnimalParticle implements IMessage {
    private int entityId;

    public PacketFeedAnimalParticle() {
        this.entityId = -1;
    }

    public PacketFeedAnimalParticle(EntityAnimal animal) {
        this.entityId = animal.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    public static class Handler implements IMessageHandler<PacketFeedAnimalParticle, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketFeedAnimalParticle message, MessageContext ctx) {
            NaturesMobs.proxy.schedule(() -> {
                World world = Minecraft.getMinecraft().world;
                if (world != null) {
                    Entity entity = world.getEntityByID(message.entityId);
                    if (entity != null) {
                        BlockPos pos = entity.getPosition();
                        float posX = pos.getX() + entity.width / 2;
                        float posY = pos.getY() + entity.height - (entity.height / 4);
                        float posZ = pos.getZ() + entity.width / 2;

                        for (int i = world.rand.nextInt(5) + 5; i >= 0; i--) {
                            NaturesAuraAPI.instance().spawnMagicParticle(
                                    posX+ world.rand.nextFloat(),
                                    posY + world.rand.nextFloat() * 0.5F,
                                    posZ + world.rand.nextFloat(),
                                    world.rand.nextFloat() * 0.02F - 0.01f, world.rand.nextFloat() * 0.02F, world.rand.nextFloat() * 0.02F- 0.01f,
                                    0xf441af, 2F + world.rand.nextFloat() * 2F, 100, 0F, false, true);
                        }
                    }
                }
            });

            return null;
        }
    }
}
