package mc.mixin;

import mc.TimerAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"), cancellable = true)
    private void breakSurroundingBlocks(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (context.getPlayer() == null || context.getPlayer().isSneaking()) return;
        BlockPos targetBlock = context.getBlockPos().offset(context.getSide().getOpposite());
        Block targetBlockKind = context.getWorld().getBlockState(targetBlock).getBlock();

        if (context.getWorld() instanceof ServerWorld world) {
            recurseBreak(null, targetBlock, targetBlockKind, world, context.getPlayer(), 1);
//            ((TimerAccess) world).merryChristmas$printSchedule();
        }
        context.getWorld().breakBlock(targetBlock, true, context.getPlayer());

        cir.setReturnValue(false);
    }

    @Unique
    private void recurseBreak(@Nullable Direction prevDir, BlockPos prevPos, Block kind, ServerWorld world, PlayerEntity player, int depth) {
        if (depth > 4) return;
        for (Direction dir : Direction.values()) {
            if (prevDir != null && dir.equals(prevDir.getOpposite())) continue;
            BlockPos newPos = prevPos.offset(dir);
            if (world.getBlockState(newPos).getBlock().equals(kind)) {
                ((TimerAccess) world).merryChristmas$scheduleBlockBreak(newPos, depth * 2L, player);
            }
            recurseBreak(dir, newPos, kind, world, player, depth + 1);
        }
    }
}
