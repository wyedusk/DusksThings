package dev.wyedusk.dusksthings.common.content;

import dev.wyedusk.dusksthings.common.DusksThings;
import dev.wyedusk.dusksthings.common.content.mechanic.ghosts.GhostDataAttachmentType;
import dev.wyedusk.dusksthings.common.content.item.SpectralAppleItem;
import dev.wyedusk.dusksthings.common.content.item.SpectralLensItem;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.LoadoutEntry;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.LoadoutsAttachmentType;
import dev.wyedusk.dusksthings.common.content.mechanic.loadouts.compat.LoadoutCuriosEntry;
import dev.wyedusk.dusksthings.common.content.mob_effect.SpectralTransformationMobEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class Contents {
    // Deferred Registers
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, DusksThings.MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DusksThings.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DusksThings.MODID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, DusksThings.MODID);

    // Content Classes
    public static class AttachmentTypes {
        public static final Supplier<AttachmentType<GhostDataAttachmentType>> GHOST_DATA = ATTACHMENT_TYPES.register("ghost_data", () ->
                AttachmentType.builder(() -> new GhostDataAttachmentType(false, false))
                        .serialize(GhostDataAttachmentType.CODEC)
                        .copyOnDeath()
                        .copyHandler((original, holder, provider) -> new GhostDataAttachmentType(original.isPermanentGhost(), false))
                        .build());
        public static final Supplier<AttachmentType<LoadoutsAttachmentType>> LOADOUTS = ATTACHMENT_TYPES.register("loadouts", () ->
                AttachmentType.builder(() -> new LoadoutsAttachmentType(1, List.of(
                                new LoadoutEntry(ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY,List.of(LoadoutCuriosEntry.EMPTY)),
                                new LoadoutEntry(ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY,List.of(LoadoutCuriosEntry.EMPTY)),
                                new LoadoutEntry(ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY,ItemStack.EMPTY,List.of(LoadoutCuriosEntry.EMPTY))
                        )))
                        .serialize(LoadoutsAttachmentType.CODEC)
                        .copyOnDeath()
                        .build());

        protected static void register(IEventBus modEventBus) { ATTACHMENT_TYPES.register(modEventBus); }
    }

    public static class CreativeModeTabs {
        public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register(DusksThings.MODID, () ->
                CreativeModeTab.builder()
                        .title(Component.translatable("itemGroup." + DusksThings.MODID))
                        .icon(() -> Items.SPECTRAL_LENS.get().getDefaultInstance())
                        .displayItems((parameters, output) -> {
                            output.accept(Items.SPECTRAL_APPLE.get());
                            output.accept(Items.SPECTRAL_LENS.get());
                        }).build());

        protected static void register(IEventBus modEventBus) { CREATIVE_MODE_TABS.register(modEventBus); }
    }

    public static class Items {
        public static final DeferredItem<Item> SPECTRAL_LENS = ITEMS.registerItem("spectral_lens", SpectralLensItem::new,
                new Item.Properties()
                        .stacksTo(1));
        public static final DeferredItem<Item> SPECTRAL_APPLE = ITEMS.registerItem("spectral_apple", SpectralAppleItem::new,
                new Item.Properties()
                        .food(new FoodProperties.Builder()
                                .nutrition(0)
                                .saturationModifier(0F)
                                .alwaysEdible()
                                .build())
                        .fireResistant());

        protected static void register(IEventBus modEventBus) { ITEMS.register(modEventBus); }
    }

    public static class MobEffects {
        public static final DeferredHolder<MobEffect, SpectralTransformationMobEffect> SPECTRAL_TRANSFORMATION_MOB_EFFECT = MOB_EFFECTS.register("spectral_transformation",SpectralTransformationMobEffect::new);

        protected static void register(IEventBus modEventBus) { MOB_EFFECTS.register(modEventBus); }
    }

    // Register Function
    public static void registerContents(IEventBus modEventBus) {
        AttachmentTypes.register(modEventBus);
        CreativeModeTabs.register(modEventBus);
        Items.register(modEventBus);
        MobEffects.register(modEventBus);
    }
}
