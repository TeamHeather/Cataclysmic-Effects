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
 * Called on the server after an entity receives a mob effect it did not have.
 * Refreshing an active effect calls {@link MobEffectUpdatedCallback} instead.
 */
@FunctionalInterface
public interface MobEffectAddedCallback {

    Event<MobEffectAddedCallback> EVENT = EventFactory.createArrayBacked(MobEffectAddedCallback.class, callbacks -> (entity, instance, source) -> {
        for (MobEffectAddedCallback callback : callbacks) {
            callback.onEffectAdded(entity, instance, source);
        }
    });

    /**
     * Creates a callback that only listens to the given effect.
     *
     * @param effect the effect to listen to
     * @param callback the callback to run for that effect
     * @return the filtered callback
     */
    static MobEffectAddedCallback only(Holder<MobEffect> effect, MobEffectAddedCallback callback) {
        return (entity, instance, source) -> {
            if (instance.is(effect)) {
                callback.onEffectAdded(entity, instance, source);
            }
        };
    }

    /**
     * @param entity the entity that received the effect
     * @param instance the added effect
     * @param source the entity that caused the effect, if any
     */
    void onEffectAdded(LivingEntity entity, MobEffectInstance instance, @Nullable Entity source);
}
