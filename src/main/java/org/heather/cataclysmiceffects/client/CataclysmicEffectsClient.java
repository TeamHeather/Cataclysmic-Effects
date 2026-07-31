package org.heather.cataclysmiceffects;

import net.fabricmc.api.ClientModInitializer;

public final class CataclysmicEffectsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CataclysmicEffectsMod.LOGGER.debug("Initializing...");
        CataclysmicEffectsMod.LOGGER.debug("Initialized.");
    }
}