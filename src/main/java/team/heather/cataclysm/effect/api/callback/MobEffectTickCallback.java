package team.heather.cataclysm.effect.api.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

/**
 * Called on the server every tick for each active mob effect of an entity.
 *
 * <p>This runs while vanilla iterates the active effects, so adding or removing
 * effects here must be deferred to a later tick.</p>
 */
@FunctionalInterface
public interface MobEffectTickCallback {

    Event<MobEffectTickCallback> EVENT = EventFactory.createArrayBacked(MobEffectTickCallback.class, callbacks -> (level, entity, instance) -> {
        for (MobEffectTickCallback callback : callbacks) {
            callback.onEffectTick(level, entity, instance);
        }
    });

    /**
     * Creates a callback that only listens to the given effect.
     *
     * @param effect the effect to listen to
     * @param callback the callback to run for that effect
     * @return the filtered callback
     */
    static MobEffectTickCallback only(Holder<MobEffect> effect, MobEffectTickCallback callback) {
        return (level, entity, instance) -> {
            if (instance.is(effect)) {
                callback.onEffectTick(level, entity, instance);
            }
        };
    }

    /**
     * @param level the level of the entity
     * @param entity the entity with the effect
     * @param instance the ticking effect
     */
    void onEffectTick(ServerLevel level, LivingEntity entity, MobEffectInstance instance);
}
