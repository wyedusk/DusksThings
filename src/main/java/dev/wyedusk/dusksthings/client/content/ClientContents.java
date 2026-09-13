package dev.wyedusk.dusksthings.client.content;

import com.mojang.blaze3d.platform.InputConstants;
import dev.wyedusk.dusksthings.common.DusksThings;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

public class ClientContents {
    public static class KeyMappings {
        public static final KeyMapping LOADOUT_ONE_KEY = new KeyMapping(
                "key." + DusksThings.MODID + ".loadout_one",
                KeyConflictContext.UNIVERSAL,
                KeyModifier.ALT,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_1,
                "key.categories." + DusksThings.MODID
        );
        public static final KeyMapping LOADOUT_TWO_KEY = new KeyMapping(
                "key." + DusksThings.MODID + ".loadout_two",
                KeyConflictContext.UNIVERSAL,
                KeyModifier.ALT,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_2,
                "key.categories." + DusksThings.MODID
        );
        public static final KeyMapping LOADOUT_THREE_KEY = new KeyMapping(
                "key." + DusksThings.MODID + ".loadout_three",
                KeyConflictContext.UNIVERSAL,
                KeyModifier.ALT,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_3,
                "key.categories." + DusksThings.MODID
        );
    }
}
