package com.jeremyseq.onomatopoeia.text_types;

import com.jeremyseq.onomatopoeia.Text;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LightningText extends Text {
    private final String text;
    private final int size;
    private final float randomStretch;  // Added random stretch factor

    private static final List<String> lightningTexts = new ArrayList<>();

    static {
        lightningTexts.add("§lZAP!");
        lightningTexts.add("§lBOOM!");
        lightningTexts.add("§lCRASH!");
        lightningTexts.add("§lCRACK!");
        lightningTexts.add("§lKRAK!");
    }

    public LightningText(double x, double y, double z, int size) {
        super(x, y, z);
        this.text = getRandomLightningText();
        this.size = size;
        this.randomStretch = 1.0f + (new Random().nextFloat() - .5f); // Random stretch between .5 and 1.5
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

        if (elapsed > duration) {
            // past total duration, don't render
            return;
        }

        float explosionStrength = 2f; // initial explosion size
        float scale = Math.min(1.0F + explosionStrength * (1.0F - progress), 2.0F); // explosive scale at start
        poseStack.scale(scale, scale, scale);

        float shakeIntensity = 2f;
        float xShake = (float)Math.sin(progress * 70f * Math.PI) * shakeIntensity * life; // quick shake in x-direction
        float yShake = (float)Math.sin(progress * 70f * Math.PI) * shakeIntensity * life; // quick shake in y-direction
        poseStack.translate(xShake, yShake, 0.0F);

        // Flickering alpha effect and fade out
        float flicker = (float)Math.abs(Math.sin(progress * 10f * Math.PI)) * 0.3F + 0.2F; // flicker effect, alpha goes up and down
        int color = (int)(255 * flicker * life) << 24 | 0xFFEF00; // bright yellow text with flickering alpha

        // Apply random stretch to the text (horizontal scaling)
        poseStack.scale(randomStretch, 1f, 1f); // Stretch horizontally by random factor

        // Apply the size scaling (both horizontal and vertical)
        poseStack.scale(size, size, size);

        // used to fix weird bug where the text is rendered with full opacity when opacity is 0
        if (life > 25 / 255f) {

            fontRenderer.drawInBatch(text, -textWidth / 2f, -textHeight / 2f, color, false, poseStack.last().pose(), bufferSource, displayMode, 0, 15728880);
        }
    }

    private static String getRandomLightningText() {
        if (!lightningTexts.isEmpty()) {
            return lightningTexts.get(new Random().nextInt(lightningTexts.size())); // Choose a random text
        }
        return "§lZAP!";
    }

    @Override
    public boolean doRandomizationRendering() {
        return true;
    }

    @Override
    public Vec2 getRandomDriftSpeedYRange() {

        return new Vec2(0f, .003f);
    }

    @Override
    public float getRandomRotationRange() {
        return 30f;
    }
}
