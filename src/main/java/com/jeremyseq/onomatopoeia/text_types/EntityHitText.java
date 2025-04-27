package com.jeremyseq.onomatopoeia.text_types;

import com.jeremyseq.onomatopoeia.Text;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityHitText extends Text {

    private final String text;
    private final int size;

    private static final List<String> hitText = new ArrayList<>();
    private final float randomStretch;  // Added random stretch factor

    static {
        hitText.add("THWACK!");
        hitText.add("OW!");
        hitText.add("SMACK!");
        hitText.add("WHACK!");
        hitText.add("POW!");
        hitText.add("BAM!");
        hitText.add("WHAM!");
        hitText.add("THWAP!");
        hitText.add("OOF!");
    }

    public EntityHitText(double x, double y, double z, int size) {
        super(x, y, z);
        this.size = size;
        this.text = getRandomHitText();
        this.randomStretch = 1.0f + (new Random().nextFloat() - .5f) * 0.5f; // Random stretch between .75 and 1.25
    }

    @Override
    public void renderSpecific(PoseStack poseStack) {
        Color color = Color.WHITE;
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        Font.DisplayMode displayMode = Font.DisplayMode.SEE_THROUGH;
        Font fontRenderer = Minecraft.getInstance().font;
        int textWidth = fontRenderer.width(text);
        int textHeight = fontRenderer.lineHeight;

        long timestamp = System.currentTimeMillis();
        long elapsed = timestamp - this.getTimestamp();
        float duration = this.getDuration(); // full duration

        float growDuration = 200f; // fixed grow time
        float size;
        float alpha = 1.0f; // full opacity

        if (elapsed > duration) {
            // past total duration, don't render
            return;
        }

        if (elapsed <= growDuration) {
            // growing phase
            float growProgress = elapsed / growDuration;
            size = this.size * growProgress;
        } else {
            // shrinking/fading phase
            float fadeProgress = (elapsed - growDuration) / (duration - growDuration);
            float shrinkFactor = 1.0f - 0.3f * fadeProgress; // shrink down to 70% at end
            size = this.size * shrinkFactor;

            alpha = 1.0f - fadeProgress; // fade alpha to 0 at end
        }

        // Apply random stretch to the text (horizontal scaling)
        poseStack.scale(randomStretch, 1.0f, 1.0f); // Stretch horizontally by random factor

        // Apply the size scaling (both horizontal and vertical)
        poseStack.scale(size, size, size);

        int fadedColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255)).getRGB();

        // used to fix weird bug where the text is rendered with full opacity when opacity is 0
        if (alpha > 25 / 255f) {
            fontRenderer.drawInBatch(text, -textWidth / 2f, -textHeight / 2f, fadedColor, false, poseStack.last().pose(), bufferSource, displayMode, 0, 15728880);
        }
    }

    private static String getRandomHitText() {
        if (!hitText.isEmpty()) {
            return hitText.get(new Random().nextInt(hitText.size())); // Choose a random text
        }
        return "THWACK!";
    }

    @Override
    public boolean doRandomizationRendering() {
        return true;
    }
}
