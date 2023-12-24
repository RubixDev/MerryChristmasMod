package mc.mixin;

import mc.TimerAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.tick.TickManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements TimerAccess {
    @Shadow public abstract TickManager getTickManager();

    @Unique
    private final Map<BlockPos, Pair<Long, PlayerEntity>> blocksToBreak = new HashMap<>();

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        TickManager tickManager = this.getTickManager();
        if (!tickManager.shouldTick() || (tickManager.isFrozen() && !tickManager.isStepping())) return;

        List<BlockPos> removeKeys = new ArrayList<>();
        for (Map.Entry<BlockPos, Pair<Long, PlayerEntity>> entry : blocksToBreak.entrySet()) {
            if (entry.getValue().getLeft() <= 1) {
                this.breakBlock(entry.getKey(), true, entry.getValue().getRight());
                removeKeys.add(entry.getKey());
                continue;
            }
            entry.setValue(new Pair<>(entry.getValue().getLeft() - 1, entry.getValue().getRight()));
        }
        for (BlockPos key : removeKeys) {
            blocksToBreak.remove(key);
        }
    }

    @Override
    public void merryChristmas$scheduleBlockBreak(BlockPos pos, long ticks, PlayerEntity player) {
        if (blocksToBreak.containsKey(pos) && blocksToBreak.get(pos).getLeft() > ticks) {
            blocksToBreak.get(pos).setLeft(ticks);
        } else {
            this.blocksToBreak.putIfAbsent(pos, new Pair<>(ticks, player));
        }
    }

    @Override
    public void merryChristmas$printSchedule() {
        blocksToBreak.entrySet().stream().map(entry -> new Pair<>(entry.getKey(), entry.getValue().getLeft())).sorted(Comparator.comparing(Pair::getRight)).forEach(pair -> System.out.println("BB " + pair.getRight() + " | " + pair.getLeft()));
    }
}
