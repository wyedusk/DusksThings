package dev.wyedusk.dusksthings.client.rendering.gui.widget;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.LoadoutsUtil;
import dev.wyedusk.dusksthings.common.network.packet.C2SChangeLoadoutPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class LoadoutButton extends AbstractWidget {
    private final ResourceLocation texture;
    private final ResourceLocation highlightTexture;
    private final ResourceLocation disabledTexture;
    private final int loadoutNumber;

    public LoadoutButton(int x, int y, int loadoutNumber) {
        super(x, y, 20, 20, Component.empty());
        String loadoutNumberText = loadoutNumber == 1 ? "one" : loadoutNumber == 2 ? "two" : "three";
        this.texture = ResourceLocation.fromNamespaceAndPath(DusksThings.MODID, "textures/gui/sprites/widget/loadout_%s.png".formatted(loadoutNumberText));
        this.highlightTexture = ResourceLocation.fromNamespaceAndPath(DusksThings.MODID, "textures/gui/sprites/widget/loadout_%s_highlighted.png".formatted(loadoutNumberText));
        this.disabledTexture = ResourceLocation.fromNamespaceAndPath(DusksThings.MODID, "textures/gui/sprites/widget/loadout_%s_disabled.png".formatted(loadoutNumberText));
        this.loadoutNumber = loadoutNumber;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation currentTexture;
        Player player = Minecraft.getInstance().player;

        if (!this.active) {
            currentTexture = disabledTexture;
        } else if (this.isHovered() || (player != null && LoadoutsUtil.getCurrentLoadout(player) == this.loadoutNumber)) {
            currentTexture = highlightTexture;
        } else {
            currentTexture = texture;
        }

        guiGraphics.blit(currentTexture, this.getX(), this.getY(), 0, 0, 20, 20, 20, 20);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        if (this.active) {
            PacketDistributor.sendToServer(new C2SChangeLoadoutPacket(this.loadoutNumber));
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {}
}