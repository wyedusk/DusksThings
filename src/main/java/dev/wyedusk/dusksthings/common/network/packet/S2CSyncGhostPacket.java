package dev.wyedusk.dusksthings.common.network.packet;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.content.Contents;
import dev.wyedusk.dusksthings.common.content.mechanic.ghosts.GhostDataAttachmentType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record S2CSyncGhostPacket(int entityId, GhostDataAttachmentType ghostData) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CSyncGhostPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DusksThings.MODID,
            "sync_ghost_s2c"));
    public static final StreamCodec<FriendlyByteBuf, S2CSyncGhostPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, S2CSyncGhostPacket::entityId,
            GhostDataAttachmentType.STREAM_CODEC, S2CSyncGhostPacket::ghostData,
            S2CSyncGhostPacket::new
    );

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CSyncGhostPacket packet, IPayloadContext context) {
        if (!context.flow().isClientbound()) return;
        if (Minecraft.getInstance().level != null) {
            Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId());
            if (entity != null) {
                entity.setData(Contents.AttachmentTypes.GHOST_DATA, packet.ghostData());
            }
        }
    }

}