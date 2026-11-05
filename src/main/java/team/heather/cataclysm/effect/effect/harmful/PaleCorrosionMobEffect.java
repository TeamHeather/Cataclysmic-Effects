package team.heather.cataclysm.effect.effect.harmful;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import team.heather.cataclysm.effect.CataclysmicEffectsMod;
import team.heather.cataclysm.effect.api.callback.AllowPaleCorrosionCallback;
import team.heather.cataclysm.effect.api.callback.PaleCorrosionHeartDisabledCallback;
import team.heather.cataclysm.effect.api.callback.PaleCorrosionHeartsRestoredCallback;

/**
 * Disables half a heart of the affected entity each time the effect ticks.
 *
 * <p>Hearts are disabled through a max health modifier, so they stay disabled
 * across saves and are synced to the client, which renders them as pale hearts.
 * Like poison, it cannot kill: the last half heart is never disabled.</p>
 *
 * <p>The hearts come back through {@link #restoreHearts(LivingEntity)} when the
 * effect ends, not through {@link #removeAttributeModifiers}, as vanilla also
 * calls that method whenever the effect is refreshed.</p>
 */
public class PaleCorrosionMobEffect extends MobEffect {

    public static final ResourceLocation MODIFIER_ID = CataclysmicEffectsMod.createIdentifier("pale_corrosion");

    private static final int BASE_TICK_INTERVAL = 40;
    private static final float DISABLED_HEALTH_PER_TICK = 1.0F;
    private static final float MIN_MAX_HEALTH = 1.0F;

    private static final float SOUND_VOLUME = 0.8F;
    private static final float HURT_SOUND_PITCH = 1.3F;
    private static final float BREAK_SOUND_PITCH = 0.7F;

    public PaleCorrosionMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        AttributeInstance maxHealth = entity.getAttribute(Attributes.MAX_HEALTH);

        if (maxHealth == null || entity.getMaxHealth() - DISABLED_HEALTH_PER_TICK < MIN_MAX_HEALTH
                || !AllowPaleCorrosionCallback.EVENT.invoker().allowCorrosion(entity, amplifier)) {
            return true;
        }

        double disabledHealth = getDisabledHealth(entity) + DISABLED_HEALTH_PER_TICK;

        maxHealth.removeModifier(MODIFIER_ID);
        maxHealth.addPermanentModifier(new AttributeModifier(MODIFIER_ID, -disabledHealth, AttributeModifier.Operation.ADD_VALUE));

        sendParticles(level, entity);
        playSounds(level, entity);
        PaleCorrosionHeartDisabledCallback.EVENT.invoker().onHeartDisabled(entity, amplifier, (float) disabledHealth);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int interval = BASE_TICK_INTERVAL >> amplifier;
        return interval <= 0 || duration % interval == 0;
    }

    /**
     * Gets the amount of health currently disabled by Pale Corrosion.
     *
     * @param entity the entity to check
     * @return the disabled health, in half hearts
     */
    public static float getDisabledHealth(LivingEntity entity) {
        AttributeInstance maxHealth = entity.getAttribute(Attributes.MAX_HEALTH);
        AttributeModifier modifier = maxHealth == null ? null : maxHealth.getModifier(MODIFIER_ID);

        return modifier == null ? 0.0F : (float) -modifier.amount();
    }

    /**
     * Gives back every heart disabled by Pale Corrosion. The hearts return
     * empty, so the entity still has to heal them.
     *
     * @param entity the entity to restore
     */
    public static void restoreHearts(LivingEntity entity) {
        AttributeInstance maxHealth = entity.getAttribute(Attributes.MAX_HEALTH);
        float restoredHealth = getDisabledHealth(entity);

        if (maxHealth != null && maxHealth.removeModifier(MODIFIER_ID)) {
            PaleCorrosionHeartsRestoredCallback.EVENT.invoker().onHeartsRestored(entity, restoredHealth);
        }
    }

    /**
     * Sends the particles used as visual feedback for Pale Corrosion.
     *
     * @param level the server level in which to send the particles
     * @param entity the corroded entity
     */
    public static void sendParticles(ServerLevel level, LivingEntity entity) {
        double x = entity.getX();
        double y = entity.getY() + entity.getBbHeight() * 0.6D;
        double z = entity.getZ();

        level.sendParticles(ParticleTypes.PALE_OAK_LEAVES, x, y, z, 3, 0.3D, 0.4D, 0.3D, 0.01D);
        level.sendParticles(ParticleTypes.WHITE_ASH, x, y, z, 6, 0.35D, 0.45D, 0.35D, 0.02D);
    }

    /**
     * Plays the sounds used as audible feedback for Pale Corrosion.
     *
     * @param level the server level in which to play the sounds
     * @param entity the corroded entity
     */
    public static void playSounds(ServerLevel level, LivingEntity entity) {
        level.playSound(null, entity.blockPosition(), SoundEvents.CREAKING_HEART_HURT, entity.getSoundSource(), SOUND_VOLUME, HURT_SOUND_PITCH);
        level.playSound(null, entity.blockPosition(), SoundEvents.RESIN_BREAK, entity.getSoundSource(), SOUND_VOLUME, BREAK_SOUND_PITCH);
    }
}
