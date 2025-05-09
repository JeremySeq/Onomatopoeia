package com.jeremyseq.onomatopoeia.overlay;

import com.google.common.reflect.TypeToken;
import com.google.gson.*;
import net.minecraft.client.gui.Font;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.awt.*;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;

public class NarrativeTextOverlay {

    private static String narrativeText = "";
    private static long textChangeTime = 0;

    private static final long TRANSITION_DURATION = 1000;
    private static final long VISIBLE_DURATION = 4000;
    private static final long FADE_OUT_DURATION = 1000;

    public static String type = "fun";

    private static final Map<String, Map<String, List<String>>> narrativeEventMap;

    static {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String, List<String>>>>() {}.getType();
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(NarrativeTextOverlay.class.getResourceAsStream("/assets/onomatopoeia/narrative_text.json")))) {
            narrativeEventMap = gson.fromJson(reader, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load narrative_texts.json", e);
        }
    }

    public static void sendNarrativeEvent(String eventCode) {
        List<String> options = narrativeEventMap.get(eventCode).get(type);
        if (options != null && !options.isEmpty()) {
            String selected = options.get(new Random().nextInt(options.size()));
            sendNarrativeText(selected);
        }
    }

    private static void sendNarrativeText(String text) {
        if (!text.equals(narrativeText)) {
            narrativeText = text;
            textChangeTime = System.currentTimeMillis();
        }
    }

    public static final IGuiOverlay NARRATIVE_TEXT = ((gui, poseStack, partialTick, width, height) -> {
        if (!gui.getMinecraft().options.hideGui && !narrativeText.isEmpty()) {
            long currentTime = System.currentTimeMillis();
            long timeSinceChange = currentTime - textChangeTime;

            long totalDuration = TRANSITION_DURATION + VISIBLE_DURATION + FADE_OUT_DURATION;

            if (timeSinceChange > totalDuration) {
                narrativeText = "";
                return;
            }

            float alphaProgress;
            if (timeSinceChange <= TRANSITION_DURATION) {
                // fade in
                alphaProgress = timeSinceChange / (float) TRANSITION_DURATION;
            } else if (timeSinceChange <= TRANSITION_DURATION + VISIBLE_DURATION) {
                // fully visible
                alphaProgress = 1.0f;
            } else {
                // fade out
                long fadeOutElapsed = timeSinceChange - TRANSITION_DURATION - VISIBLE_DURATION;
                alphaProgress = 1.0f - (fadeOutElapsed / (float) FADE_OUT_DURATION);
            }

            alphaProgress = Math.max(0.0f, Math.min(alphaProgress, 1.0f));
            int alpha = (int) (255 * alphaProgress);
            Color color = new Color(255, 255, 255, alpha);

            // slide in (optional, still during fade-in only)
            int targetY = 20;
            int startY = -30;
            int yPos;
            if (timeSinceChange < TRANSITION_DURATION) {
                float slideProgress = timeSinceChange / (float) TRANSITION_DURATION;
                yPos = (int) (startY + (targetY - startY) * slideProgress);
            } else {
                yPos = targetY;
            }

            Font font = gui.getFont();
            int xPos = (width / 2) - (font.width(narrativeText) / 2);

            if (color.getAlpha() > 15) {
                poseStack.drawString(font, narrativeText, xPos, yPos, color.getRGB());
            }
        }
    });
}
