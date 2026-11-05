package team.heather.cataclysm.effect.api.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

/**
 * Called on the server after the duration or amplifier of an active mob effect
 * changes because of a refresh, or when it falls back to a hidden effect.
 */
@FunctionalInterface
public interface MobEffectUpdatedCallback {

    Event<MobEffectUpdatedCallback> EVENT = EventFactory.createArrayBacked(MobEffectUpdatedCallback.class, callbacks -> (entity, instance, source) -> {
        for (MobEffectUpdatedCallback callback : callbacks) {
            callback.onEffectUpdated(entity, instance, source);
        }
    });

    /**
     * Creates a callback that only listens to the given effect.
     *
     * @param effect the effect to listen to
     * @param callback the callback to run for that effect
     * @return the filtered callback
     */
    static MobEffectUpdatedCallback only(Holder<MobEffect> effect, MobEffectUpdatedCallback callback) {
        return (entity, instance, source) -> {
            if (instance.is(effect)) {
                callback.onEffectUpdated(entity, instance, source);
            }
        };
    }

    /**
     * @param entity the entity with the effect
     * @param instance the effect in its new state
     * @param source the entity that caused the update, if any
     */
    void onEffectUpdated(LivingEntity entity, MobEffectInstance instance, @Nullable Entity source);
}
