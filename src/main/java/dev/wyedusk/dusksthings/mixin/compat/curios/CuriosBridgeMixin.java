package dev.wyedusk.dusksthings.mixin.compat.curios;

import dev.wyedusk.dusksthings.common.compat.curios.CuriosBridge;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

@Mixin(CuriosBridge.class)
public class CuriosBridgeMixin {
    @Inject(method = "hasCuriosItem", at = @At("HEAD"), cancellable = true)
    private static void dusksthings$hasCuriosItem(LivingEntity entity, ItemLike item, CallbackInfoReturnable<Boolean> cir) {
        Optional<ICuriosItemHandler> curiosInventory = CuriosApi.getCuriosInventory(entity);
        curiosInventory.ifPresent(
                iCuriosItemHandler -> cir.setReturnValue(!iCuriosItemHandler.findCurios(item.asItem()).isEmpty()));
    }
}