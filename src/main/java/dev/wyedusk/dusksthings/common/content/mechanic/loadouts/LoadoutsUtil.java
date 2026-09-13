package dev.wyedusk.dusksthings.common.content.mechanic.loadouts;

import dev.wyedusk.dusksthings.common.content.Contents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;

public class LoadoutsUtil {
    public static List<LoadoutEntry> getLoadouts(Player player) {
        LoadoutsAttachmentType data = player.getData(Contents.AttachmentTypes.LOADOUTS.get());
        return data.loadouts();
    }

    public static LoadoutEntry getLoadout(Player player, int loadoutNumber) {
        List<LoadoutEntry> loadouts = getLoadouts(player);
        return loadouts.get(loadoutNumber - 1);
    }

    public static int getCurrentLoadout(Player player) {
        LoadoutsAttachmentType data = player.getData(Contents.AttachmentTypes.LOADOUTS.get());
        return data.currentLoadout();
    }

    public static void setCurrentLoadout(Player player, int loadoutNumber) {
        LoadoutsAttachmentType data = player.getData(Contents.AttachmentTypes.LOADOUTS.get());
        player.setData(Contents.AttachmentTypes.LOADOUTS.get(), new LoadoutsAttachmentType(loadoutNumber, data.loadouts()));
    }

    public static void saveEquipmentToLoadout(Player player, int loadoutNumber) {
        LoadoutsAttachmentType data = player.getData(Contents.AttachmentTypes.LOADOUTS.get());
        LoadoutEntry newEntry = new LoadoutEntry(
                player.getInventory().getArmor(3),
                player.getInventory().getArmor(2),
                player.getInventory().getArmor(1),
                player.getInventory().getArmor(0)
        );
        ArrayList<LoadoutEntry> loadouts = new ArrayList<>(data.loadouts());
        loadouts.set(loadoutNumber - 1, newEntry);
        player.setData(Contents.AttachmentTypes.LOADOUTS.get(), new LoadoutsAttachmentType(data.currentLoadout(), loadouts.stream().toList()));
    }

    public static void equipLoadout(Player player, int loadoutNumber) {
        saveEquipmentToLoadout(player, getCurrentLoadout(player));
        setCurrentLoadout(player, loadoutNumber);
        LoadoutEntry loadout = getLoadout(player, loadoutNumber);
        player.setItemSlot(EquipmentSlot.HEAD, loadout.headStack().copy());
        player.setItemSlot(EquipmentSlot.CHEST, loadout.chestStack().copy());
        player.setItemSlot(EquipmentSlot.LEGS, loadout.legsStack().copy());
        player.setItemSlot(EquipmentSlot.FEET, loadout.feetStack().copy());
    }
}
