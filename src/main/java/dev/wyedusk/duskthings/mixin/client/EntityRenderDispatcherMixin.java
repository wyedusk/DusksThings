package dev.wyedusk.duskthings.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.wyedusk.duskthings.DTConfig;
import dev.wyedusk.duskthings.DuskThings;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(method = "renderShadow", at = @At("HEAD"), cancellable = true)
    private static void duskthings$renderShadow(
            PoseStack poseStack, MultiBufferSource buffer, Entity entity, float p_114461_, float p_114462_, LevelReader level, float p_114464_,
            CallbackInfo ci) {
        if (!DTConfig.ghostsFeatureEnabled) return;
        if (entity.getData(DuskThings.IS_GHOST).equals(true)) ci.cancel();
    }
}
