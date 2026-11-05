package team.heather.cataclysm.effect;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.heather.cataclysm.effect.effect.CataclysmicMobEffects;
import team.heather.cataclysm.effect.effect.MobEffectProcessor;

public final class CataclysmicEffectsMod implements ModInitializer {

    public static final String MOD_ID = "cataclysmic_effects";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.debug("Initializing...");
        CataclysmicMobEffects.initialize();
        MobEffectProcessor.initialize();
        LOGGER.debug("Initialized.");
    }

    public static ResourceLocation createIdentifier(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}