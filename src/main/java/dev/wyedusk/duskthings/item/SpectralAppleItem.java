package dev.wyedusk.duskthings.item;

import dev.wyedusk.duskthings.DTConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.fml.ModList;
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
}
