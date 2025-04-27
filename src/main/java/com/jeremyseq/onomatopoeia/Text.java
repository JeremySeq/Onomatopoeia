package com.jeremyseq.onomatopoeia;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.phys.Vec2;

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
        this.randomOffsetX = (random.nextFloat() - 0.5f) * this.getRandomOffsetRange()*2;
        this.randomOffsetY = (random.nextFloat() - 0.5f) * this.getRandomOffsetRange()*2;
        this.randomOffsetZ = (random.nextFloat() - 0.5f) * this.getRandomOffsetRange()*2;

        // random rotation
        this.randomRotation = (random.nextFloat() - 0.5f) * this.getRandomRotationRange()*2;

        // random drift upward speed
        this.randomDriftSpeedY = this.getRandomDriftSpeedYRange().x + random.nextFloat() *
                (this.getRandomDriftSpeedYRange().y - this.getRandomDriftSpeedYRange().x);
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


    /**
     * Override this method to specify the range of random offsets.
     * @return the range of random offsets in blocks, from -x to +x, -y to +y, -z to +z.
     */
    public float getRandomOffsetRange() {
        return .25f;
    }

    /**
     * Override this method to specify the range of random rotation.
     * @return the range of random rotation in degrees, from -x to +x.
     */
    public float getRandomRotationRange() {
        return 15f;
    }

    /**
     * Override this method to specify the range of random upward drift speed.
     * @return the range of random upward drift speed in blocks per second, from min to max.
     */
    public Vec2 getRandomDriftSpeedYRange() {
        return new Vec2(.001f, .003f);
    }
}
