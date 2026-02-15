package com.woodsdev.craftsouls.entity.ai;

import com.woodsdev.craftsouls.entity.custom.IronGolemBossEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class CloseRangeAttackGoal extends Goal {

    private final IronGolemBossEntity boss;

    public CloseRangeAttackGoal(IronGolemBossEntity boss) {
        this.boss = boss;
    }

    @Override
    public boolean canUse() {
        if (boss.isBusy()) return false;

        Player target = boss.level().getNearestPlayer(boss, 3.0D);
        if (target == null) return false;

        return boss.distanceTo(target) <= 3.0D;
    }

    @Override
    public void start() {
        Player target = boss.level().getNearestPlayer(boss, 3.0D);
        if (target == null) return;

        boss.getNavigation().stop();

        // RANDOM
        if (boss.getRandom().nextBoolean()) {
            if (boss.getGrabCooldown() <= 0) {
                boss.startGrab(target);
            } else {
                boss.startStomp();
            }
        } else {
            boss.startStomp();
        }
    }

    @Override
    public boolean canContinueToUse() {
        return boss.isGrabbing() || boss.isStomping();
    }
}

