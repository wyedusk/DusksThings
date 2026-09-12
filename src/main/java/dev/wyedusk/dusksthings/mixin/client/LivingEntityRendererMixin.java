package dev.wyedusk.dusksthings.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.wyedusk.dusksthings.client.config.ClientConfig;
import dev.wyedusk.dusksthings.common.config.ServerConfig;
import dev.wyedusk.dusksthings.common.content.mechanic.ghosts.GhostsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {
    @Shadow
    protected abstract boolean isBodyVisible(LivingEntity p_115341_);

    @Unique
    private static final ThreadLocal<LivingEntity> CURRENT_ENTITY = new ThreadLocal<>();

    @Inject(method = "render", at = @At("HEAD"))
    private void dusksthings$render$captureCurrentEntity(LivingEntity entity, float yaw, float tick, PoseStack pose, MultiBufferSource buffer, int light, CallbackInfo ci) {
        CURRENT_ENTITY.set(entity);
    }

    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"))
    private void dusksthings$render$modifyRenderToBufferArgs(Args args) {
        if (!ServerConfig.GHOSTS_FEATURE_ENABLED.getAsBoolean()) return;

        Minecraft minecraft = Minecraft.getInstance();
        assert minecraft.player != null;
        // Visibility Modifier
        boolean entityInvisible = CURRENT_ENTITY.get().isInvisibleTo(minecraft.player);
        boolean flag1 = !(this.isBodyVisible(CURRENT_ENTITY.get()))
                && !(entityInvisible);
        int targetColor = flag1 ? 654311423 : -1;
        if (entityInvisible && GhostsUtil.playerCanAlwaysSeeGhosts(minecraft.player) && ServerConfig.SPECTRAL_LENS_SHOWS_INVISIBLE_ENTITIES.getAsBoolean()) targetColor = 654311423;
        // Translucency Modifier
        if (GhostsUtil.isGhost(CURRENT_ENTITY.get()) || CURRENT_ENTITY.get().hasEffect(MobEffects.INVISIBILITY)) {
            int alpha = ClientConfig.GHOST_TRANSPARENCY.getAsInt();
            targetColor = (alpha << 24) | (targetColor & 0x00FFFFFF);
        }

        args.set(4, targetColor);
    }


    @Inject(method = "getRenderType", at = @At("HEAD"), cancellable = true)
    private void duskthings$getRenderType(LivingEntity entity, boolean p_115323_, boolean p_115324_, boolean p_115325_, CallbackInfoReturnable<RenderType> cir) {
        Player player = Minecraft.getInstance().player;
        assert player != null;
        if (GhostsUtil.isGhost(entity) && (entity.isInvisibleTo(player) && !GhostsUtil.playerCanAlwaysSeeGhosts(player))) {
            cir.setReturnValue(null);
            return;
        }
        if (entity.isInvisibleTo(player) && !ServerConfig.SPECTRAL_LENS_SHOWS_INVISIBLE_ENTITIES.getAsBoolean()) {
            cir.setReturnValue(null);
            return;
        }
        LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>> renderer = (LivingEntityRenderer<LivingEntity, EntityModel<LivingEntity>>)(Object)this;
        ResourceLocation texture = renderer.getTextureLocation(entity);

        cir.setReturnValue(RenderType.entityTranslucent(texture));
    }
}