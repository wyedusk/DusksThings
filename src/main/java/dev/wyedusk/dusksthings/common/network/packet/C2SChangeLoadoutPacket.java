package dev.wyedusk.dusksthings.common.network.packet;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.config.ServerConfig;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.LoadoutsUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record C2SChangeLoadoutPacket(int loadoutNumber) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<C2SChangeLoadoutPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DusksThings.MODID,
            "change_loadout_c2s"));
    public static final StreamCodec<FriendlyByteBuf, C2SChangeLoadoutPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, C2SChangeLoadoutPacket::loadoutNumber,
            C2SChangeLoadoutPacket::new
    );

    @Override
    public @NotNull CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(C2SChangeLoadoutPacket packet, IPayloadContext context) {
        if (!context.flow().isServerbound()) return;
        if (!ServerConfig.SPEC.isLoaded()) return;
        if (!ServerConfig.LOADOUTS_FEATURE_ENABLED.getAsBoolean()) return;

        Player player = context.player();
        int currentLoadout = LoadoutsUtil.getCurrentLoadout(player);
        if (currentLoadout == packet.loadoutNumber) return;
        LoadoutsUtil.equipLoadout(player, packet.loadoutNumber);
        PacketDistributor.sendToPlayer((ServerPlayer) player, new S2CSyncLoadoutsPacket(LoadoutsUtil.getCurrentLoadout(player), LoadoutsUtil.getLoadouts(player)));
    }
}