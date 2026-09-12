package dev.wyedusk.dusksthings.client.event;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.config.ServerConfig;
import dev.wyedusk.dusksthings.common.content.mechanic.ghosts.GhostsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

@EventBusSubscriber(modid = DusksThings.MODID, value = Dist.CLIENT)
public class ClientEntityEventListener {
    @SubscribeEvent
    public static void onRenderEntity(RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        if (!ServerConfig.GHOSTS_FEATURE_ENABLED.getAsBoolean()) return;

        LivingEntity entity = event.getEntity();
        Player player = Minecraft.getInstance().player;

        if (entity == player) return; // don't hide the main player
        if (!GhostsUtil.isGhost(entity)) return;

        boolean canAlwaysSeeGhosts = false;
        if (player != null) {
            canAlwaysSeeGhosts = GhostsUtil.playerCanAlwaysSeeGhosts(player);
        }
        if (canAlwaysSeeGhosts) return;

        Minecraft mc = Minecraft.getInstance();
        Entity cameraEntity = mc.getCameraEntity();
        if (cameraEntity == null) return;

        Vec3 lookVector = cameraEntity.getViewVector(1.0F).normalize();
        Vec3 playerEyePos = cameraEntity.getEyePosition(1.0F);
        Vec3 entityPos = entity.position().add(0, entity.getBbHeight() / 2.0, 0);
        Vec3 toEntityVector = entityPos.subtract(playerEyePos).normalize();

        double dotProduct = lookVector.dot(toEntityVector);

        double innerFovBoundary = 0.65;

        boolean isInEdgeZone = dotProduct < innerFovBoundary;

        if (!isInEdgeZone) {
            event.setCanceled(true);
        }
    }
}
