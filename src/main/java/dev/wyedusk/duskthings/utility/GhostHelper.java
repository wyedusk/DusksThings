package dev.wyedusk.duskthings.utility;

import dev.wyedusk.duskthings.DTConfig;
import dev.wyedusk.duskthings.DuskThings;
import dev.wyedusk.duskthings.compat.curios.CuriosBridge;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

import java.util.Set;

public class GhostHelper {
    public static boolean playerCanAlwaysSeeGhosts(
            Player player) {
        if (!DTConfig.ghostsFeatureEnabled) return false;
        return (player.getInventory().hasAnyOf(Set.of(DuskThings.SPECTRAL_LENS.get())) && !ModList.get().isLoaded("curios") && DTConfig.spectralLensFunctionality)
                | player.getData(DuskThings.IS_GHOST).equals(true)
                | ((player.getMainHandItem().is(DuskThings.SPECTRAL_LENS) | player.getOffhandItem().is(DuskThings.SPECTRAL_LENS)) && DTConfig.spectralLensFunctionality)
                | player.isSpectator()
                | (CuriosBridge.hasCuriosItem(player, DuskThings.SPECTRAL_LENS.get()) && ModList.get().isLoaded("curios") && DTConfig.spectralLensFunctionality);
    }
}
