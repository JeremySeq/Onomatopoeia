package com.jeremyseq.onomatopoeia.mixin;

import com.jeremyseq.onomatopoeia.text_types.ExplosionText;
import com.jeremyseq.onomatopoeia.TextRenderer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;


@Mixin(Level.class)
public abstract class ExplosionMixin {

    @Inject(at=@At("HEAD"), method="explode(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/damagesource/DamageSource;Lnet/minecraft/world/level/ExplosionDamageCalculator;DDDFZLnet/minecraft/world/level/Level$ExplosionInteraction;Z)Lnet/minecraft/world/level/Explosion;")
    public void explode(Entity pSource, DamageSource pDamageSource, ExplosionDamageCalculator pDamageCalculator, double pX, double pY, double pZ, float pRadius, boolean pFire, Level.ExplosionInteraction pExplosionInteraction, boolean pSpawnParticles, CallbackInfoReturnable<Explosion> cir) {
        Color color = new Color(255, 88, 0);
        if (pSource instanceof EndCrystal) {
            color = new Color(172, 34, 187);
        } else if (pSource instanceof Creeper) {
            color = new Color(76, 224, 53);
        }
        TextRenderer.addText(new ExplosionText(pX, pY, pZ, (int) pRadius, color));
    }
}
