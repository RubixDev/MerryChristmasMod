package mc;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public interface TimerAccess {
    void merryChristmas$scheduleBlockBreak(BlockPos pos, long ticks, PlayerEntity player);
    void merryChristmas$printSchedule();
}
