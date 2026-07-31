package org.heather.cataclysmiceffects;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.heather.cataclysmiceffects.effects.DisperMobEffect;
import org.heather.cataclysmiceffects.effects.ImmortalityMobEffect;
import org.heather.cataclysmiceffects.internal.CataclysmicEffectsMod;

public final class CataclysmicMobEffects {

    public static final Holder<MobEffect> IMMORTALITY = register("immunity", new ImmortalityMobEffect(MobEffectCategory.BENEFICIAL, 0xFEFFD1));
    public static final Holder<MobEffect> DISPER = register("disper", new DisperMobEffect(MobEffectCategory.HARMFUL, 0xCE904B));

    private CataclysmicMobEffects() {}

    public static void initialize() {
        CataclysmicEffectsMod.LOGGER.debug("Registered {} effects", 3);
    }

    private static Holder.Reference<MobEffect> register(String path, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, CataclysmicEffectsMod.createIdentifier(path), new SimpleMobEffect(category, color));
    }
}