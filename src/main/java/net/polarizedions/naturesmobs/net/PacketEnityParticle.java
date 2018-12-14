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

public class PacketEnityParticle implements IMessage {
    private int entityId;
    private ParticleType type;

    public PacketEnityParticle() {
        this.entityId = -1;
        this.type = null;
    }

    public PacketEnityParticle(EntityAnimal animal, ParticleType type) {
        this.entityId = animal.getEntityId();
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.type = ParticleType.fromIndex(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.type.ordinal());
    }

    public enum ParticleType {
        FEEDER,
        CREEPER_REPELLENT;

        // Copied from superaxander <3
        public static ParticleType fromIndex(int index) {
            if (index < 0 || index >= values().length) {
                NaturesMobs.logger.error("Received invalid particle index: {}", index);
                return values()[0];
            }
            return values()[index];
        }
    }

    public static class Handler implements IMessageHandler<PacketEnityParticle, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PacketEnityParticle message, MessageContext ctx) {
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
                            // TODO: use type to render different types
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
