package org.heather.cataclysmiceffects.client;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public final class ImmortalityHeartFeedback {

    private static final int DURATION = 20;

    private static UUID playerId;
    private static float damage;
    private static int endTick;

    private ImmortalityHeartFeedback() {}

    public static void show(Player player, float damage) {
        if (damage <= 0.0F) return;

        playerId = player.getUUID();
        ImmortalityHeartFeedback.damage = damage;
        endTick = player.tickCount + DURATION;
    }

    public static boolean shouldBlink(Player player) {
        if (!isActive(player)) return false;

        int remainingTicks = endTick - player.tickCount;
        return remainingTicks / 3 % 2 == 1;
    }

    public static int getSimulatedHealth(Player player) {
        return Mth.ceil(Math.max(player.getHealth() - damage, 0.0F));
    }

    private static boolean isActive(Player player) {
        if (!player.getUUID().equals(playerId) || player.tickCount >= endTick) {
            playerId = null;
            damage = 0.0F;
            return false;
        }

        return true;
    }
}