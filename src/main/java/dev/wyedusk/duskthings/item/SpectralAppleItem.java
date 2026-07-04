package dev.wyedusk.duskthings.item;

import dev.wyedusk.duskthings.DTConfig;
import dev.wyedusk.duskthings.DuskThings;
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
    public SpectralAppleItem(
            Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (!DTConfig.ghostsFeatureEnabled || !DTConfig.spectralAppleFunctionality) {
            tooltip.add(Component.translatable("tooltip.duskthings.disabled_item").withStyle(ChatFormatting.RED));
            return;
        }
        tooltip.add(Component.translatable("item.duskthings.spectral_apple.tooltip").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(
            ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        FoodProperties foodproperties = stack.getFoodProperties(entity);
        if (!DTConfig.ghostsFeatureEnabled || !DTConfig.spectralAppleFunctionality) {
            entity.addEffect(new MobEffectInstance(
                    DuskThings.SPECTRAL_TRANSFORMATION_MOB_EFFECT.getDelegate(),
                    DTConfig.spectralAppleWaitTime * 20));
        }
        return foodproperties != null ? entity.eat(level, stack, foodproperties) : stack;
    }
}
