package dev.wyedusk.duskthings.network;

import dev.wyedusk.duskthings.DuskThings;
import dev.wyedusk.duskthings.network.packet.ClientboundSyncExtraHealthPacket;
import dev.wyedusk.duskthings.network.packet.ClientboundSyncGhostPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = DuskThings.MODID)
public class NetworkHandler implements IModBusEvent {
    @SubscribeEvent
    public static void registerPayloads(
            RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(DuskThings.MODID);

        registrar.playToClient(
                ClientboundSyncGhostPacket.TYPE,
                ClientboundSyncGhostPacket.CODEC,
                (payload, context) -> context.enqueueWork(() -> handleClientboundSyncGhostPacket(payload))
        );
        registrar.playToClient(
                ClientboundSyncExtraHealthPacket.TYPE,
                ClientboundSyncExtraHealthPacket.CODEC,
                (payload, context) -> context.enqueueWork(() -> handleClientboundSyncExtraHealthPacket(payload))
        );
    }

    private static void handleClientboundSyncGhostPacket(
            ClientboundSyncGhostPacket payload) {
        if (Minecraft.getInstance().level != null) {
            Entity entity = Minecraft.getInstance().level.getEntity(payload.entityId());
            if (entity != null) {
                entity.setData(DuskThings.IS_GHOST, payload.isGhost());
            }
        }
    }

    private static void handleClientboundSyncExtraHealthPacket(
            ClientboundSyncExtraHealthPacket payload) {
        if (Minecraft.getInstance().level != null) {
            LivingEntity entity = (LivingEntity) Minecraft.getInstance().level.getEntity(payload.entityId());
            if (entity != null) {
                entity.setData(DuskThings.EXTRA_HEALTH, payload.extraHealth());
            }
        }
    }
}