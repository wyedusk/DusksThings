package dev.wyedusk.dusksthings.common.content.mechanic.loadouts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record LoadoutEntry(ItemStack headStack, ItemStack chestStack, ItemStack legsStack, ItemStack feetStack) {
    public static final Codec<LoadoutEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ItemStack.OPTIONAL_CODEC.fieldOf("headStack").forGetter(LoadoutEntry::headStack),
            ItemStack.OPTIONAL_CODEC.fieldOf("chestStack").forGetter(LoadoutEntry::chestStack),
            ItemStack.OPTIONAL_CODEC.fieldOf("legsStack").forGetter(LoadoutEntry::legsStack),
            ItemStack.OPTIONAL_CODEC.fieldOf("feetStack").forGetter(LoadoutEntry::feetStack)
    ).apply(inst, LoadoutEntry::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, LoadoutEntry> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC, LoadoutEntry::headStack,
            ItemStack.OPTIONAL_STREAM_CODEC, LoadoutEntry::chestStack,
            ItemStack.OPTIONAL_STREAM_CODEC, LoadoutEntry::legsStack,
            ItemStack.OPTIONAL_STREAM_CODEC, LoadoutEntry::feetStack,
            LoadoutEntry::new
    );
}
