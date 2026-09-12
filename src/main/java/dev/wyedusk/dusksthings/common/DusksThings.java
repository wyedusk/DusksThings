package dev.wyedusk.dusksthings.common;

import com.mojang.logging.LogUtils;
import dev.wyedusk.dusksthings.common.config.ServerConfig;
import dev.wyedusk.dusksthings.common.content.Contents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(DusksThings.MODID)
public class DusksThings {
    public static final String MODID = "dusksthings";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DusksThings(IEventBus modEventBus, ModContainer modContainer) {
        Contents.registerContents(modEventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
    }

}