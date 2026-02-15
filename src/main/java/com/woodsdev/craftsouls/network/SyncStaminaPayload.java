package com.woodsdev.craftsouls.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncStaminaPayload(int stamina) implements CustomPacketPayload {

    public static final Type<SyncStaminaPayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath("craftsouls", "sync_stamina"));

    public static final StreamCodec<FriendlyByteBuf, SyncStaminaPayload> CODEC =
            StreamCodec.of(
                    (buf, payload) -> buf.writeInt(payload.stamina),
                    buf -> new SyncStaminaPayload(buf.readInt())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleClient(SyncStaminaPayload payload,
                                    IPayloadContext context) {

        context.enqueueWork(() -> {

            Player player = Minecraft.getInstance().player;

            if (player != null) {
                player.getPersistentData()
                        .putInt("craftsouls_stamina", payload.stamina());
            }
        });
    }
}
