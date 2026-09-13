package dev.wyedusk.dusksthings.common.content.mechanic.ghosts;

import dev.wyedusk.dusksthings.common.compat.curios.CuriosBridge;
import dev.wyedusk.dusksthings.common.config.ServerConfig;
import dev.wyedusk.dusksthings.common.content.Contents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.ModList;

import java.util.Set;

public class GhostsUtil {
    public static boolean isGhost(LivingEntity entity) {
        GhostDataAttachmentType data = entity.getData(Contents.AttachmentTypes.GHOST_DATA.get());
        return data.isPermanentGhost() || data.isTemporaryGhost();
    }

    public static boolean playerCanAlwaysSeeGhosts(Player player) {
        if (!ServerConfig.GHOSTS_FEATURE_ENABLED.getAsBoolean()) return true;
        return (player.getInventory().hasAnyOf(Set.of(
                Contents.Items.SPECTRAL_LENS.get())) && !ModList.get().isLoaded("curios") && ServerConfig.SPECTRAL_LENS_ENABLED.getAsBoolean())
                || GhostsUtil.isGhost(player)
                || ((player.getMainHandItem().is(Contents.Items.SPECTRAL_LENS) | player.getOffhandItem().is(Contents.Items.SPECTRAL_LENS)) && ServerConfig.SPECTRAL_LENS_ENABLED.getAsBoolean())
                || player.isSpectator()
                || (CuriosBridge.hasCuriosItem(player, Contents.Items.SPECTRAL_LENS.get()) && ModList.get().isLoaded("curios") && ServerConfig.SPECTRAL_LENS_ENABLED.getAsBoolean());
    }
}
