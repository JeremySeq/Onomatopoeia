package com.jeremyseq.onomatopoeia;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Onomatopoeia.MODID, value = Dist.CLIENT)
public class TextRenderer {

    private static final List<Text> textList = new java.util.ArrayList<>();

    public static void addText(Text text) {
        textList.add(text);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderWorldStage(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            PoseStack poseStack = event.getPoseStack();
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) {
                return;
            }

            List<Text> snapshot = new ArrayList<>(textList); // used to prevent ConcurrentModificationException
            for (Text text : snapshot) {
                text.render(poseStack);
            }

            // remove text at end of duration
            textList.removeIf((text) -> System.currentTimeMillis() - text.getTimestamp() > text.getDuration());
        }
    }

}
