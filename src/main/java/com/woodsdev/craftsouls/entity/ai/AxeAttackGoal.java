package com.woodsdev.craftsouls.entity.ai;

import com.woodsdev.craftsouls.entity.custom.IronGolemBossEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class AxeAttackGoal extends Goal {

    private final IronGolemBossEntity boss;

    public AxeAttackGoal(IronGolemBossEntity boss) {
        this.boss = boss;
    }

    @Override
    public boolean canUse() {

        if (boss.isBusy()) return false;

        Player target = boss.level().getNearestPlayer(boss,6.0D);
        if (target == null) return false;
        double distance = boss.distanceTo(target);

        return distance >= 5.0D && distance <= 10.0D;
    }

    @Override
    public void start() {
        boss.getNavigation().stop();
        boss.startAxeAttack();
    }

    @Override
    public boolean canContinueToUse() {
        return boss.isAxeAttacking();
    }
}

