package org.heather.cataclysmiceffects.internal;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.heather.cataclysmiceffects.CataclysmicMobEffects;
import org.heather.cataclysmiceffects.MobEffectProcessor;

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

    public static Identifier createIdentifier(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}