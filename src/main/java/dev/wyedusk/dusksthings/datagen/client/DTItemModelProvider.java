package dev.wyedusk.dusksthings.datagen.client;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.content.Contents;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class DTItemModelProvider extends ItemModelProvider {
    public DTItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, DusksThings.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(Contents.Items.SPECTRAL_LENS.get());
        this.basicItem(Contents.Items.SPECTRAL_APPLE.get());
    }
}