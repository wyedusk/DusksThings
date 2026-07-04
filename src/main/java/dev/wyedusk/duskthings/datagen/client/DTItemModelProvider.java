package dev.wyedusk.duskthings.datagen.client;

import dev.wyedusk.duskthings.DuskThings;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class DTItemModelProvider extends ItemModelProvider {
    public DTItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DuskThings.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(DuskThings.SPECTRAL_LENS.get());
        this.basicItem(DuskThings.SPECTRAL_APPLE.get());
    }
}
