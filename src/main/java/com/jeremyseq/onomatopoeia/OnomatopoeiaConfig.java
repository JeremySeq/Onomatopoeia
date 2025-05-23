package com.jeremyseq.onomatopoeia;

import net.minecraftforge.common.ForgeConfigSpec;

public class OnomatopoeiaConfig {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;


    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_NARRATIVE_TEXT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_HIT_TEXT;
    public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BLOCK_PLACE_TEXT;

    static {
        ENABLE_NARRATIVE_TEXT = BUILDER.comment(" Enables narrative text, rare cinematic lines that appear during special in-game events")
                .define("Enable Narrative Text", true);
        ENABLE_HIT_TEXT = BUILDER.define("Enable Hit Text", true);
        ENABLE_BLOCK_PLACE_TEXT = BUILDER.define("Enable Block Place Text", true);
        SPEC = BUILDER.build();
    }
}
