package dev.wyedusk.dusksthings.datagen.server;

import dev.wyedusk.dusksthings.common.content.Contents;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DTRecipeProvider extends RecipeProvider {
    public DTRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output, HolderLookup.@NotNull Provider lookup) {
        HolderLookup.RegistryLookup<Biome> biomeLookup = lookup.lookupOrThrow(Registries.BIOME);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, Contents.Items.SPECTRAL_LENS)
                .pattern("DGD")
                .pattern("GSG")
                .pattern("QGQ")
                .define('D', Tags.Items.GEMS_DIAMOND)
                .define('G', Tags.Items.GLASS_PANES)
                .define('Q', Tags.Items.GEMS_QUARTZ)
                .define('S', Items.SOUL_LANTERN)
                .unlockedBy("entered_soul_sand_valley", PlayerTrigger.TriggerInstance.located(
                        LocationPredicate.Builder.location().setBiomes(HolderSet.direct(biomeLookup.getOrThrow(Biomes.SOUL_SAND_VALLEY).getDelegate()))
                ))
                .save(output);
    }
}