package team.heather.cataclysm.effect.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.heather.cataclysm.effect.effect.CataclysmicMobEffects;
import team.heather.cataclysm.effect.effect.harmful.MortemMobEffect;
import team.heather.cataclysm.effect.effect.harmful.PaleCorrosionMobEffect;
import team.heather.cataclysm.effect.effect.harmful.VulnerabilityMobEffect;

import java.util.Collection;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @ModifyVariable(method = "hurtServer", at = @At("HEAD"), argsOnly = true)
    private float cataclysm$applyVulnerability(float damage) {
        return VulnerabilityMobEffect.tryApply((LivingEntity) (Object) this, damage);
    }

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void cataclysm$blockTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (MortemMobEffect.blocksTotem((LivingEntity) (Object) this)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onEffectsRemoved", at = @At("HEAD"))
    private void cataclysm$restorePaleHearts(Collection<MobEffectInstance> effects, CallbackInfo ci) {
        for (MobEffectInstance effect : effects) {
            if (effect.is(CataclysmicMobEffects.PALE_CORROSION)) {
                PaleCorrosionMobEffect.restoreHearts((LivingEntity) (Object) this);
            }
        }
    }
}
