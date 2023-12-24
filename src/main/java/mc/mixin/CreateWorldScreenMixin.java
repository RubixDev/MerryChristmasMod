package mc.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.world.GeneratorOptionsHolder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.WorldPreset;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.OptionalLong;

@Mixin(CreateWorldScreen.class)
public abstract class CreateWorldScreenMixin extends Screen {
    protected CreateWorldScreenMixin(Text title) {
        super(title);
    }

    @Shadow protected abstract void createLevel();

    @Shadow @Final
    WorldCreator worldCreator;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void createWorldWithHardcodedSettings(MinecraftClient client, Screen parent, GeneratorOptionsHolder generatorOptionsHolder, Optional<RegistryKey<WorldPreset>> defaultWorldType, OptionalLong seed, CallbackInfo ci) {
        this.worldCreator.setSeed("7236835963411870415");
        this.worldCreator.setDifficulty(Difficulty.EASY);
        this.worldCreator.setWorldName("christmas");
        this.worldCreator.setGameMode(WorldCreator.Mode.SURVIVAL);
        this.worldCreator.setCheatsEnabled(false);
        GameRules gameRules = new GameRules();
        gameRules.get(GameRules.DO_DAYLIGHT_CYCLE).set(false, null);
        gameRules.get(GameRules.DO_WEATHER_CYCLE).set(false, null);
        gameRules.get(GameRules.SPAWN_RADIUS).set(0, null);
        this.worldCreator.setGameRules(gameRules);
        this.client = client;
        this.createLevel();
    }
}
