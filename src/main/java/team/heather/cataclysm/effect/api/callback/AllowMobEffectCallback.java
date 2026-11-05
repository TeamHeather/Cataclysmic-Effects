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
 * Called on the server before a mob effect is added to or refreshed on an entity.
 * If any listener returns {@code false}, the effect is rejected.
 *
 * <p>Effects applied through {@link LivingEntity#forceAddEffect} skip this callback.</p>
 */
@FunctionalInterface
public interface AllowMobEffectCallback {

    Event<AllowMobEffectCallback> EVENT = EventFactory.createArrayBacked(AllowMobEffectCallback.class, callbacks -> (entity, instance, source) -> {
        for (AllowMobEffectCallback callback : callbacks) {
            if (!callback.allowEffect(entity, instance, source)) {
                return false;
            }
        }

        return true;
    });

    /**
     * Creates a callback that only listens to the given effect.
     *
     * @param effect the effect to listen to
     * @param callback the callback to run for that effect
     * @return the filtered callback
     */
    static AllowMobEffectCallback only(Holder<MobEffect> effect, AllowMobEffectCallback callback) {
        return (entity, instance, source) -> !instance.is(effect) || callback.allowEffect(entity, instance, source);
    }

    /**
     * @param entity the entity receiving the effect
     * @param instance the effect about to be applied
     * @param source the entity that caused the effect, if any
     * @return {@code true} to let the effect through
     */
    boolean allowEffect(LivingEntity entity, MobEffectInstance instance, @Nullable Entity source);
}
