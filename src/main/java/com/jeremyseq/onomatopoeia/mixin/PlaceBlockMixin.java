package com.jeremyseq.onomatopoeia.mixin;

import com.jeremyseq.onomatopoeia.overlay.NarrativeTextOverlay;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class PlaceBlockMixin {
    @Inject(at = @At("HEAD"), method = "placeBlock")
    public void placeBlock(BlockPlaceContext pContext, BlockState pState, CallbackInfoReturnable<Boolean> cir) {
        // check if the block is placed by a player
        if (pContext.getPlayer() != null) {
            if (pState.getBlock() == Blocks.DRAGON_HEAD) {
                NarrativeTextOverlay.sendNarrativeEvent("dragon_head_placed");
            }
        }
    }
}
