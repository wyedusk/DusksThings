package dev.wyedusk.dusksthings.common.compat;

import dev.wyedusk.dusksthings.common.DusksThings;
import net.neoforged.bus.api.IEventBus;

public class CompatHandler {
    public static void initialiseCompats(IEventBus eventBus) {
    }

    private void loadClass(IEventBus eventBus, String modId, String className) {
        try {
            Class<?> compatClass = Class.forName(className);
            compatClass.getDeclaredConstructor().newInstance(eventBus);
        } catch (Exception e) {
            DusksThings.LOGGER.error("Failed to load compatibility for '{}' due to {}!", modId, e.getClass());
        }
    }
}