package team.heather.cataclysm.effect.api.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

/**
 * Called on the server when Cleansing is about to reject or remove a harmful
 * effect. If any listener returns {@code false}, the effect stays.
 */
@FunctionalInterface
public interface AllowCleansingCallback {

    Event<AllowCleansingCallback> EVENT = EventFactory.createArrayBacked(AllowCleansingCallback.class, callbacks -> (entity, instance) -> {
        for (AllowCleansingCallback callback : callbacks) {
            if (!callback.allowCleansing(entity, instance)) {
                return false;
            }
        }

        return true;
    });

    /**
     * @param entity the entity with Cleansing
     * @param instance the harmful effect
     * @return {@code true} to let Cleansing get rid of the effect
     */
    boolean allowCleansing(LivingEntity entity, MobEffectInstance instance);
}
