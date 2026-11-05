package team.heather.cataclysm.effect.api.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.LivingEntity;

/**
 * Called on the server before Pale Corrosion disables half a heart. If any
 * listener returns {@code false}, that tick does nothing.
 */
@FunctionalInterface
public interface AllowPaleCorrosionCallback {

    Event<AllowPaleCorrosionCallback> EVENT = EventFactory.createArrayBacked(AllowPaleCorrosionCallback.class, callbacks -> (entity, amplifier) -> {
        for (AllowPaleCorrosionCallback callback : callbacks) {
            if (!callback.allowCorrosion(entity, amplifier)) {
                return false;
            }
        }

        return true;
    });

    /**
     * @param entity the corroded entity
     * @param amplifier the amplifier of the effect
     * @return {@code true} to let the heart be disabled
     */
    boolean allowCorrosion(LivingEntity entity, int amplifier);
}
