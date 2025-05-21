package com.jeremyseq.onomatopoeia.mixin;

import com.jeremyseq.onomatopoeia.OnomatopoeiaConfig;
import com.jeremyseq.onomatopoeia.TextRenderer;
import com.jeremyseq.onomatopoeia.text_types.EntityHitText;
import net.minecraft.client.Minecraft;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(LivingEntity.class)
public abstract class EntityHurtMixin {

    @Inject(at = @At("HEAD"), method = "hurt")
    public void hurt(DamageSource pSource, float pAmount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (!OnomatopoeiaConfig.ENABLE_HIT_TEXT.get()) return;
        if (Minecraft.getInstance().player != null && entity.getId() != Minecraft.getInstance().player.getId()) {
            if (!Objects.equals(pSource.getMsgId(), "player")) return; // only render for player damage

            int size = (int) Math.min(4, pAmount / 4f);
            if (pAmount >= 2 && size == 0) {
                size = 1;
            }
            if (size > 0) {
                TextRenderer.addText(new EntityHitText(entity.getX(), entity.getEyeY(), entity.getZ(), size));
            }
        }
    }
}