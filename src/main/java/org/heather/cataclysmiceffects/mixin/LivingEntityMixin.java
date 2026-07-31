package org.heather.cataclysmiceffects.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.heather.cataclysmiceffects.CataclysmicMobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true, name = "damage")
    private float arcadia$hurtServer(float damage) {
        LivingEntity entity = (LivingEntity) (Object) this;
        MobEffectInstance effect = entity.getEffect(CataclysmicMobEffects.VULNERABILITY);

        return effect == null ? damage : damage * (effect.getAmplifier() + 2);
    }
}