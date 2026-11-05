package team.heather.cataclysm.effect.client;

import net.fabricmc.api.ClientModInitializer;
import team.heather.cataclysm.effect.CataclysmicEffectsMod;

public final class CataclysmicEffectsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        CataclysmicEffectsMod.LOGGER.debug("Initializing...");
        CataclysmicEffectsMod.LOGGER.debug("Initialized.");
    }
}