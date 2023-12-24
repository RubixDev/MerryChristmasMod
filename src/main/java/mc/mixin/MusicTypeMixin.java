package mc.mixin;

import net.minecraft.client.sound.MusicType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(MusicType.class)
public class MusicTypeMixin {
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 20))
    private static int noMenuMinDelay(int constant) { return 0; }
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 600))
    private static int noMenuMaxDelay(int constant) { return 0; }
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 12000))
    private static int noGameMinDelay(int constant) { return 0; }
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 24000))
    private static int noGameMaxDelay(int constant) { return 0; }
}
