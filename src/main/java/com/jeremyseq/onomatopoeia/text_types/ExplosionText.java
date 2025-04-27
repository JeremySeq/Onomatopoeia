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

public class ExplosionText extends Text {
    private final String text;
    private final int size;
    private final Color color;

    private static final List<String> explosionTexts = new ArrayList<>();

    static {
        explosionTexts.add("BOOM!");
        explosionTexts.add("BAM!");
        explosionTexts.add("KABOOM!");
        explosionTexts.add("BLAM!");
        explosionTexts.add("KABLOOEY!");
        explosionTexts.add("POW!");
        explosionTexts.add("KA-POW!");
        explosionTexts.add("BANG!");
        explosionTexts.add("WHAM!");
        explosionTexts.add("KA-BOOM!");
    }

    public ExplosionText(double x, double y, double z, int size, Color color) {
        super(x, y, z);
        this.text = getRandomExplosionText();
        this.size = size;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public int getSize() {
        return size;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public void renderSpecific(PoseStack poseStack) {
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        Font.DisplayMode displayMode = Font.DisplayMode.SEE_THROUGH;
        Font fontRenderer = Minecraft.getInstance().font;
        int textWidth = fontRenderer.width(text);
        int textHeight = fontRenderer.lineHeight;

        long timestamp = System.currentTimeMillis();
        long elapsed = timestamp - this.getTimestamp();
        float duration = this.getDuration(); // full duration

        float growDuration = 400f; // fixed grow time
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

        float shakeX = 0;
        float shakeY = 0;
        if (elapsed < this.getDuration()/2) {
            // calculate shake offsets
            shakeX = (float) (Math.random() * 2 - 1) * 1.5f;
            shakeY = (float) (Math.random() * 2 - 1) * 1.5f;
        }

        poseStack.scale(size, size, size);

        int fadedColor = new Color(getColor().getRed(), getColor().getGreen(), getColor().getBlue(), (int) (alpha*255)).getRGB();

        // used to fix weird bug where the text is rendered with full opacity when opacity is 0
        if (alpha > 25/255f) {
            fontRenderer.drawInBatch(text, -textWidth / 2f + shakeX, -textHeight / 2f + shakeY, fadedColor, false, poseStack.last().pose(), bufferSource, displayMode, 0, 15728880);
        }
    }

    private static String getRandomExplosionText() {
        if (!explosionTexts.isEmpty()) {
            return explosionTexts.get(new Random().nextInt(explosionTexts.size())); // Choose a random text
        }
        return "BOOM!";
    }

    @Override
    public boolean doRandomizationRendering() {
        return true;
    }
}
