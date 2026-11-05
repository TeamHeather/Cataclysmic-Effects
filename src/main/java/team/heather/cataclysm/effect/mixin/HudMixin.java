package team.heather.cataclysm.effect.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.heather.cataclysm.effect.client.PaleCorrosionHeartFeedback;
import team.heather.cataclysm.effect.effect.harmful.PaleCorrosionMobEffect;

@Mixin(Gui.class)
public abstract class HudMixin {

    /** Keeps the slots of the hearts disabled by Pale Corrosion in the health bar layout. */
    @ModifyVariable(method = "renderPlayerHealth", at = @At("STORE"), ordinal = 0)
    private float cataclysm$reservePaleHearts(float maxHealth, @Local Player player) {
        PaleCorrosionHeartFeedback.update(player);

        float fullMaxHealth = (float) player.getAttributeValue(Attributes.MAX_HEALTH) + PaleCorrosionMobEffect.getDisabledHealth(player);
        return Math.max(maxHealth, fullMaxHealth);
    }

    @Inject(
            method = "renderHearts",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private void cataclysm$renderPaleHeart(
            GuiGraphics graphics, Player player, int x, int y, int rowHeight, int regenerationSlot, float maxHealth,
            int health, int displayHealth, int absorption, boolean blinking, CallbackInfo ci,
            @Local(ordinal = 10) int slot, @Local(ordinal = 13) int heartX, @Local(ordinal = 14) int heartY
    ) {
        PaleCorrosionHeartFeedback.render(graphics, player, slot, heartX, heartY);
    }
}
