package dev.wyedusk.dusksthings.common.content.mechanic.loadouts.compat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record LoadoutCuriosEntry(String identifier, int slot, ItemStack functionalStack, ItemStack cosmeticStack) {
    public static final Codec<LoadoutCuriosEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.STRING.fieldOf("identifier").forGetter(LoadoutCuriosEntry::identifier),
            Codec.INT.fieldOf("slot").forGetter(LoadoutCuriosEntry::slot),
            ItemStack.OPTIONAL_CODEC.fieldOf("functionalStack").forGetter(LoadoutCuriosEntry::functionalStack),
            ItemStack.OPTIONAL_CODEC.fieldOf("cosmeticStack").forGetter(LoadoutCuriosEntry::cosmeticStack)
    ).apply(inst, LoadoutCuriosEntry::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, LoadoutCuriosEntry> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, LoadoutCuriosEntry::identifier,
            ByteBufCodecs.VAR_INT, LoadoutCuriosEntry::slot,
            ItemStack.OPTIONAL_STREAM_CODEC, LoadoutCuriosEntry::functionalStack,
            ItemStack.OPTIONAL_STREAM_CODEC, LoadoutCuriosEntry::cosmeticStack,
            LoadoutCuriosEntry::new
    );

    public static final LoadoutCuriosEntry EMPTY = new LoadoutCuriosEntry("", 0, ItemStack.EMPTY, ItemStack.EMPTY);
}
