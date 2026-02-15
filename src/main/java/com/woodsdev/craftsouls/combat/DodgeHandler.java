package com.woodsdev.craftsouls.combat;

import com.woodsdev.craftsouls.stamina.StaminaManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class DodgeHandler {
    private static final String DODGE_TAG = "craftsouls_dodge_cd";
    private static final int COOLDOWN_TICKS = 30; // 1.5 seconds


    public static void performDodge(ServerPlayer player, boolean left) {

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
    }

}
