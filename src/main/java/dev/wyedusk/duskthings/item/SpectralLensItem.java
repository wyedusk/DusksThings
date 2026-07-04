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

public class SpectralLensItem extends Item {
    public SpectralLensItem(
            Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        String modifiers = DTConfig.spectralLensShowsInvisible ? "" : ".noinvis";
        if (ModList.get().isLoaded("curios")) {
            tooltip.add(Component.translatable("item.duskthings.spectral_lens.tooltip"+modifiers+".curios", Component.translatable("curios.identifier.charm").withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("item.duskthings.spectral_lens.tooltip"+modifiers).withStyle(ChatFormatting.GRAY));
        }
    }
}
