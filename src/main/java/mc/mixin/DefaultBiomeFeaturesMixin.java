package mc.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultBiomeFeatures.class)
public class DefaultBiomeFeaturesMixin {
    @Inject(method = {"addSnowyMobs", "addFarmAnimals", "addCaveMobs", "addPlainsMobs", "addDesertMobs", "addDripstoneCaveMobs", "addMushroomMobs", "addJungleMobs", "addEndMobs"}, at = @At("HEAD"))
    private static void spawnSnowGolems(SpawnSettings.Builder builder, CallbackInfo ci) {
        builder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.SNOW_GOLEM, 1000, 1, 2));
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SNOW_GOLEM, 1000, 1, 2));
    }

    @Inject(method = "addMonsters", at = @At("HEAD"))
    private static void spawnSnowGolems(SpawnSettings.Builder builder, int zombieWeight, int zombieVillagerWeight, int skeletonWeight, boolean drowned, CallbackInfo ci) {
        builder.spawn(SpawnGroup.MONSTER, new SpawnSettings.SpawnEntry(EntityType.SNOW_GOLEM, 1000, 1, 2));
    }
}
