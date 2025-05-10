package com.jeremyseq.onomatopoeia.mixin;

import com.jeremyseq.onomatopoeia.TextRenderer;
import com.jeremyseq.onomatopoeia.Util;
import com.jeremyseq.onomatopoeia.overlay.NarrativeTextOverlay;
import com.jeremyseq.onomatopoeia.text_types.BlockPlaceText;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.awt.*;

@Mixin(BlockItem.class)
public abstract class PlaceBlockMixin {
    @Shadow @Nullable protected abstract BlockState getPlacementState(BlockPlaceContext pContext);

    @Inject(at = @At("HEAD"), method = "placeBlock")
    public void placeBlock(BlockPlaceContext pContext, BlockState pState, CallbackInfoReturnable<Boolean> cir) {
        // check if the block is placed by a player
        if (pContext.getPlayer() != null) {
            if (pState.getBlock() == Blocks.DRAGON_HEAD) {
                NarrativeTextOverlay.sendNarrativeEvent("dragon_head_placed");
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "place")
    public void onPlaceBlock2(BlockPlaceContext pContext, CallbackInfoReturnable<InteractionResult> cir) {
        // only run if block was actually placed
        if (cir.getReturnValue() == InteractionResult.sidedSuccess(true) && pContext.getPlayer() != null) {
            BlockState blockstate = this.getPlacementState(pContext);
            if (blockstate == null) return;
            int rgb = Util.calculateRGBColorFromMapColor(
                    blockstate.getMapColor(pContext.getLevel(), pContext.getClickedPos()), MapColor.Brightness.HIGH);
            Color color = new Color(rgb);
            TextRenderer.addText(new BlockPlaceText(
                    pContext.getClickLocation().x,
                    pContext.getClickLocation().y,
                    pContext.getClickLocation().z,
                    1, blockstate, color
            ));
        }
    }
}
