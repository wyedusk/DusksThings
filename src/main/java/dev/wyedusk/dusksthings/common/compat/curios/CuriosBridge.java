package dev.wyedusk.dusksthings.common.compat.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ItemLike;

public class CuriosBridge {
    public static boolean hasCuriosItem(LivingEntity entity, ItemLike item) {
        return false;
    }
}