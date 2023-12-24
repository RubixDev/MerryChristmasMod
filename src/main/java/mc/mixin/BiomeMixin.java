package mc.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class BiomeMixin {
    @Inject(method = "getPrecipitation", at = @At("HEAD"), cancellable = true)
    private void snowEverywhere(BlockPos pos, CallbackInfoReturnable<Biome.Precipitation> cir) {
        cir.setReturnValue(Biome.Precipitation.SNOW);
    }

    @Inject(method = "getTemperature()F", at = @At("HEAD"), cancellable = true)
    private void snowEverywhere2(CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(-10f);
    }

    @Inject(method = "getTemperature(Lnet/minecraft/util/math/BlockPos;)F", at = @At("HEAD"), cancellable = true)
    private void snowEverywhere2(BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(-10f);
    }
}
