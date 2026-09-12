package dev.wyedusk.dusksthings.common.content.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wyedusk.dusksthings.common.config.ServerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class GhostsCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> buildCommand() {
        // TODO: get & set ghost statuses via commands
        return Commands.literal("ghosts")
                .executes(GhostsCommand::giveFeatureEnabled);
    }

    private static int giveFeatureEnabled(CommandContext<CommandSourceStack> context) {
        boolean featureEnabled = ServerConfig.GHOSTS_FEATURE_ENABLED.getAsBoolean();
        boolean spectralLensEnabled = ServerConfig.SPECTRAL_LENS_ENABLED.getAsBoolean();
        boolean spectralLensShowsInvisibleEntities = ServerConfig.SPECTRAL_LENS_SHOWS_INVISIBLE_ENTITIES.getAsBoolean();
        boolean spectralAppleEnabled = ServerConfig.SPECTRAL_APPLE_ENABLED.getAsBoolean();
        int spectralAppleWaitTime = ServerConfig.SPECTRAL_APPLE_WAIT_TIME.getAsInt();

        CommandSourceStack source = context.getSource();
        MutableComponent message = Component.empty().plainCopy();
        message.append(Component.literal("Ghosts Feature: ").withStyle(ChatFormatting.LIGHT_PURPLE));
        message.append(Component.literal(featureEnabled ? "Enabled" : "Disabled").withStyle(featureEnabled ? ChatFormatting.GREEN : ChatFormatting.RED));
        if (featureEnabled) {
            message.append(Component.literal("\n").withStyle(ChatFormatting.RESET));
            message.append(Component.literal("  Spectral Lens: ").withStyle(ChatFormatting.LIGHT_PURPLE));
            message.append(Component.literal(spectralLensEnabled ? "Enabled" : "Disabled").withStyle(spectralLensEnabled ? ChatFormatting.GREEN : ChatFormatting.RED));

            if (spectralLensEnabled) {
                message.append(Component.literal("\n").withStyle(ChatFormatting.RESET));
                message.append(Component.literal("    Shows Invisible Entities: ").withStyle(ChatFormatting.LIGHT_PURPLE));
                message.append(Component.literal(spectralLensShowsInvisibleEntities ? "Yes" : "No").withStyle(spectralLensShowsInvisibleEntities ? ChatFormatting.GREEN : ChatFormatting.RED));
            }

            message.append(Component.literal("\n").withStyle(ChatFormatting.RESET));
            message.append(Component.literal("  Spectral Apple: ").withStyle(ChatFormatting.LIGHT_PURPLE));
            message.append(Component.literal(spectralAppleEnabled ? "Enabled" : "Disabled").withStyle(spectralAppleEnabled ? ChatFormatting.GREEN : ChatFormatting.RED));
            if (spectralAppleEnabled) {
                message.append(Component.literal("\n").withStyle(ChatFormatting.RESET));
                message.append(Component.literal("    Wait Time: ").withStyle(ChatFormatting.LIGHT_PURPLE));
                message.append(Component.literal(String.valueOf(spectralAppleWaitTime)).withStyle(ChatFormatting.YELLOW));
                message.append(Component.literal("s").withStyle(ChatFormatting.GRAY));
            }
        }

        source.sendSystemMessage(message);
        return Command.SINGLE_SUCCESS;
    }
}
