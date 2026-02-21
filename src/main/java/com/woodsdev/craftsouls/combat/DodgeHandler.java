package com.woodsdev.craftsouls.combat;

import com.woodsdev.craftsouls.client.DodgeAnimationHandler;
import com.woodsdev.craftsouls.stamina.StaminaManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DodgeHandler {
    private static final String DODGE_TAG = "craftsouls_dodge_cd";
    private static final int COOLDOWN_TICKS = 30; // 1.5 seconds

    private static final Set<UUID> DODGING = new HashSet<>();

    public static void performDodge(ServerPlayer player, boolean left) {
        DODGING.add(player.getUUID());

        player.level().scheduleTick(player.blockPosition(), player.level().getBlockState(player.blockPosition()).getBlock(), 8);

        int cost = 25;

        long now = player.level().getGameTime();
        long last = player.getPersistentData().getLong(DODGE_TAG).orElse(0L); // POTENTIAL BUG, FIX SOON

        // cooldown check
        if (now - last < COOLDOWN_TICKS) return;

        if (StaminaManager.get(player) < cost) return;

        // set cooldown
        player.getPersistentData().putLong(DODGE_TAG, now);

        StaminaManager.consume(player, cost);

        // calculate right vector
        Vec3 look = player.getLookAngle();
        Vec3 right = new Vec3(-look.z, 0, look.x).normalize();

        Vec3 dash = left ? right.scale(-1.2) : right.scale(1.2);

        player.setDeltaMovement(
                player.getDeltaMovement().add(dash.x, 0.25, dash.z)
        );

        player.hurtMarked = true;

        // give i frames
        player.invulnerableTime = 15;
        // Trigger animation
        DodgeAnimationHandler.startDodge();
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {

        if (event.getEntity() instanceof ServerPlayer player) {
            DODGING.remove(player.getUUID());
        }
    }
    //helper to get dodge
    public static boolean isDodging(Player player) {
        return DODGING.contains(player.getUUID());
    }
}
