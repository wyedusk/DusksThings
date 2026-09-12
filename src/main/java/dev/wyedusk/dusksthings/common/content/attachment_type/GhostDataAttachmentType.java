package dev.wyedusk.dusksthings.common.content.attachment_type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GhostDataAttachmentType(boolean isPermanentGhost, boolean isTemporaryGhost) {
    public static final Codec<GhostDataAttachmentType> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.BOOL.fieldOf("isPermanentGhost").forGetter(GhostDataAttachmentType::isPermanentGhost),
            Codec.BOOL.fieldOf("isTemporaryGhost").forGetter(GhostDataAttachmentType::isTemporaryGhost)
    ).apply(inst, GhostDataAttachmentType::new));
    public static final StreamCodec<FriendlyByteBuf, GhostDataAttachmentType> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, GhostDataAttachmentType::isPermanentGhost,
            ByteBufCodecs.BOOL, GhostDataAttachmentType::isTemporaryGhost,
            GhostDataAttachmentType::new
    );
}
