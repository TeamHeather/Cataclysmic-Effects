package team.heather.cataclysm.effect.api.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

/**
 * Called on the server after a mob effect is removed from an entity.
 *
 * <p>Expired effects are removed while vanilla iterates the active effects, so
 * adding or removing effects here must be deferred to a later tick.</p>
 */
@FunctionalInterface
public interface MobEffectRemovedCallback {

    Event<MobEffectRemovedCallback> EVENT = EventFactory.createArrayBacked(MobEffectRemovedCallback.class, callbacks -> (entity, instance, expired) -> {
        for (MobEffectRemovedCallback callback : callbacks) {
            callback.onEffectRemoved(entity, instance, expired);
        }
    });

    /**
     * Creates a callback that only listens to the given effect.
     *
     * @param effect the effect to listen to
     * @param callback the callback to run for that effect
     * @return the filtered callback
     */
    static MobEffectRemovedCallback only(Holder<MobEffect> effect, MobEffectRemovedCallback callback) {
        return (entity, instance, expired) -> {
            if (instance.is(effect)) {
                callback.onEffectRemoved(entity, instance, expired);
            }
        };
    }

    /**
     * @param entity the entity that lost the effect
     * @param instance the removed effect
     * @param expired {@code true} if the effect ran out of time, {@code false}
     *                if something cleared it, such as milk or a command
     */
    void onEffectRemoved(LivingEntity entity, MobEffectInstance instance, boolean expired);
}
