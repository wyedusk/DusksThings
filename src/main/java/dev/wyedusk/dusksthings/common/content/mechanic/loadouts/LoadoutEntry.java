package dev.wyedusk.dusksthings.common.content.mechanic.loadouts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record LoadoutEntry(ItemStack headStack, ItemStack chestStack, ItemStack legsStack, ItemStack feetStack) {
    public static final Codec<LoadoutEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            ItemStack.CODEC.fieldOf("headStack").forGetter(LoadoutEntry::headStack),
            ItemStack.CODEC.fieldOf("chestStack").forGetter(LoadoutEntry::chestStack),
            ItemStack.CODEC.fieldOf("legsStack").forGetter(LoadoutEntry::legsStack),
            ItemStack.CODEC.fieldOf("feetStack").forGetter(LoadoutEntry::feetStack)
    ).apply(inst, LoadoutEntry::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, LoadoutEntry> STREAM_CODEC = StreamCodec.composite(
            ItemStack.STREAM_CODEC, LoadoutEntry::headStack,
            ItemStack.STREAM_CODEC, LoadoutEntry::chestStack,
            ItemStack.STREAM_CODEC, LoadoutEntry::legsStack,
            ItemStack.STREAM_CODEC, LoadoutEntry::feetStack,
            LoadoutEntry::new
    );
}
