package com.jeremyseq.onomatopoeia;

import net.minecraft.world.level.material.MapColor;

public class Util {
    /**
     * This is similar to {@link MapColor#calculateRGBColor}.
     * For some reason the red and blue channels in the original were swapped.
     */
    public static int calculateRGBColorFromMapColor(MapColor mapColor, MapColor.Brightness pBrightness) {
        if (mapColor == MapColor.NONE) {
            return 0;
        } else {
            int i = pBrightness.modifier;
            int j = (mapColor.col >> 16 & 255) * i / 255;
            int k = (mapColor.col >> 8 & 255) * i / 255;
            int l = (mapColor.col & 255) * i / 255;
            return -16777216 | j << 16 | k << 8 | l;
        }
    }
}
