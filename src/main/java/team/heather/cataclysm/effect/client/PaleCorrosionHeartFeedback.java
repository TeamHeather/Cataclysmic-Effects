package team.heather.cataclysm.effect.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import team.heather.cataclysm.effect.CataclysmicEffectsMod;
import team.heather.cataclysm.effect.effect.harmful.PaleCorrosionMobEffect;

/**
 * Renders the hearts disabled by Pale Corrosion in the health bar. The half
 * sprites cover the right half of a heart, as hearts are disabled from the end
 * of the bar and the left half of that heart may still be alive.
 */
public final class PaleCorrosionHeartFeedback {

    private static final int DURATION = 20;
    private static final int HEART_SIZE = 9;

    private static final ResourceLocation FULL = CataclysmicEffectsMod.createIdentifier("hud/heart/pale_full");
    private static final ResourceLocation FULL_BLINKING = CataclysmicEffectsMod.createIdentifier("hud/heart/pale_full_blinking");
    private static final ResourceLocation HALF = CataclysmicEffectsMod.createIdentifier("hud/heart/pale_half");
    private static final ResourceLocation HALF_BLINKING = CataclysmicEffectsMod.createIdentifier("hud/heart/pale_half_blinking");

    private static float disabledHealth;
    private static int endTick;

    private PaleCorrosionHeartFeedback() {}

    /**
     * Makes the most recently disabled heart blink when another half is disabled.
     *
     * @param player the player owning the health bar
     */
    public static void update(Player player) {
        float currentDisabledHealth = PaleCorrosionMobEffect.getDisabledHealth(player);

        if (currentDisabledHealth > disabledHealth) {
            endTick = player.tickCount + DURATION;
        }

        disabledHealth = currentDisabledHealth;
    }

    /**
     * Renders the disabled part of a heart, if any.
     *
     * @param graphics the graphics to render with
     * @param player the player owning the health bar
     * @param slot the index of the heart in the health bar
     * @param x the x position of the heart
     * @param y the y position of the heart
     */
    public static void render(GuiGraphics graphics, Player player, int slot, int x, int y) {
        float disabled = PaleCorrosionMobEffect.getDisabledHealth(player);
        if (disabled <= 0.0F) return;

        int firstDisabled = Mth.ceil(player.getMaxHealth());
        int lastDisabled = firstDisabled + Mth.ceil(disabled);
        int left = slot * 2;

        boolean full = left >= firstDisabled && left < lastDisabled;
        boolean half = left + 1 == firstDisabled;
        if (!full && !half) return;

        boolean blinking = slot == firstDisabled / 2 && shouldBlink(player);
        ResourceLocation sprite = full ? (blinking ? FULL_BLINKING : FULL) : (blinking ? HALF_BLINKING : HALF);

        graphics.blitSprite(RenderType::guiTextured, sprite, x, y, HEART_SIZE, HEART_SIZE);
    }

    private static boolean shouldBlink(Player player) {
        int remainingTicks = endTick - player.tickCount;
        return remainingTicks > 0 && remainingTicks / 3 % 2 == 1;
    }
}
