package dev.wyedusk.dusksthings.common.content.item;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.config.ServerConfig;
import dev.wyedusk.dusksthings.common.content.Contents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpectralAppleItem extends Item {
    public SpectralAppleItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (!ServerConfig.GHOSTS_FEATURE_ENABLED.getAsBoolean() || !ServerConfig.SPECTRAL_APPLE_ENABLED.getAsBoolean()) {
            tooltip.add(Component.translatable("tooltip." + DusksThings.MODID + ".disabled_item").withStyle(ChatFormatting.RED));
            return;
        }
        tooltip.add(Component.translatable("item." + DusksThings.MODID + ".spectral_apple.tooltip").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        FoodProperties properties = stack.getFoodProperties(entity);
        if (ServerConfig.GHOSTS_FEATURE_ENABLED.getAsBoolean() && ServerConfig.SPECTRAL_APPLE_ENABLED.getAsBoolean()) {
            entity.addEffect(new MobEffectInstance(
                    Contents.MobEffects.SPECTRAL_TRANSFORMATION_MOB_EFFECT.getDelegate(),
                    ServerConfig.SPECTRAL_APPLE_WAIT_TIME.getAsInt() * 20));
        }
        return properties != null ? entity.eat(level, stack, properties) : stack;
    }
}