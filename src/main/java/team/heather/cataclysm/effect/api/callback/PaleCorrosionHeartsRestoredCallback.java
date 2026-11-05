package team.heather.cataclysm.effect.api.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

/**
 * Called on the server after an entity gets back the hearts disabled by Pale
 * Corrosion. It does not run if the effect ended without disabling any heart.
 */
@FunctionalInterface
public interface PaleCorrosionHeartsRestoredCallback {

    Event<PaleCorrosionHeartsRestoredCallback> EVENT = EventFactory.createArrayBacked(PaleCorrosionHeartsRestoredCallback.class, callbacks -> (entity, restoredHealth) -> {
        for (PaleCorrosionHeartsRestoredCallback callback : callbacks) {
            callback.onHeartsRestored(entity, restoredHealth);
        }
    });

    /**
     * @param entity the restored entity
     * @param restoredHealth the health given back, in half hearts
     */
    void onHeartsRestored(LivingEntity entity, float restoredHealth);
}
