package com.jeremyseq.onomatopoeia.text_types;

import com.jeremyseq.onomatopoeia.Text;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VillagerText extends Text {
    private final String text;
    private final int size;

    public VillagerText(double x, double y, double z, int size) {
        super(x, y, z);
        this.text = "§oHm";
        this.size = size;
        this.setDuration(2000);
    }

    @Override
    public void renderSpecific(PoseStack poseStack) {
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        net.minecraft.client.gui.Font.DisplayMode displayMode = net.minecraft.client.gui.Font.DisplayMode.SEE_THROUGH;
        Font fontRenderer = Minecraft.getInstance().font;
        int textWidth = fontRenderer.width(text);
        int textHeight = fontRenderer.lineHeight;

        long timestamp = System.currentTimeMillis();
        long elapsed = timestamp - this.getTimestamp();
        float duration = this.getDuration(); // full duration
        float progress = elapsed / duration;
        float life = 1.0F - progress; // 1.0 at start, 0.0 at end

        float size = this.size;
        float alpha = 1.0f; // full opacity

        if (elapsed > duration) {
            // past total duration, don't render
            return;
        }

        float randomStretch = 1.0f + (new Random().nextFloat() - .5f); // Random stretch between .5 and 1.5


        int maxMCount = 10; // Max number of 'm's that can be added
        int mCount = (int)(progress * maxMCount); // Increase number of 'm's based on progress

        // Create the text with "Hm" + mCount number of 'm's
        String villagerText = text + "m".repeat(mCount);

        // **Vibration Effect** (slightly stronger for villager speech)
        float vibrationStrength = 0.05F;
        float yVibration = (float)Math.sin(progress * 6.0F * Math.PI) * vibrationStrength;

        // **Subtle Scaling** effect (for impact)
        float scale = (float) (1.0f + 0.1f * Math.sin(progress * 3.0f * Math.PI)); // Slightly pulsing
        poseStack.scale(scale, scale, scale);

        // Apply the size scaling (both horizontal and vertical)
        poseStack.scale(size, size, size);

        // **Color**: Slightly darker color to reflect the villager’s neutral tone
        int color = (int)(255 * (1.0F - progress)) << 24 | 0x6F4E37; // Brown color

        // used to fix weird bug where the text is rendered with full opacity when opacity is 0
        if (life > 25 / 255f) {
            fontRenderer.drawInBatch(villagerText, -textWidth / 2f, (-textHeight / 2f) + yVibration, color, false, poseStack.last().pose(), bufferSource, displayMode, 0, 15728880);
        }
    }

    @Override
    public boolean doRandomizationRendering() {
        return true;
    }

    @Override
    public Vec2 getRandomDriftSpeedYRange() {

        return new Vec2(0f, .0005f);
    }

}
