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

public class JumpOnSlimeText extends Text {
    private final String text;
    private final int size;
    private final float randomStretch;  // Added random stretch factor

    private static final List<String> slimeTexts = new ArrayList<>();

    static {
        slimeTexts.add("§oboing");
        slimeTexts.add("§obloop");
        slimeTexts.add("§osplat");
    }

    public JumpOnSlimeText(double x, double y, double z, int size) {
        super(x, y, z);
        this.text = getRandomSlimeText();
        this.size = size;
        this.randomStretch = 1.0f + (new Random().nextFloat() - .5f); // Random stretch between .5 and 1.5
        this.setDuration(1000);
    }

    @Override
    public void renderSpecific(PoseStack poseStack) {
        Color color = new Color(80, 200, 120);
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

        // vertical bounce
        float bounceHeight = 0.2F; // blocks
        float yBounce = (float)Math.sin(progress * 50f * Math.PI) * bounceHeight * (life);

        // squash based on lifetime
        float stretchAmount = .5f; // how stretchy
        float yScale = 1.0F + stretchAmount * life;
        float xzScale = 1.0F - stretchAmount * life * 0.5F;

        // random rotation (during lifetime)
        float rotation = (float)Math.sin(progress * 100f * Math.PI) * 5f * life; // +- 5 degrees
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation));

        // Apply random stretch to the text (horizontal scaling)
        poseStack.scale(randomStretch * xzScale, yScale, xzScale); // Stretch horizontally by random factor

        // Apply the size scaling (both horizontal and vertical)
        poseStack.scale(size, size, size);

        int fadedColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (life * 255)).getRGB();

        // used to fix weird bug where the text is rendered with full opacity when opacity is 0
        if (life > 25 / 255f) {
            fontRenderer.drawInBatch(text, -textWidth / 2f, (-textHeight / 2f) + yBounce, fadedColor, false, poseStack.last().pose(), bufferSource, displayMode, 0, 15728880);
        }
    }

    private static String getRandomSlimeText() {
        if (!slimeTexts.isEmpty()) {
            return slimeTexts.get(new Random().nextInt(slimeTexts.size())); // Choose a random text
        }
        return "§oboing";
    }

    @Override
    public boolean doRandomizationRendering() {
        return true;
    }

    @Override
    public Vec2 getRandomDriftSpeedYRange() {

        return new Vec2(0f, 0f);
    }

    @Override
    public float getRandomRotationRange() {
        return 30f;
    }
}
