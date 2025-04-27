package com.jeremyseq.onomatopoeia;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;

import java.util.Random;

public abstract class Text {
    private static final Random random = new Random();

    private final double baseX;
    private final double baseY;
    private final double baseZ;

    private final float randomOffsetX;
    private final float randomOffsetY;
    private final float randomOffsetZ;
    private final float randomRotation;

    private final float randomDriftSpeedY; // upward drift speed

    private final long timestamp;
    private int duration = 2000; // in milliseconds

    public Text(double x, double y, double z) {
        this.baseX = x;
        this.baseY = y;
        this.baseZ = z;
        this.timestamp = System.currentTimeMillis();

        // random offsets
        this.randomOffsetX = (random.nextFloat() - 0.5f) * .5f; // -0.25 to +0.25 blocks
        this.randomOffsetY = (random.nextFloat() - 0.5f) * .5f;
        this.randomOffsetZ = (random.nextFloat() - 0.5f) * .5f;

        // random rotation
        this.randomRotation = (random.nextFloat() - 0.5f) * 30f; // -15 to +15 degrees

        // random drift upward speed
        this.randomDriftSpeedY = 0.001f + random.nextFloat() * 0.002f; // 0.001 to 0.003 blocks per ms
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void render(PoseStack poseStack) {
        EntityRenderDispatcher renderManager = Minecraft.getInstance().getEntityRenderDispatcher();
        poseStack.pushPose();
        long elapsed = System.currentTimeMillis() - timestamp;


        if (this.doRandomizationRendering()) {
            // calculate extra upward drift based on elapsed time
            double driftY = elapsed * this.randomDriftSpeedY;

            // translate to base position + random offset + upward drift
            poseStack.translate(
                    this.baseX - renderManager.camera.getPosition().x + this.randomOffsetX,
                    this.baseY - renderManager.camera.getPosition().y + this.randomOffsetY + driftY,
                    this.baseZ - renderManager.camera.getPosition().z + this.randomOffsetZ
            );

            poseStack.mulPose(renderManager.cameraOrientation());

            poseStack.mulPose(Axis.ZP.rotationDegrees(this.randomRotation));
        } else {
            // translate to base position
            poseStack.translate(
                    this.baseX - renderManager.camera.getPosition().x,
                    this.baseY - renderManager.camera.getPosition().y,
                    this.baseZ - renderManager.camera.getPosition().z
            );

            poseStack.mulPose(renderManager.cameraOrientation());
        }

        poseStack.scale(-0.025F, -0.025F, 0.025F);

        renderSpecific(poseStack);

        poseStack.popPose();
    }

    public abstract void renderSpecific(PoseStack poseStack);

    /**
     * Override this method to enable randomization rendering.
     * Adds random upward drift, random rotation, and random offsets.
     */
    public abstract boolean doRandomizationRendering(); //

}
