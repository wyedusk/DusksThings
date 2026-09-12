package dev.wyedusk.dusksthings.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.wyedusk.dusksthings.common.config.ServerConfig;
import dev.wyedusk.dusksthings.common.content.mechanic.ghosts.GhostsUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "renderShadow", at = @At("HEAD"), cancellable = true)
    private static void dusksthings$renderShadow(PoseStack poseStack, MultiBufferSource buffer, Entity entity, float p_114461_, float p_114462_, LevelReader level, float p_114464_, CallbackInfo ci) {
        if (!ServerConfig.GHOSTS_FEATURE_ENABLED.getAsBoolean()) return;
        if (entity instanceof LivingEntity living)
            if (GhostsUtil.isGhost(living)) ci.cancel();
    }
}