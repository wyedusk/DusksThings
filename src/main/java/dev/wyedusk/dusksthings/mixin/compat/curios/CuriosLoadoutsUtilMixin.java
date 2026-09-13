package dev.wyedusk.dusksthings.mixin.compat.curios;

import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.LoadoutEntry;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.LoadoutsUtil;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.compat.LoadoutCuriosEntry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
        LoadoutEntry loadout = LoadoutsUtil.getLoadout(player, loadoutNumber);
        List<LoadoutCuriosEntry> curiosEntries = loadout.curiosEntries();
        Optional<ICuriosItemHandler> optCuriosInventory = CuriosApi.getCuriosInventory(player);

        optCuriosInventory.ifPresent(ICuriosItemHandler::reset);
        optCuriosInventory.ifPresent(curiosInventory -> curiosEntries.forEach(entry ->
                curiosInventory.setEquippedCurio(entry.identifier(), entry.slot(), entry.functionalStack())));
    }
}
