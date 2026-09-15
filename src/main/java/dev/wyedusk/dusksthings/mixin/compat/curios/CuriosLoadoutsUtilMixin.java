package dev.wyedusk.dusksthings.mixin.compat.curios;

import dev.wyedusk.dusksthings.common.config.ServerConfig;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.LoadoutEntry;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.LoadoutsUtil;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.compat.LoadoutCuriosEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Mixin(LoadoutsUtil.class)
public class CuriosLoadoutsUtilMixin {
    @Unique
    private static final ThreadLocal<Player> CURRENT_PLAYER = new ThreadLocal<>();

    @Inject(method = "saveEquipmentToLoadout", at = @At("HEAD"))
    private static void dusksthings$saveEquipmentToLoadout$getCurrentPlayer(Player player, int loadoutNumber, CallbackInfo ci) {
        CURRENT_PLAYER.set(player);
    }

    @ModifyVariable(method = "saveEquipmentToLoadout", at = @At("STORE"), name = "curiosEntries")
    private static List<LoadoutCuriosEntry> dusksthings$saveEquipmentToLoadout$modifyCuriosEntries(List<LoadoutCuriosEntry> original) {
        if (!ServerConfig.LOADOUTS_AFFECTS_CURIOS.getAsBoolean()) return original;
        Player player = CURRENT_PLAYER.get();
        Optional<ICuriosItemHandler> optCuriosInventory = CuriosApi.getCuriosInventory(player);
        if (optCuriosInventory.isPresent()) {
            ICuriosItemHandler curiosInventory = optCuriosInventory.get();
            List<LoadoutCuriosEntry> entries = new ArrayList<>();
            curiosInventory.getCurios().forEach((identifier, slotHandler) -> {
                for (int slot = 0; slot < slotHandler.getSlots(); slot++) {
                    ItemStack stack = slotHandler.getStacks().getStackInSlot(slot);
                    if (!stack.isEmpty())
                        entries.add(new LoadoutCuriosEntry(identifier, slot, stack.copy(), ItemStack.EMPTY));
                }
            });
            return entries;
        }
        return original;
    }

    @Inject(method = "equipLoadout", at = @At("TAIL"))
    private static void dusksthings$equipLoadout(Player player, int loadoutNumber, CallbackInfo ci) {
        if (!ServerConfig.LOADOUTS_AFFECTS_CURIOS.getAsBoolean()) return;
        LoadoutEntry loadout = LoadoutsUtil.getLoadout(player, loadoutNumber);
        List<LoadoutCuriosEntry> curiosEntries = loadout.curiosEntries();
        Optional<ICuriosItemHandler> optCuriosInventory = CuriosApi.getCuriosInventory(player);
        ArrayList<ItemStack> items = new ArrayList<>();
        curiosEntries.forEach(entry -> items.add(entry.functionalStack()));
        optCuriosInventory.ifPresent(ICuriosItemHandler::reset);
        // First Curios loadout equip
        ArrayList<ItemStack> leftoverItems = dusksthings$equipLoadoutCuriosIteration(player, curiosEntries, optCuriosInventory, items);
        // Second Curios loadout equip to handle leftover items
        UUID playerUUID = player.getUUID();
        MinecraftServer server = player.getServer();
        if (server != null) {
            NeoForge.EVENT_BUS.addListener(new Consumer<ServerTickEvent.Post>() {
                private int ticksElapsed = 0;

                public void accept(ServerTickEvent.Post event) {
                    ticksElapsed++;
                    if (ticksElapsed >= 1) {
                        try {
                            ServerPlayer currentPlayer = server.getPlayerList().getPlayer(playerUUID);
                            ArrayList<ItemStack> finalLeftoverItems = dusksthings$equipLoadoutCuriosIteration(player, curiosEntries, optCuriosInventory, leftoverItems);
                            if (currentPlayer != null && currentPlayer.isAlive()) {
                                finalLeftoverItems.forEach(leftoverItem -> ItemHandlerHelper.giveItemToPlayer(currentPlayer, leftoverItem));
                            }
                        } finally {
                            NeoForge.EVENT_BUS.unregister(this);
                        }
                    }
                }
            });
        }
    }

    @Unique
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private static ArrayList<ItemStack> dusksthings$equipLoadoutCuriosIteration(Player player, List<LoadoutCuriosEntry> curiosEntries, Optional<ICuriosItemHandler> optCuriosInventory, ArrayList<ItemStack> items) {
        optCuriosInventory.ifPresent(curiosInventory -> {
            curiosEntries.forEach(entry -> {
                curiosInventory.setEquippedCurio(entry.identifier(), entry.slot(), entry.functionalStack());
                final boolean[] hasRemoved = {false};
                if (curiosInventory.isEquipped(entry.functionalStack().getItem())) {
                    items.removeIf(stack -> {
                        if (stack.getItem() == entry.functionalStack().getItem() && !hasRemoved[0]) {
                            hasRemoved[0] = true;
                            return true;
                        }
                        return false;
                    });
                }
            });
        });
        return items;
    }
}
