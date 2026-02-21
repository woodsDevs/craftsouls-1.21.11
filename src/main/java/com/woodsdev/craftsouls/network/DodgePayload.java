package com.woodsdev.craftsouls.network;

import com.woodsdev.craftsouls.client.DodgeAnimationHandler;
import com.woodsdev.craftsouls.combat.DodgeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import software.bernie.geckolib.util.GeckoLibUtil;

public record DodgePayload(boolean left) implements CustomPacketPayload {

    public static final Type<DodgePayload> TYPE =
            new Type<>(Identifier.fromNamespaceAndPath("craftsouls","dodge"));

    public static final StreamCodec<FriendlyByteBuf, DodgePayload> CODEC =
            StreamCodec.of(
                    (buf, payload) -> buf.writeBoolean(payload.left()),
                    buf -> new DodgePayload(buf.readBoolean())
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    // server side handling
    public static void handleServer(DodgePayload payload, IPayloadContext context) {

        context.enqueueWork(() -> {

            if (!(context.player() instanceof ServerPlayer player)) return;


            DodgeHandler.performDodge(player, payload.left());


        });
    }

}
