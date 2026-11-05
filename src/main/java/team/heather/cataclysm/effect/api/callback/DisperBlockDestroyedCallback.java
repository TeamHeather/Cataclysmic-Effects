package team.heather.cataclysm.effect.api.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Called on the server after Disper destroys a block.
 */
@FunctionalInterface
public interface DisperBlockDestroyedCallback {

    Event<DisperBlockDestroyedCallback> EVENT = EventFactory.createArrayBacked(DisperBlockDestroyedCallback.class, callbacks -> (player, blockPos, state) -> {
        for (DisperBlockDestroyedCallback callback : callbacks) {
            callback.onBlockDestroyed(player, blockPos, state);
        }
    });

    /**
     * @param player the player with the effect
     * @param blockPos the position of the destroyed block
     * @param state the state of the block before it was destroyed
     */
    void onBlockDestroyed(ServerPlayer player, BlockPos blockPos, BlockState state);
}
