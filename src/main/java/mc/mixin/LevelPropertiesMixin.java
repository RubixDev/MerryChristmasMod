package mc.mixin;

import com.mojang.serialization.Lifecycle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelProperties.class)
public class LevelPropertiesMixin {
    @Shadow private int spawnX;

    @Shadow private int spawnY;

    @Shadow private int spawnZ;

    @Shadow private float spawnAngle;

    @Shadow private long time;

    @Shadow private long timeOfDay;

    @Shadow private boolean raining;

    @Inject(method = "<init>(Lnet/minecraft/world/level/LevelInfo;Lnet/minecraft/world/gen/GeneratorOptions;Lnet/minecraft/world/level/LevelProperties$SpecialProperty;Lcom/mojang/serialization/Lifecycle;)V", at = @At("TAIL"))
    private void overrideProps(LevelInfo levelInfo, GeneratorOptions generatorOptions, LevelProperties.SpecialProperty specialProperty, Lifecycle lifecycle, CallbackInfo ci) {
        this.spawnX = 397;
        this.spawnY = 144;
        this.spawnZ = 580;
        this.spawnAngle = 52.7F;
        this.time = 18000;
        this.timeOfDay = 18000;
        this.raining = true;
    }

    @Inject(method = "setSpawnPos", at = @At("HEAD"), cancellable = true)
    private void preventSpawnOverride(BlockPos pos, float angle, CallbackInfo ci) { ci.cancel(); }
    @Inject(method = "setSpawnX", at = @At("HEAD"), cancellable = true)
    private void preventSpawnOverrideX(int spawnX, CallbackInfo ci) { ci.cancel(); }
    @Inject(method = "setSpawnY", at = @At("HEAD"), cancellable = true)
    private void preventSpawnOverrideY(int spawnY, CallbackInfo ci) { ci.cancel(); }
    @Inject(method = "setSpawnZ", at = @At("HEAD"), cancellable = true)
    private void preventSpawnOverrideZ(int spawnZ, CallbackInfo ci) { ci.cancel(); }
    @Inject(method = "setSpawnAngle", at = @At("HEAD"), cancellable = true)
    private void preventSpawnOverrideAngle(float spawnAngle, CallbackInfo ci) { ci.cancel(); }
}
