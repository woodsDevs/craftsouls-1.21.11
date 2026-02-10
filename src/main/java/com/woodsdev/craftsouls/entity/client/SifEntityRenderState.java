package com.woodsdev.craftsouls.entity.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class SifEntityRenderState extends LivingEntityRenderState {
    public boolean attacking = false;


    public boolean wasAttacking = false;
    public boolean walking = false;

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.wasAttacking = this.attacking;
        this.attacking = attacking;

    }
}
