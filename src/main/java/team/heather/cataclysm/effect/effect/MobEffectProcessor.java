package team.heather.cataclysm.effect.effect;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import team.heather.cataclysm.effect.effect.harmful.DisperMobEffect;
import team.heather.cataclysm.effect.CataclysmicEffectsMod;

import java.util.function.Consumer;

public final class MobEffectProcessor {
    private MobEffectProcessor() {}

    public static void initialize() {
        CataclysmicEffectsMod.LOGGER.debug("Initializing Mob Effect Processor...");
        registerDisperHandlers();
        CataclysmicEffectsMod.LOGGER.debug("Mob Effect Processor initialized.");
    }

    private static void registerDisperHandlers() {
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) ->
                DisperMobEffect.DISPER_BREAKABLE_BLOCKS.contains(state.getBlock()) || !DisperMobEffect.tryApply(player, pos));

        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ItemStack stack = player.getItemInHand(hand);

            if (player.isSpectator() || !(stack.getItem() instanceof BlockItem)) {
                return InteractionResult.PASS;
            }

            BlockPos pos = new BlockPlaceContext(player, hand, stack, hitResult).getClickedPos();
            return DisperMobEffect.tryApply(player, pos) ? InteractionResult.FAIL : InteractionResult.PASS;
        });
    }

    /**
     * Executes the specified task when the player is a server player and has the given effect.
     *
     * @param player the player to check
     * @param effect the mob effect required to execute the task
     * @param task the task to execute with the server player
     * @return {@code true} if the task was executed; otherwise, {@code false}
     */
    public static boolean handle(Player player, Holder<MobEffect> effect, Consumer<ServerPlayer> task) {
        if (player instanceof ServerPlayer serverPlayer && serverPlayer.hasEffect(effect)) {
            task.accept(serverPlayer);
            return true;
        }

        return false;
    }
}