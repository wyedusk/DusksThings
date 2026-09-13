package dev.wyedusk.dusksthings.common.content.mechanic.loadouts;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.List;

public record LoadoutsAttachmentType(int currentLoadout, List<LoadoutEntry> loadouts) {
    public static final Codec<LoadoutsAttachmentType> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.INT.fieldOf("currentLoadout").forGetter(LoadoutsAttachmentType::currentLoadout),
            LoadoutEntry.CODEC.listOf().fieldOf("loadouts").forGetter(LoadoutsAttachmentType::loadouts)
    ).apply(inst, LoadoutsAttachmentType::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, LoadoutsAttachmentType> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, LoadoutsAttachmentType::currentLoadout,
            ByteBufCodecs.collection(ArrayList::new, LoadoutEntry.STREAM_CODEC), LoadoutsAttachmentType::loadouts,
            LoadoutsAttachmentType::new
    );
}