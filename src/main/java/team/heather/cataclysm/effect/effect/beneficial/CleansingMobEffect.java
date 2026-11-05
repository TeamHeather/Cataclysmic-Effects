package team.heather.cataclysm.effect.effect.beneficial;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import team.heather.cataclysm.effect.api.callback.AllowCleansingCallback;
import team.heather.cataclysm.effect.effect.CataclysmicMobEffects;

import java.util.List;

/**
 * Keeps the affected entity free of harmful mob effects while active. From
 * level II it also puts out fire and removes freezing.
 *
 * <p>Harmful effects are removed from
 * {@link team.heather.cataclysm.effect.mixin.LivingEntityMixin} before vanilla
 * ticks the active effects, as removing them from
 * {@link #applyEffectTick} would break that iteration.</p>
 */
public class CleansingMobEffect extends MobEffect {

    private static final int STATUS_CLEANSING_AMPLIFIER = 1;

    private static final float SOUND_VOLUME = 0.8F;
    private static final float BREW_SOUND_PITCH = 1.6F;
    private static final float EXTINGUISH_SOUND_PITCH = 1.4F;

    public CleansingMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    /**
     * Checks whether Cleansing stops the given effect from reaching the entity.
     *
     * @param entity the entity receiving the effect
     * @param instance the effect about to be applied
     * @return {@code true} if the effect should be rejected
     */
    public static boolean blocks(LivingEntity entity, MobEffectInstance instance) {
        return entity.hasEffect(CataclysmicMobEffects.CLEANSING) && shouldCleanse(entity, instance);
    }

    /**
     * Removes every harmful effect from the entity if it has Cleansing.
     *
     * @param level the level of the entity
     * @param entity the entity to cleanse
     */
    public static void cleanseEffects(ServerLevel level, LivingEntity entity) {
        if (!entity.hasEffect(CataclysmicMobEffects.CLEANSING)) return;

        boolean cleansed = false;

        for (MobEffectInstance instance : List.copyOf(entity.getActiveEffects())) {
            if (shouldCleanse(entity, instance)) {
                cleansed |= entity.removeEffect(instance.getEffect());
            }
        }

        if (cleansed) {
            sendParticles(level, entity);
            level.playSound(null, entity.blockPosition(), SoundEvents.BREWING_STAND_BREW, entity.getSoundSource(), SOUND_VOLUME, BREW_SOUND_PITCH);
        }
    }

    private static boolean shouldCleanse(LivingEntity entity, MobEffectInstance instance) {
        return instance.getEffect().value().getCategory() == MobEffectCategory.HARMFUL
                && AllowCleansingCallback.EVENT.invoker().allowCleansing(entity, instance);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        if (amplifier < STATUS_CLEANSING_AMPLIFIER) return true;

        boolean burning = entity.getRemainingFireTicks() > 0;

        entity.clearFire();
        entity.setTicksFrozen(0);

        if (burning) {
            sendParticles(level, entity);
            level.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXTINGUISH_FIRE, entity.getSoundSource(), SOUND_VOLUME, EXTINGUISH_SOUND_PITCH);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    /**
     * Sends the particles used as visual feedback for Cleansing.
     *
     * @param level the server level in which to send the particles
     * @param entity the cleansed entity
     */
    public static void sendParticles(ServerLevel level, LivingEntity entity) {
        double y = entity.getY() + entity.getBbHeight() * 0.5D;

        level.sendParticles(ParticleTypes.CLOUD, entity.getX(), y, entity.getZ(), 6, 0.3D, 0.4D, 0.3D, 0.01D);
    }
}
