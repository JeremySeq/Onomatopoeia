package com.jeremyseq.onomatopoeia.mixin;

import com.jeremyseq.onomatopoeia.TextRenderer;
import com.jeremyseq.onomatopoeia.text_types.VillagerText;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public class PlaySoundMixin {
    @Inject(at = @At("HEAD"), method = "playSound")
    public void playSound(double pX, double pY, double pZ, SoundEvent pSoundEvent, SoundSource pSource, float pVolume, float pPitch, boolean pDistanceDelay, long pSeed, CallbackInfo ci) {
        if (pSoundEvent.getLocation().toString().equals("minecraft:entity.villager.ambient")) {
            TextRenderer.addText(new VillagerText(pX, pY+1, pZ, 1));
        }
    }
}
