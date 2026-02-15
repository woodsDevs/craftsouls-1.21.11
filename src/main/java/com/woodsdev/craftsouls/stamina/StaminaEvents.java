package com.woodsdev.craftsouls.stamina;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class StaminaEvents {

    // initialize stamina when player joins
    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {

        if (event.getEntity() instanceof ServerPlayer player) {

            if (player.getPersistentData()
                    .getInt("craftsouls_stamina")
                    .isEmpty()) {

                StaminaManager.set(player, 100);
            }
        }
    }

    // regen tick
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {

        if (event.getEntity() instanceof ServerPlayer player) {

            StaminaManager.tick(player);
        }
    }
}
