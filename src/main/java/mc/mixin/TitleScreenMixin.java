package mc.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.CreditsAndAttributionScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.PressableTextWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {
    @Shadow @Final public static Text COPYRIGHT;

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)I"), index = 1)
    private String aaaaa(String original) {
        return "Vanilla Minecraft (definitely not modded)";
    }

    @Inject(method = "initWidgetsNormal", at = @At("HEAD"), cancellable = true)
    private void noMultiplayer(int y, int spacingY, CallbackInfo ci) {
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Play"), button -> {
            if (this.client.getLevelStorage().getLevelList().levels().stream().filter(level -> level.getRootPath().equals("christmas")).findFirst().isEmpty()) {
                System.out.println("BBBBB Create world");
                CreateWorldScreen.create(this.client, null);

//                ResourcePackManager resourcePackManager = new ResourcePackManager(new VanillaDataPackProvider(client.getSymlinkFinder()));
//                SaveLoading.DataPacks dataPacks = new SaveLoading.DataPacks(resourcePackManager, DataConfiguration.SAFE_MODE, false, true);
//                SaveLoading.ServerConfig serverConfig = new SaveLoading.ServerConfig(dataPacks, CommandManager.RegistrationEnvironment.INTEGRATED, 2);
//                CompletableFuture<GeneratorOptionsHolder> completableFuture = SaveLoading.load(serverConfig, context -> new SaveLoading.LoadContext<>(new CreateWorldScreen.WorldCreationSettings(new WorldGenSettings(GeneratorOptions.createRandom(), WorldPresets.createDemoOptions(context.worldGenRegistryManager())), context.dataConfiguration()), context.dimensionsRegistryManager()), (resourceManager, dataPackContents, combinedDynamicRegistries, generatorOptions) -> {
//                    resourceManager.close();
//                    return new GeneratorOptionsHolder(generatorOptions.worldGenSettings(), combinedDynamicRegistries, dataPackContents, generatorOptions.dataConfiguration());
//                }, Util.getMainWorkerExecutor(), client);
//                client.runTasks(completableFuture::isDone);
//                WorldCreator worldCreator = new WorldCreator(client.getLevelStorage().getSavesDirectory(), completableFuture.join(), Optional.of(WorldPresets.DEFAULT), OptionalLong.of(-8169697951202909253L));
//
//                GeneratorOptionsHolder generatorOptionsHolder = worldCreator.getGeneratorOptionsHolder();
//                DimensionOptionsRegistryHolder.DimensionsConfig dimensionsConfig = generatorOptionsHolder.selectedDimensions().toConfig(generatorOptionsHolder.dimensionOptionsRegistry());
//                CombinedDynamicRegistries<ServerDynamicRegistryType> combinedDynamicRegistries = generatorOptionsHolder.combinedDynamicRegistries().with(ServerDynamicRegistryType.DIMENSIONS, dimensionsConfig.toDynamicRegistryManager());
//                Lifecycle lifecycle = FeatureFlags.isNotVanilla(generatorOptionsHolder.dataConfiguration().enabledFeatures()) ? Lifecycle.experimental() : Lifecycle.stable();
//                Lifecycle lifecycle2 = combinedDynamicRegistries.getCombinedRegistryManager().getRegistryLifecycle();
//                Lifecycle lifecycle3 = lifecycle2.add(lifecycle);
//                boolean bl = lifecycle2 == Lifecycle.stable();
//                IntegratedServerLoader.tryLoad(this.client, this, lifecycle3, () -> this.startServer(dimensionsConfig.specialWorldProperty(), combinedDynamicRegistries, lifecycle3), bl);
            } else {
                System.out.println("CCCCC Loading existing world");
                this.client.createIntegratedServerLoader().start("christmas", () -> {
                    System.err.println("CANCELLED");
                    this.client.setScreen(new TitleScreen());
                });
            }
//            this.client.setScreen(new SelectWorldScreen(this));
        }).dimensions(this.width / 2 - 100, y, 200, 20).build());
        ci.cancel();
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/TitleScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;", ordinal = 0, shift = At.Shift.BEFORE), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void removeButtons(CallbackInfo ci, int i, int j, int k, int l) {
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.options"), button -> this.client.setScreen(new OptionsScreen(this, this.client.options))).dimensions(this.width / 2 - 100, l + 72 + 12 - 48, 98, 20).build());
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("menu.quit"), button -> this.client.scheduleStop()).dimensions(this.width / 2 + 2, l + 72 + 12 - 48, 98, 20).build());
        this.addDrawableChild(new PressableTextWidget(j, this.height - 10, i, 10, COPYRIGHT, button -> this.client.setScreen(new CreditsAndAttributionScreen(this)), this.textRenderer));
        ci.cancel();
    }
}
