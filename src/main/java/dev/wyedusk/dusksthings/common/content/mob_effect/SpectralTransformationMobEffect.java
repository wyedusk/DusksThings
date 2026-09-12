package dev.wyedusk.dusksthings.common.content.mob_effect;

import dev.wyedusk.dusksthings.common.content.Contents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class SpectralTransformationMobEffect extends MobEffect {
    public SpectralTransformationMobEffect() {
        super(MobEffectCategory.NEUTRAL, 0xBAFFFF);
    }

    @Override
    public void onMobHurt(@NotNull LivingEntity entity, int amplifier, @NotNull DamageSource source, float amount) {
        if (amount > 0 && !entity.level().isClientSide()) {
            entity.removeEffect(Contents.MobEffects.SPECTRAL_TRANSFORMATION_MOB_EFFECT.getDelegate());
        }
    }
}