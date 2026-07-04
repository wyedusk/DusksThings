package dev.wyedusk.duskthings.client.renderer;

import dev.wyedusk.duskthings.DTConfig;
import dev.wyedusk.duskthings.DuskThings;
import dev.wyedusk.duskthings.utility.GhostHelper;
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

@EventBusSubscriber(modid = DuskThings.MODID, value = Dist.CLIENT)
public class EntityRenderEvent {
    @SubscribeEvent
    public static void onRenderEntity(
            RenderLivingEvent.Pre<LivingEntity, EntityModel<LivingEntity>> event) {
        if (!DTConfig.ghostsFeatureEnabled) return;

        LivingEntity entity = event.getEntity();
        Player player = Minecraft.getInstance().player;

        if (entity == player && !DTConfig.seeSelfAsGhost) return; // don't hide the main player
        if (entity.getData(DuskThings.IS_GHOST).equals(false)) return;

        boolean canAlwaysSeeGhosts = false;
        if (player != null) {
            canAlwaysSeeGhosts = GhostHelper.playerCanAlwaysSeeGhosts(player);
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
