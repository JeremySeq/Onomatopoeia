package com.jeremyseq.onomatopoeia.text_types;

import com.jeremyseq.onomatopoeia.Text;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BlockPlaceText extends Text {

    private final String text;
    private final int size;
    private Color color = new Color(255, 255, 160); // light yellow

    private static final Map<SoundType, List<String>> materialSounds = new HashMap<>();

    static {
        materialSounds.put(SoundType.STONE, Arrays.asList("clack", "chunk", "clang"));
        materialSounds.put(SoundType.WOOD, Arrays.asList("thunk", "tap", "knock"));
        materialSounds.put(SoundType.METAL, Arrays.asList("clang", "clink", "ping"));
        materialSounds.put(SoundType.GLASS, Arrays.asList("tink", "clink", "crack"));
        materialSounds.put(SoundType.GRAVEL, Arrays.asList("plop", "thud", "clop"));
        materialSounds.put(SoundType.ROOTED_DIRT, Arrays.asList("thud", "splat", "ploof"));
        materialSounds.put(SoundType.SAND, Arrays.asList("shff", "fss", "pff"));
        materialSounds.put(SoundType.WOOL, Arrays.asList("poof", "fwump", "thump"));
        materialSounds.put(SoundType.SNOW, Arrays.asList("shff", "pff", "crump"));
        // fallback/default sounds will be used if material is not recognized
    }

    public BlockPlaceText(double x, double y, double z, int size, BlockState state) {
        super(x, y, z);
        this.size = size;
        this.text = getRandomTextForMaterial(state.getSoundType());
        this.setDuration(800);
    }

    public BlockPlaceText(double x, double y, double z, int size, BlockState state, Color color) {
        super(x, y, z);
        this.size = size;
        this.text = getRandomTextForMaterial(state.getSoundType());
        this.color = color;
        this.setDuration(800);
    }

    private static String getRandomTextForMaterial(SoundType soundType) {
        List<String> options = materialSounds.getOrDefault(soundType, Arrays.asList("click", "pop", "plop"));
        return options.get(new Random().nextInt(options.size()));
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
        float duration = this.getDuration();

        float growDuration = 150f;
        float scale;
        float alpha = 1.0f;

        if (elapsed > duration) return;

        if (elapsed <= growDuration) {
            scale = this.size * (elapsed / growDuration);
        } else {
            float fadeProgress = (elapsed - growDuration) / (duration - growDuration);
            scale = this.size * (1.0f - fadeProgress);
            alpha = 1.0f - fadeProgress;
        }

        poseStack.scale(scale, scale, scale);

        int fadedColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (alpha * 255)).getRGB();

        if (alpha > 25 / 255f) {
            fontRenderer.drawInBatch(text, -textWidth / 2f, -textHeight / 2f, fadedColor, false, poseStack.last().pose(), bufferSource, displayMode, 0, 15728880);
        }
    }

    @Override
    public boolean doRandomizationRendering() {
        return true;
    }

    @Override
    public Vec2 getRandomDriftSpeedYRange() {
        return new Vec2(0.0005f, 0.001f); // slight upward float
    }
}
