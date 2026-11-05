package team.heather.cataclysm.effect.api.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

/**
 * Called on the server when a player with Disper interacts with a block. If any
 * listener returns {@code false}, the interaction continues as if the player did
 * not have the effect.
 */
@FunctionalInterface
public interface AllowDisperCallback {

    Event<AllowDisperCallback> EVENT = EventFactory.createArrayBacked(AllowDisperCallback.class, callbacks -> (player, blockPos) -> {
        for (AllowDisperCallback callback : callbacks) {
            if (!callback.allowDisper(player, blockPos)) {
                return false;
            }
        }

        return true;
    });

    /**
     * @param player the player with the effect
     * @param blockPos the targeted block position
     * @return {@code true} to let Disper destroy the block
     */
    boolean allowDisper(ServerPlayer player, BlockPos blockPos);
}
