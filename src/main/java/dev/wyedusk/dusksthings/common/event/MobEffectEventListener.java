package dev.wyedusk.dusksthings.common.event;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.config.ServerConfig;
import dev.wyedusk.dusksthings.common.content.Contents;
import dev.wyedusk.dusksthings.common.content.mechanic.ghosts.GhostDataAttachmentType;
import dev.wyedusk.dusksthings.common.network.packet.S2CSyncGhostPacket;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = DusksThings.MODID)
public class MobEffectEventListener {
    @SubscribeEvent
    public static void onEffectExpired(MobEffectEvent.Expired event) {
        if (!ServerConfig.GHOSTS_FEATURE_ENABLED.getAsBoolean()) return;
        if (event.getEffectInstance() != null && event.getEffectInstance().is(Contents.MobEffects.SPECTRAL_TRANSFORMATION_MOB_EFFECT.getDelegate())) {
            LivingEntity entity = event.getEntity();
            GhostDataAttachmentType data = new GhostDataAttachmentType(
                    entity.getData(Contents.AttachmentTypes.GHOST_DATA.get()).isPermanentGhost(),
                    true
            );
            entity.setData(Contents.AttachmentTypes.GHOST_DATA.get(), data);
            PacketDistributor.sendToPlayersTrackingEntity(
                    entity,
                    new S2CSyncGhostPacket(entity.getId(), data)
            );
        }
    }
}