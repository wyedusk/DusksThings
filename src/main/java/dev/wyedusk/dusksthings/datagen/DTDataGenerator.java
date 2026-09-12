package dev.wyedusk.dusksthings.datagen;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.datagen.client.DTItemModelProvider;
import dev.wyedusk.dusksthings.datagen.server.DTRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = DusksThings.MODID)
public class DTDataGenerator implements IModBusEvent {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        // Client-side Providers
        generator.addProvider(
                event.includeClient(),
                new DTItemModelProvider(output, existingFileHelper)
        );

        // Server-side Providers
        generator.addProvider(
                event.includeServer(),
                new DTRecipeProvider(output, provider)
        );
    }
}