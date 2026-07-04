package dev.wyedusk.duskthings.server.event;

import dev.wyedusk.duskthings.DuskThings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = DuskThings.MODID)
public class EntityEvents {
    @SubscribeEvent
    public static void onEntityTick(
            EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (entity instanceof ItemEntity itemEntity) {
            if (itemEntity.isRemoved() && itemEntity.getItem().is(Items.APPLE)) {
                BlockPos pos = entity.blockPosition();
                Vec3 entityPos = entity.position();
                Level level = entity.level();

                if (level.getBlockState(pos).is(Blocks.SOUL_FIRE)) {
                    ItemEntity spectralAppleEntity = new ItemEntity(
                            level,
                            entityPos.x, entityPos.y, entityPos.z,
                            DuskThings.SPECTRAL_APPLE.toStack(itemEntity.getItem().getCount())
                    );
                    level.addFreshEntity(spectralAppleEntity);
                    spectralAppleEntity.setDeltaMovement(Vec3.ZERO);
                }
            }
        }
    }
}