package team.heather.cataclysm.effect.effect;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import team.heather.cataclysm.effect.CataclysmicEffectsMod;
import team.heather.cataclysm.effect.effect.beneficial.CleansingMobEffect;
import team.heather.cataclysm.effect.effect.beneficial.ImmortalityMobEffect;
import team.heather.cataclysm.effect.effect.harmful.DisperMobEffect;
import team.heather.cataclysm.effect.effect.harmful.MortemMobEffect;
import team.heather.cataclysm.effect.effect.harmful.PaleCorrosionMobEffect;
import team.heather.cataclysm.effect.effect.harmful.VulnerabilityMobEffect;

public final class CataclysmicMobEffects {

    public static final Holder<MobEffect> IMMORTALITY = register("immortality", new ImmortalityMobEffect(MobEffectCategory.BENEFICIAL, 0xFEFFD1));
    public static final Holder<MobEffect> CLEANSING = register("cleansing", new CleansingMobEffect(MobEffectCategory.BENEFICIAL, 0xF4F1E6));
    public static final Holder<MobEffect> DISPER = register("disper", new DisperMobEffect(MobEffectCategory.HARMFUL, 0xCE904B));
    public static final Holder<MobEffect> VULNERABILITY = register("vulnerability", new VulnerabilityMobEffect(MobEffectCategory.HARMFUL, 0x9CC4D6));
    public static final Holder<MobEffect> MORTEM = register("mortem", new MortemMobEffect(MobEffectCategory.HARMFUL, 0xC79A3E));
    public static final Holder<MobEffect> PALE_CORROSION = register("pale_corrosion", new PaleCorrosionMobEffect(MobEffectCategory.HARMFUL, 0xD6D3C4));

    private CataclysmicMobEffects() {}

    public static void initialize() {
        CataclysmicEffectsMod.LOGGER.debug("Registered {} effects", 6);
    }

    private static Holder.Reference<MobEffect> register(String path, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, CataclysmicEffectsMod.createIdentifier(path), effect);
    }
}
