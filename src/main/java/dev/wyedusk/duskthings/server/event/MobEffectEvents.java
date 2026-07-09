package dev.wyedusk.duskthings.server.event;

import dev.wyedusk.duskthings.DTConfig;
import dev.wyedusk.duskthings.DuskThings;
import dev.wyedusk.duskthings.network.packet.ClientboundSyncGhostPacket;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = DuskThings.MODID)
public class MobEffectEvents {
    @SubscribeEvent
    public static void onEffectExpired(
            MobEffectEvent.Expired event) {
        if (!DTConfig.ghostsFeatureEnabled) return;
        if (event.getEffectInstance() != null && event.getEffectInstance().is(DuskThings.SPECTRAL_TRANSFORMATION_MOB_EFFECT.getDelegate())) {
            LivingEntity entity = event.getEntity();
            entity.setData(DuskThings.IS_GHOST, true);
            PacketDistributor.sendToPlayersTrackingEntity(
                    entity,
                    new ClientboundSyncGhostPacket(entity.getId(), true)
            );
        }
    }
}
