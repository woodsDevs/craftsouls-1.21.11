package com.woodsdev.craftsouls.entity.ai;

import com.woodsdev.craftsouls.entity.custom.IronGolemBossEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class RangedAttackGoal extends Goal {

    private final IronGolemBossEntity boss;

    public RangedAttackGoal(IronGolemBossEntity boss) {
        this.boss = boss;
    }

    @Override
    public boolean canUse() {

        if (boss.isBusy()) return false;

        Player target = boss.level().getNearestPlayer(boss,25.0D);
        if (target == null) return false;

        double distance = boss.distanceTo(target);


        return distance >= 20.0D;

    }

    @Override
    public void start() {
        Player target = boss.level().getNearestPlayer(boss,25.0D);
        if (target != null) {
            boss.getNavigation().stop();
            boss.startProjectileAttack(target);
        }
    }
}

