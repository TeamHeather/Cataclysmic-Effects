package team.heather.cataclysm.effect.effect.harmful;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import team.heather.cataclysm.effect.api.callback.AllowDisperCallback;
import team.heather.cataclysm.effect.api.callback.DisperBlockDestroyedCallback;
import team.heather.cataclysm.effect.effect.CataclysmicMobEffects;
import team.heather.cataclysm.effect.effect.MobEffectProcessor;

import java.util.Set;

/**
 * Prevents affected players from interacting with most blocks.
 *
 * <p>When the Disper functionality is applied, the targeted block is destroyed
 * with its normal drops, followed by particles and sounds at its position.
 * Blocks listed in {@link #DISPER_BREAKABLE_BLOCKS} are expected to be excluded
 * by the event handler before calling {@link #tryApply(Player, BlockPos)}.</p>
 */
public class DisperMobEffect extends MobEffect {

    /**
     * Blocks that players affected by Disper may break normally.
     */
    public static final Set<Block> DISPER_BREAKABLE_BLOCKS = Set.of(Blocks.SPAWNER, Blocks.CREAKING_HEART);

    public DisperMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    /**
     * Applies the Disper functionality if the player has the Disper effect.
     *
     * <p>The targeted block is destroyed with its normal loot drops before the
     * Disper particles and sounds are emitted. No action is performed when the
     * player is not a server player or does not have the effect.</p>
     *
     * @param player the player whose effects are checked
     * @param blockPos the targeted block position
     * @return {@code true} if the player had Disper and the functionality ran;
     *         otherwise, {@code false}
     */
    public static boolean tryApply(Player player, BlockPos blockPos) {
        if (player instanceof ServerPlayer serverPlayer && serverPlayer.hasEffect(CataclysmicMobEffects.DISPER)
                && !AllowDisperCallback.EVENT.invoker().allowDisper(serverPlayer, blockPos)) {
            return false;
        }

        return MobEffectProcessor.handle(player, CataclysmicMobEffects.DISPER, serverPlayer -> {
            ServerLevel level = serverPlayer.serverLevel();
            BlockState state = level.getBlockState(blockPos);

            level.destroyBlock(blockPos, true, serverPlayer);
            sendParticles(level, blockPos);
            playSounds(level, blockPos);
            DisperBlockDestroyedCallback.EVENT.invoker().onBlockDestroyed(serverPlayer, blockPos, state);
        });
    }

    /**
     * Sends the particles used as visual feedback for Disper.
     *
     * @param level the server level in which to send the particles
     * @param blockPos the center position of the particle effect
     */
    public static void sendParticles(ServerLevel level, BlockPos blockPos) {
        double x = blockPos.getX() + 0.5D;
        double y = blockPos.getY() + 0.5D;
        double z = blockPos.getZ() + 0.5D;

        level.sendParticles(ParticleTypes.WAX_ON, x, y, z, 4, 0.35D, 0.35D, 0.35D, 0.05D);
        level.sendParticles(ParticleTypes.SMOKE, x, y, z, 8, 0.25D, 0.25D, 0.25D, 0.01D);
    }

    /**
     * Plays the sounds used as audible feedback for Disper.
     *
     * @param level the server level in which to play the sounds
     * @param blockPos the source position of the sounds
     */
    public static void playSounds(ServerLevel level, BlockPos blockPos) {
        level.playSound(null, blockPos, SoundEvents.CONDUIT_DEACTIVATE, SoundSource.BLOCKS, 0.8F, 1.75F);
        level.playSound(null, blockPos, SoundEvents.CONDUIT_DEACTIVATE, SoundSource.BLOCKS, 0.8F, 1.85F);
    }
}