package team.heather.cataclysm.effect.api.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

/**
 * Called on the server after Pale Corrosion disables half a heart.
 */
@FunctionalInterface
public interface PaleCorrosionHeartDisabledCallback {

    Event<PaleCorrosionHeartDisabledCallback> EVENT = EventFactory.createArrayBacked(PaleCorrosionHeartDisabledCallback.class, callbacks -> (entity, amplifier, disabledHealth) -> {
        for (PaleCorrosionHeartDisabledCallback callback : callbacks) {
            callback.onHeartDisabled(entity, amplifier, disabledHealth);
        }
    });

    /**
     * @param entity the corroded entity
     * @param amplifier the amplifier of the effect
     * @param disabledHealth the total health disabled so far, in half hearts
     */
    void onHeartDisabled(LivingEntity entity, int amplifier, float disabledHealth);
}
