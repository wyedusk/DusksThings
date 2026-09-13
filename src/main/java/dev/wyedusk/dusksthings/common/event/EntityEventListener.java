package dev.wyedusk.dusksthings.common.event;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.content.Contents;
import dev.wyedusk.dusksthings.common.content.mechanic.ghosts.GhostDataAttachmentType;
import dev.wyedusk.dusksthings.common.network.packet.S2CSyncGhostPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = DusksThings.MODID)
public class EntityEventListener {
    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (entity instanceof ItemEntity itemEntity) {
            // Create Spectral Apple via Soul Burning
            if (itemEntity.isRemoved() && itemEntity.getItem().is(Items.APPLE)) {
                BlockPos pos = entity.blockPosition();
                Vec3 entityPos = entity.position();
                Level level = entity.level();
                if (level.getBlockState(pos).is(Blocks.SOUL_FIRE)) {
                    ItemEntity spectralAppleEntity = new ItemEntity(
                            level,
                            entityPos.x, entityPos.y, entityPos.z,
                            Contents.Items.SPECTRAL_APPLE.toStack(itemEntity.getItem().getCount())
                    );
                    level.addFreshEntity(spectralAppleEntity);
                    spectralAppleEntity.setDeltaMovement(Vec3.ZERO);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getTarget() instanceof LivingEntity entity)) return;
        GhostDataAttachmentType ghostData = entity.getData(Contents.AttachmentTypes.GHOST_DATA.get());
        PacketDistributor.sendToPlayer(
                (ServerPlayer) event.getEntity(),
                new S2CSyncGhostPacket(entity.getId(), ghostData)
        );
    }
}
