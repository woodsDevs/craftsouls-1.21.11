package com.woodsdev.craftsouls.client;

import com.woodsdev.craftsouls.combat.DodgeHandler;
import com.woodsdev.craftsouls.network.DodgePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class DodgeKeyHandler {

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {

        if (Minecraft.getInstance().player == null) return;

        LocalPlayer player = Minecraft.getInstance().player;

        while (DodgeKeyRegister.dodgeLeft.consumeClick()) {
            System.out.println("dodge left pressed");
            Minecraft.getInstance().getConnection()
                    .send(new DodgePayload(true));
            //dodge left
        }

        while (DodgeKeyRegister.dodgeRight.consumeClick()) {
            System.out.println("dodge right pressed");
            Minecraft.getInstance().getConnection()
                    .send(new DodgePayload(false));
            //dodge right
        }
    }
}
