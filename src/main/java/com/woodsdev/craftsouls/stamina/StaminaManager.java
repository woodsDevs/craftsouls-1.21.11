package com.woodsdev.craftsouls.stamina;

import com.woodsdev.craftsouls.network.SyncStaminaPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class StaminaManager {

    private static final String STAMINA = "craftsouls_stamina";
    private static final String REGEN_DELAY = "craftsouls_regen";

    // get stamina safely (returns 0 if not present)
    public static int get(Player player) {

        Optional<Integer> value =
                player.getPersistentData().getInt(STAMINA);

        return value.orElse(100); // default stamina
    }

    // set stamina
    public static void set(Player player, int value) {

        player.getPersistentData().putInt(STAMINA, value);
    }

    // consume stamina when dodging
    public static void consume(ServerPlayer player, int amount) {

        int stamina = get(player);

        stamina -= amount;
        if (stamina < 0) stamina = 0;

        set(player, stamina);

        // set regen delay (20 ticks = 1 second)
        player.getPersistentData().putInt(REGEN_DELAY, 60);

        sync(player);
    }

    // regen stamina every tick
    public static void tick(ServerPlayer player) {

        int delay = player.getPersistentData()
                .getInt(REGEN_DELAY)
                .orElse(0);

        if (delay > 0) {
            player.getPersistentData()
                    .putInt(REGEN_DELAY, delay - 1);
            return;
        }

        int stamina = get(player);

        if (stamina < 100) {
            stamina++;
            set(player, stamina);
            sync(player);
        }
    }

    private static void sync(ServerPlayer player) {
        player.connection.send(new SyncStaminaPayload(get(player)));
    }
}
