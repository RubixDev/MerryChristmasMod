package mc.mixin;

import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(SimpleOption.DoubleSliderCallbacks.class)
public class SoundsMin50 {
    @ModifyConstant(method = "validate(Ljava/lang/Double;)Ljava/util/Optional;", constant = @Constant(doubleValue = 0.0))
    private double min50(double constant) {
        return 0.5;
    }
}
