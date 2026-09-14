package dev.wyedusk.dusksthings.common.content.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.wyedusk.dusksthings.common.config.ServerConfig;
import dev.wyedusk.dusksthings.common.content.Contents;
import dev.wyedusk.dusksthings.common.content.mechanic.ghosts.GhostDataAttachmentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class GhostsCommand {
    public static LiteralArgumentBuilder<CommandSourceStack> buildCommand() {
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal("ghosts");
        command.then(Commands.literal("get")
                .then(Commands.argument("entity", EntityArgument.entity())
                        .executes(GhostsCommand::getGhostStatusOnEntity))
                .executes(GhostsCommand::getGhostStatusOnSelf));

        command.then(Commands.literal("set")
                .requires(predicate -> predicate.hasPermission(2))
                .then(Commands.argument("ghost", BoolArgumentType.bool())
                        .then(Commands.argument("permanent", BoolArgumentType.bool())
                                .then(Commands.argument("entity", EntityArgument.entity())
                                        .executes(GhostsCommand::setGhostStatusOnEntity))
                                .executes(GhostsCommand::setGhostStatusOnSelf))
                        .executes(DTCommand::unfinishedCommandError))
                .executes(DTCommand::unfinishedCommandError));

        command.executes(GhostsCommand::giveFeatureEnabled);
        return command;
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

    private static int getGhostStatusOnSelf(CommandContext<CommandSourceStack> context) {
        Entity entity = context.getSource().getEntity();
        return getGhostStatus(context, entity, true);
    }
    private static int getGhostStatusOnEntity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(context, "entity");
        return getGhostStatus(context, entity, false);
    }

    private static int getGhostStatus(CommandContext<CommandSourceStack> context, Entity entity, boolean isSelf) {
        CommandSourceStack source = context.getSource();

        if (entity instanceof LivingEntity) {
            GhostDataAttachmentType data = entity.getData(Contents.AttachmentTypes.GHOST_DATA.get());
            boolean isTempGhost = data.isTemporaryGhost();
            boolean isPermGhost = data.isPermanentGhost();
            Component name = isSelf ? Component.literal("You") : entity.getName();
            source.sendSuccess(() -> name.copy().append(Component.literal(" %s %s temporary ghost and ".formatted(isSelf ? "are" : "is", isTempGhost ? "a" : "not a")).append(Component.literal("%s %s permanent ghost".formatted(isSelf ? "are" : "is", isPermGhost ? "a" : "not a"))).withStyle(ChatFormatting.RESET)), false);
        } else {
            source.sendFailure(Component.literal("Provided entity isn't capable of being a ghost!"));
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int setGhostStatusOnSelf(CommandContext<CommandSourceStack> context) {
        Entity entity = context.getSource().getEntity();
        boolean isGhost = BoolArgumentType.getBool(context, "ghost");
        boolean isPermanent = BoolArgumentType.getBool(context, "permanent");
        return setGhostStatus(context, entity, isGhost, isPermanent, true);
    }
    private static int setGhostStatusOnEntity(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity entity = EntityArgument.getEntity(context, "entity");
        boolean isGhost = BoolArgumentType.getBool(context, "ghost");
        boolean isPermanent = BoolArgumentType.getBool(context, "permanent");
        return setGhostStatus(context, entity, isGhost, isPermanent, false);
    }

    private static int setGhostStatus(CommandContext<CommandSourceStack> context, Entity entity, boolean isGhost, boolean isPermanent, boolean isSelf) {
        CommandSourceStack source = context.getSource();

        if (entity instanceof LivingEntity) {
            Component name = isSelf ? Component.literal("You") : entity.getName();
            GhostDataAttachmentType data = entity.getData(Contents.AttachmentTypes.GHOST_DATA.get());
            boolean isTempGhost = !isPermanent ? isGhost : data.isTemporaryGhost();
            boolean isPermGhost = isPermanent ? isGhost : data.isPermanentGhost();
            entity.setData(Contents.AttachmentTypes.GHOST_DATA.get(), new GhostDataAttachmentType(isPermGhost, isTempGhost));
            source.sendSuccess(() -> name.copy().append(Component.literal(" %s %s %s %s ghost.".formatted(isSelf ? "are" : "is", isGhost ? "now" : "no longer", isPermanent ? "permanently" : "temporarily", isGhost ? "a" : "not a")).withStyle(ChatFormatting.RESET)), true);
        } else {
            source.sendFailure(Component.literal("Provided entity isn't capable of being a ghost!"));
        }

        return Command.SINGLE_SUCCESS;
    }
}
