package dev.wyedusk.dusksthings.common.content.item;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpectralLensItem extends Item {
    public SpectralLensItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (!ServerConfig.GHOSTS_FEATURE_ENABLED.getAsBoolean() || !ServerConfig.SPECTRAL_LENS_ENABLED.getAsBoolean()) {
            tooltip.add(Component.translatable("tooltip." + DusksThings.MODID + ".disabled_item").withStyle(ChatFormatting.RED));
            return;
        }
        String modifiers = ServerConfig.SPECTRAL_LENS_SHOWS_INVISIBLE_ENTITIES.getAsBoolean() ? "" : ".noinvis";
        if (ModList.get().isLoaded("curios")) {
            tooltip.add(Component.translatable("item." + DusksThings.MODID + ".spectral_lens.tooltip"+modifiers+".curios", Component.translatable("curios.identifier.charm").withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.translatable("item." + DusksThings.MODID + ".spectral_lens.tooltip"+modifiers).withStyle(ChatFormatting.GRAY));
        }
    }
}