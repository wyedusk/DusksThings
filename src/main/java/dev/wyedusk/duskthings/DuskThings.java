package dev.wyedusk.duskthings;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import dev.wyedusk.duskthings.compat.CompatHandler;
import dev.wyedusk.duskthings.item.SpectralLensItem;import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod(DuskThings.MODID)
public class DuskThings {
    public static final String MODID = "duskthings";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    /*public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.duskthings"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
            }).build());
     */

    // Items
    public static final DeferredItem<Item> SPECTRAL_LENS = ITEMS.registerItem("spectral_lens", SpectralLensItem::new,
            new Item.Properties()
                    .stacksTo(1));
    public static final DeferredItem<Item> SPECTRAL_APPLE = ITEMS.registerItem("spectral_apple", Item::new,
            new Item.Properties()
                    .food(new FoodProperties.Builder()
                            .nutrition(4)
                            .saturationModifier(0.3F)
                            .build()));

    // Creative Tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register(MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.duskthings"))
            .icon(() -> SPECTRAL_LENS.get().getDefaultInstance())
            .build());

    // Attachment Types
    public static final Supplier<AttachmentType<Boolean>> IS_GHOST = ATTACHMENT_TYPES.register(
            "is_ghost", () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL.fieldOf("is_ghost").codec()).build());

    public DuskThings(
            IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::buildCreativeTab);

        CompatHandler.initialiseCompats(modEventBus);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, DTConfig.SPEC);
    }

    public void buildCreativeTab(
            BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CREATIVE_TAB.getKey()) {
            for (DeferredHolder<Item, ? extends Item> itemHolder : ITEMS.getEntries()) {
                event.accept(itemHolder.get());
            }
        }
    }
}
