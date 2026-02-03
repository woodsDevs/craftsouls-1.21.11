package com.woodsdev.craftsouls.entity.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class PinwheelEntityRenderState extends LivingEntityRenderState {
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public boolean wasCharging = false;
    public boolean attacking = false;
    public boolean wasAttacking;
    private boolean charging;

    public boolean isCharging() {
        return charging;
    }

    public void setCharging(boolean charging) {
        this.charging = charging;
    }
    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.wasAttacking = this.attacking;
        this.attacking = attacking;

    }
}
