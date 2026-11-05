package team.heather.cataclysm.effect.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.heather.cataclysm.effect.api.callback.AllowMobEffectCallback;
import team.heather.cataclysm.effect.api.callback.MobEffectAddedCallback;
import team.heather.cataclysm.effect.api.callback.MobEffectRemovedCallback;
import team.heather.cataclysm.effect.api.callback.MobEffectUpdatedCallback;
import team.heather.cataclysm.effect.effect.CataclysmicMobEffects;
import team.heather.cataclysm.effect.effect.beneficial.CleansingMobEffect;
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
        if (MortemMobEffect.blocksTotem((LivingEntity) (Object) this, source)) {
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

    @Inject(method = "tickEffects", at = @At("HEAD"))
    private void cataclysm$cleanseEffects(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.level() instanceof ServerLevel level) {
            CleansingMobEffect.cleanseEffects(level, entity);
        }
    }

    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void cataclysm$blockCleansedEffect(MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!entity.level().isClientSide && CleansingMobEffect.blocks(entity, effect)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void cataclysm$allowEffect(MobEffectInstance effect, @Nullable Entity source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!entity.level().isClientSide && !AllowMobEffectCallback.EVENT.invoker().allowEffect(entity, effect, source)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "onEffectAdded", at = @At("TAIL"))
    private void cataclysm$onEffectAdded(MobEffectInstance effect, @Nullable Entity source, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!entity.level().isClientSide) {
            MobEffectAddedCallback.EVENT.invoker().onEffectAdded(entity, effect, source);
        }
    }

    @Inject(method = "onEffectUpdated", at = @At("TAIL"))
    private void cataclysm$onEffectUpdated(MobEffectInstance effect, boolean changed, @Nullable Entity source, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        // Vanilla also calls this every 30 seconds to resync effects that did not change
        if (changed && !entity.level().isClientSide) {
            MobEffectUpdatedCallback.EVENT.invoker().onEffectUpdated(entity, effect, source);
        }
    }

    @Inject(method = "onEffectsRemoved", at = @At("TAIL"))
    private void cataclysm$onEffectsRemoved(Collection<MobEffectInstance> effects, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.level().isClientSide) return;

        for (MobEffectInstance effect : effects) {
            MobEffectRemovedCallback.EVENT.invoker().onEffectRemoved(entity, effect, effect.getDuration() == 0);
        }
    }
}
