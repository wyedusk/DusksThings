package dev.wyedusk.duskthings.network.packet;

import dev.wyedusk.duskthings.DuskThings;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ClientboundSyncExtraHealthPacket(int entityId, int extraHealth) implements CustomPacketPayload {

    public static final Type<ClientboundSyncExtraHealthPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(DuskThings.MODID, "extra_health"));

    public static final StreamCodec<FriendlyByteBuf, ClientboundSyncExtraHealthPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ClientboundSyncExtraHealthPacket::entityId,
            ByteBufCodecs.VAR_INT, ClientboundSyncExtraHealthPacket::extraHealth,
            ClientboundSyncExtraHealthPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}