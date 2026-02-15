package com.woodsdev.craftsouls.network;

import com.woodsdev.craftsouls.craftsouls;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public class CraftSoulsNetwork {

    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {

        final PayloadRegistrar registrar =
                event.registrar(craftsouls.MODID)
                        .versioned("1.0");

        registrar.playToServer(
                DodgePayload.TYPE,
                DodgePayload.CODEC,
                DodgePayload::handleServer
        );
        registrar.playToClient(
                SyncStaminaPayload.TYPE,
                SyncStaminaPayload.CODEC,
                SyncStaminaPayload::handleClient
        );
    }
}
