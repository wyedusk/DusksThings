package dev.wyedusk.dusksthings.client.event;

import dev.wyedusk.dusksthings.client.content.ClientContents;
import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.LoadoutsUtil;
import dev.wyedusk.dusksthings.common.network.packet.C2SChangeLoadoutPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = DusksThings.MODID, value = Dist.CLIENT)
public class ClientGameBusSubscriber {
    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(ClientContents.KeyMappings.LOADOUT_ONE_KEY);
        event.register(ClientContents.KeyMappings.LOADOUT_TWO_KEY);
        event.register(ClientContents.KeyMappings.LOADOUT_THREE_KEY);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int currentLoadout = LoadoutsUtil.getCurrentLoadout(player);
        while (ClientContents.KeyMappings.LOADOUT_ONE_KEY.consumeClick()) {
            if (currentLoadout != 1)
                PacketDistributor.sendToServer(new C2SChangeLoadoutPacket(1));
        }
        while (ClientContents.KeyMappings.LOADOUT_TWO_KEY.consumeClick()) {
            if (currentLoadout != 2)
                PacketDistributor.sendToServer(new C2SChangeLoadoutPacket(2));
        }
        while (ClientContents.KeyMappings.LOADOUT_THREE_KEY.consumeClick()) {
            if (currentLoadout != 3)
                PacketDistributor.sendToServer(new C2SChangeLoadoutPacket(3));
        }
    }
}
