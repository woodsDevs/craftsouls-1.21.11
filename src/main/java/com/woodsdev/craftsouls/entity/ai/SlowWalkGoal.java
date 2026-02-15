package com.woodsdev.craftsouls.entity.ai;

import com.woodsdev.craftsouls.entity.custom.IronGolemBossEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class SlowWalkGoal extends MeleeAttackGoal {

    private final IronGolemBossEntity boss;

    public SlowWalkGoal(IronGolemBossEntity boss) {
        super(boss, 0.8D, false);
        this.boss = boss;
    }

    @Override
    public boolean canUse() {
        return !boss.isBusy() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return !boss.isBusy() && super.canContinueToUse();
    }

}

