package dev.wyedusk.dusksthings.common.network.packet;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.content.Contents;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.LoadoutEntry;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.LoadoutsAttachmentType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record S2CSyncLoadoutsPacket(int currentLoadout, List<LoadoutEntry> loadouts) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<S2CSyncLoadoutsPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DusksThings.MODID,
            "sync_loadouts_s2c"));
    public static final StreamCodec<RegistryFriendlyByteBuf, S2CSyncLoadoutsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, S2CSyncLoadoutsPacket::currentLoadout,
            ByteBufCodecs.collection(ArrayList::new, LoadoutEntry.STREAM_CODEC), S2CSyncLoadoutsPacket::loadouts,
            S2CSyncLoadoutsPacket::new
    );

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(S2CSyncLoadoutsPacket packet, IPayloadContext context) {
        if (!context.flow().isClientbound()) return;
        if (Minecraft.getInstance().level != null) {
            Player player = context.player();
            player.setData(Contents.AttachmentTypes.LOADOUTS, new LoadoutsAttachmentType(packet.currentLoadout, packet.loadouts));
        }
    }
}