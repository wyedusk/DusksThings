package dev.wyedusk.dusksthings.client.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Config Specs
    public static final ModConfigSpec.IntValue GHOST_TRANSPARENCY;

    public static final ModConfigSpec SPEC;

    static {
        // Config Specs
        BUILDER.push("ghosts");
        GHOST_TRANSPARENCY = BUILDER.comment("The transparency of Ghosts when they can be seen.\n0 = Invisible, 255 = Visible")
                .defineInRange("ghost_transparency", 120, 0, 255);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}