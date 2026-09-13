package dev.wyedusk.dusksthings.client.event;

import dev.wyedusk.dusksthings.client.rendering.gui.widget.LoadoutButton;
import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.config.ServerConfig;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = DusksThings.MODID, value = Dist.CLIENT)
public class ClientScreenEventListener {
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen inventory) {
            if (ServerConfig.LOADOUTS_FEATURE_ENABLED.getAsBoolean()) {
                int x = inventory.getGuiLeft() + inventory.getXSize();
                int y = inventory.getGuiTop();
                event.addListener(new LoadoutButton(x + 5, y + 5, 1));
                event.addListener(new LoadoutButton(x + 5, y + 30, 2));
                event.addListener(new LoadoutButton(x + 5, y + 55, 3));
            }
        }
    }
}
