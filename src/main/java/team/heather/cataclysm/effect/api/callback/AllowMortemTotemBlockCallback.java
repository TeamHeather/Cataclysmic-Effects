package team.heather.cataclysm.effect.api.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/**
 * Called on the server when Mortem is about to block a totem. If any listener
 * returns {@code false}, the totem saves the entity as usual.
 */
@FunctionalInterface
public interface AllowMortemTotemBlockCallback {

    Event<AllowMortemTotemBlockCallback> EVENT = EventFactory.createArrayBacked(AllowMortemTotemBlockCallback.class, callbacks -> (entity, source, totem) -> {
        for (AllowMortemTotemBlockCallback callback : callbacks) {
            if (!callback.allowTotemBlock(entity, source, totem)) {
                return false;
            }
        }

        return true;
    });

    /**
     * @param entity the entity about to die
     * @param source the source of the lethal damage
     * @param totem the totem the entity is holding
     * @return {@code true} to let Mortem block the totem
     */
    boolean allowTotemBlock(LivingEntity entity, DamageSource source, ItemStack totem);
}
