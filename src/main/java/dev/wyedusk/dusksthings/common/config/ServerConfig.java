package dev.wyedusk.dusksthings.common.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Config Specs
    public static final ModConfigSpec.BooleanValue GHOSTS_FEATURE_ENABLED;

    public static final ModConfigSpec.BooleanValue SPECTRAL_LENS_ENABLED;
    public static final ModConfigSpec.BooleanValue SPECTRAL_LENS_SHOWS_INVISIBLE_ENTITIES;
    public static final ModConfigSpec.BooleanValue SPECTRAL_APPLE_ENABLED;
    public static final ModConfigSpec.IntValue SPECTRAL_APPLE_WAIT_TIME;

    public static final ModConfigSpec.BooleanValue LOADOUTS_FEATURE_ENABLED;

    public static final ModConfigSpec.BooleanValue LOADOUTS_AFFECTS_CURIOS;
    public static final ModConfigSpec.IntValue MAX_LOADOUTS;

    public static final ModConfigSpec SPEC;

    static {
        // Config Specs
        BUILDER.push("ghosts");
        GHOSTS_FEATURE_ENABLED = BUILDER.comment("Should the Ghost Entities feature be enabled?")
                .define("ghostsFeatureEnabled", true);
        SPECTRAL_LENS_ENABLED = BUILDER.comment("Should the Spectral Lens be enabled?")
                .define("spectralLensEnabled", true);
        SPECTRAL_LENS_SHOWS_INVISIBLE_ENTITIES = BUILDER.comment("Should the Spectral Lens show invisible entities?")
                .define("spectralLensShowsInvisibleEntities", true);
        SPECTRAL_APPLE_ENABLED = BUILDER.comment("Should the Spectral Apple be enabled?")
                .define("spectralAppleEnabled", true);
        SPECTRAL_APPLE_WAIT_TIME = BUILDER.comment("How long in seconds should there be between using the Spectral Apple and becoming a ghost?")
                .defineInRange("spectralAppleWaitTime", 20, 0, 60);
        BUILDER.pop();

        BUILDER.push("loadouts");
        LOADOUTS_FEATURE_ENABLED = BUILDER.comment("Should the Loadouts feature be enabled?")
                .define("loadoutsFeatureEnabled", true);
        LOADOUTS_AFFECTS_CURIOS = BUILDER.comment("Should the Loadouts feature affect Curios accessories?\nIf you're having issues with Curios items disappearing when changing loadouts, disable this.\nDoes nothing without the Curios mod.")
                .define("loadoutsAffectsCurios", true);
        MAX_LOADOUTS = BUILDER.comment("How many loadouts should people be allowed to have?")
                .defineInRange("maxLoadouts", 3, 1, 3);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}