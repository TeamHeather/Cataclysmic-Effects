package org.heather.cataclysmiceffects.payload;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.heather.cataclysmiceffects.CataclysmicEffectsMod;
import org.heather.cataclysmiceffects.client.ImmortalityHeartFeedback;
import org.jspecify.annotations.NonNull;

public record SuppressedDamagePayload(float damage) implements CustomPacketPayload {

    public static final Type<SuppressedDamagePayload> PAYLOAD_TYPE = new Type<>(CataclysmicEffectsMod.createIdentifier("suppressed_damage"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SuppressedDamagePayload> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, SuppressedDamagePayload::damage, SuppressedDamagePayload::new);

    public static void initialize() {
        PayloadTypeRegistry.clientboundPlay().register(PAYLOAD_TYPE, STREAM_CODEC);
    }

    @Environment(EnvType.CLIENT)
    public static void initializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(PAYLOAD_TYPE, (payload, context) ->
                context.client().execute(() -> {
                    LocalPlayer player = context.client().player;
                    if (player != null) {
                        ImmortalityHeartFeedback.show(player, payload.damage());
                    }
                })
        );
    }

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return PAYLOAD_TYPE;
    }
}