package com.jeremyseq.onomatopoeia.mixin;

import com.jeremyseq.onomatopoeia.Onomatopoeia;
import com.jeremyseq.onomatopoeia.TextRenderer;
import com.jeremyseq.onomatopoeia.overlay.NarrativeTextOverlay;
import com.jeremyseq.onomatopoeia.text_types.LightningText;
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
        Onomatopoeia.LOGGER.debug("Sound played: {} at ({}, {}, {})", pSoundEvent.getLocation(), pX, pY, pZ);
        if (pSoundEvent.getLocation().toString().equals("minecraft:entity.villager.ambient")) {
            TextRenderer.addText(new VillagerText(pX, pY+1, pZ, 1));
        } else if (pSoundEvent.getLocation().toString().equals("minecraft:entity.lightning_bolt.thunder")) {
            TextRenderer.addText(new LightningText(pX, pY+10, pZ, 5));
        }

        // narrative text
        if (pSoundEvent.getLocation().toString().equals("minecraft:item.totem.use")) {
            NarrativeTextOverlay.sendNarrativeEvent("totem_used");
        } else if (pSoundEvent.getLocation().toString().equals("minecraft:entity.wither.spawn")) {
            NarrativeTextOverlay.sendNarrativeEvent("wither_spawned");
        } else if (pSoundEvent.getLocation().toString().equals("minecraft:entity.warden.emerge")) {
            NarrativeTextOverlay.sendNarrativeEvent("warden_spawned");
        } else if (pSoundEvent.getLocation().toString().equals("minecraft:entity.ender_dragon.death")) {
            NarrativeTextOverlay.sendNarrativeEvent("ender_dragon_death");
        }
    }
}
