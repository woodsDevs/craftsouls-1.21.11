package com.woodsdev.craftsouls.client;

import com.woodsdev.craftsouls.combat.DodgeHandler;
import com.woodsdev.craftsouls.network.DodgePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

public class DodgeKeyHandler {
    public boolean dodgeToggled = false;

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        DodgeAnimationHandler.tick(); // add this static method
        Player player = Minecraft.getInstance().player;
        if (Minecraft.getInstance().player == null) return;
        //if dodge toggle is true, change boolean
        if (DodgeKeyRegister.toggleDodge.consumeClick()){
            dodgeToggled = !dodgeToggled;
            player.displayClientMessage(Component.literal("Dodge is: " + (!dodgeToggled ? "ON" : "OFF")), true);
        }
        LocalPlayer player2 = Minecraft.getInstance().player;
        //if dodge isnt toggled , continue
        if (!dodgeToggled) {
            while (DodgeKeyRegister.dodgeLeft.consumeClick()) {
                System.out.println("dodge left pressed");
                Minecraft.getInstance().getConnection().send(new DodgePayload(true));
                //dodge left

            }

            while (DodgeKeyRegister.dodgeRight.consumeClick()) {
                System.out.println("dodge right pressed");
                Minecraft.getInstance().getConnection().send(new DodgePayload(false));
                //dodge right

            }
        }
    }
}
