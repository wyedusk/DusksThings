package dev.wyedusk.dusksthings.common.event;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.LoadoutsUtil;
import dev.wyedusk.dusksthings.common.network.packet.S2CSyncLoadoutsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = DusksThings.MODID)
public class PlayerEventListener {
    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new S2CSyncLoadoutsPacket(LoadoutsUtil.getCurrentLoadout(serverPlayer), LoadoutsUtil.getLoadouts(serverPlayer)));
        }
    }
}