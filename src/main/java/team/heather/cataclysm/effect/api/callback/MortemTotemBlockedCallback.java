package team.heather.cataclysm.effect.api.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Called on the server when Mortem stops a totem from saving an entity. It does
 * not run for entities that die without holding a totem.
 */
@FunctionalInterface
public interface MortemTotemBlockedCallback {

    Event<MortemTotemBlockedCallback> EVENT = EventFactory.createArrayBacked(MortemTotemBlockedCallback.class, callbacks -> (entity, source, totem) -> {
        for (MortemTotemBlockedCallback callback : callbacks) {
            callback.onTotemBlocked(entity, source, totem);
        }
    });

    /**
     * @param entity the entity that will die
     * @param source the source of the lethal damage
     * @param totem the blocked totem, which is not consumed
     */
    void onTotemBlocked(LivingEntity entity, DamageSource source, ItemStack totem);
}
