package team.heather.cataclysm.effect.effect.harmful;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import team.heather.cataclysm.effect.effect.CataclysmicMobEffects;

/**
 * Prevents affected entities from being saved by any kind of totem.
 *
 * <p>The totem check itself is cancelled in
 * {@link team.heather.cataclysm.effect.mixin.LivingEntityMixin}, which covers
 * every item with the death protection component. While active, two totem
 * particles slowly orbit the entity as an unobtrusive reminder.</p>
 */
public class MortemMobEffect extends MobEffect {

    private static final int PARTICLE_INTERVAL = 8;
    private static final int PARTICLES_PER_ORBIT = 2;
    private static final double ORBIT_RADIUS = 0.6D;
    private static final double ORBIT_HEIGHT = 0.7D;
    private static final float ORBIT_SPEED = 0.05F;
    private static final double PARTICLE_LIFT = 0.1D;

    public MortemMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    /**
     * Checks whether the totem of the given entity should be ignored.
     *
     * @param entity the entity about to die
     * @return {@code true} if the entity has the Mortem effect
     */
    public static boolean blocksTotem(LivingEntity entity) {
        return entity.hasEffect(CataclysmicMobEffects.MORTEM);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity entity, int amplifier) {
        MobEffectInstance instance = entity.getEffect(CataclysmicMobEffects.MORTEM);

        if (instance != null && instance.isVisible() && !entity.isInvisible()) {
            sendParticles(level, entity);
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % PARTICLE_INTERVAL == 0;
    }

    /**
     * Sends the particles used as visual feedback for Mortem.
     *
     * @param level the server level in which to send the particles
     * @param entity the entity the particles orbit around
     */
    public static void sendParticles(ServerLevel level, LivingEntity entity) {
        float angle = entity.tickCount * ORBIT_SPEED;
        double y = entity.getY() + entity.getBbHeight() * ORBIT_HEIGHT;

        for (int i = 0; i < PARTICLES_PER_ORBIT; i++) {
            float particleAngle = angle + i * Mth.TWO_PI / PARTICLES_PER_ORBIT;
            double x = entity.getX() + Mth.cos(particleAngle) * ORBIT_RADIUS;
            double z = entity.getZ() + Mth.sin(particleAngle) * ORBIT_RADIUS;

            // A count of 0 uses the offset as the velocity of a single particle
            level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING, x, y, z, 0, 0.0D, PARTICLE_LIFT, 0.0D, 1.0D);
        }
    }
}
