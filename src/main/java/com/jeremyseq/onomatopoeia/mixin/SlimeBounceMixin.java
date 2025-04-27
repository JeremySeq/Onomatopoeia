package com.jeremyseq.onomatopoeia.mixin;

import com.jeremyseq.onomatopoeia.TextRenderer;
import com.jeremyseq.onomatopoeia.text_types.JumpOnSlimeText;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SlimeBlock.class)
public class SlimeBounceMixin {

    @Inject(at = @At("HEAD"), method = "bounceUp")
    public void bounceUp(Entity pEntity, CallbackInfo ci) {
        Vec3 vec3 = pEntity.getDeltaMovement();
        if (pEntity.level() instanceof ServerLevel) {
            return;
        }
        if (vec3.y < -.5f) {
            TextRenderer.addText(new JumpOnSlimeText(pEntity.getX(), pEntity.getY(), pEntity.getZ(), 1));
        }
    }
}
