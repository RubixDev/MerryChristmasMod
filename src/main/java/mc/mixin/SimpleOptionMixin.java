package mc.mixin;

import net.minecraft.client.option.SimpleOption;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SimpleOption.class)
public class SimpleOptionMixin {
    @Redirect(method = "method_42403", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;error(Ljava/lang/String;)V"))
    private void preventLog(Logger instance, String s) {}
}
