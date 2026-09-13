package dev.wyedusk.dusksthings.common.content.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import dev.wyedusk.dusksthings.common.DusksThings;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = DusksThings.MODID)
public class DTCommand {
    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(buildCommand());
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildCommand() {
        LiteralArgumentBuilder<CommandSourceStack> baseCommand = Commands.literal(DusksThings.MODID);
        baseCommand.then(GhostsCommand.buildCommand());
        baseCommand.executes(DTCommand::giveModInfo);
        return baseCommand;
    }

    private static int giveModInfo(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        MutableComponent message = Component.empty().plainCopy();
        message.append(Component.literal("%s ".formatted(DusksThings.MODNAME)).withStyle(ChatFormatting.LIGHT_PURPLE));
        message.append(Component.literal("v").withStyle(ChatFormatting.GRAY));
        message.append(Component.literal("%s.%s.%s".formatted(DusksThings.MODVER.getMajorVersion(),DusksThings.MODVER.getMinorVersion(),DusksThings.MODVER.getIncrementalVersion())).withStyle(ChatFormatting.LIGHT_PURPLE));
        source.sendSystemMessage(message);
        return Command.SINGLE_SUCCESS;
    }

    public static int unfinishedCommandError(CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        source.sendFailure(Component.literal("Unfinished command!"));
        return Command.SINGLE_SUCCESS;
    }
}
