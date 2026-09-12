package dev.wyedusk.dusksthings.common.network;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.network.packet.S2CSyncGhostPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = DusksThings.MODID)
public class NetworkHandler {
    @SubscribeEvent
    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        event.registrar("1")
                .playToClient(
                        S2CSyncGhostPacket.TYPE,
                        S2CSyncGhostPacket.STREAM_CODEC,
                        S2CSyncGhostPacket::handle
                );
    }
}