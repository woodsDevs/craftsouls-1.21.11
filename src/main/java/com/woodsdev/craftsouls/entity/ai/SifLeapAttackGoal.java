package com.woodsdev.craftsouls.entity.ai;

import com.woodsdev.craftsouls.entity.custom.SifEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SifLeapAttackGoal extends Goal {
    private final SifEntity sif;
    private LivingEntity target;

    private int leapCooldown = 0;
    private boolean hasLeaped = false;

    public SifLeapAttackGoal(SifEntity sif) {
        this.sif = sif;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }
    @Override
    public boolean canUse() {
        if (leapCooldown > 0) {
            leapCooldown--;
            return false;
        }

        target = sif.getTarget();
        if (target == null) return false;

        double dist = sif.distanceTo(target);

        // Only leap if mid-range
        return dist > 6.0 && dist < 18.0 && sif.onGround();
    }

    @Override
    public boolean canContinueToUse() {
        if (target == null) return false;

        // Cancel if target too far
        return sif.distanceTo(target) < 25.0 && !sif.onGround();
    }

    @Override
    public void start() {
        hasLeaped = false;
    }

    @Override
    public void tick() {
        if (target == null) return;

        sif.getLookControl().setLookAt(target, 30F, 30F);

        if (!hasLeaped) {
            Vec3 direction = new Vec3(
                    target.getX() - sif.getX(),
                    0,
                    target.getZ() - sif.getZ()
            ).normalize();

            // upward leap, chagne here
            sif.setDeltaMovement(
                    direction.x ,
                    0.6,   // vertical power
                    direction.z
            );
            sif.level().playSound(
                    null,
                    sif.blockPosition(),
                    SoundEvents.ENDER_DRAGON_FLAP,
                    sif.getSoundSource(),
                    2.0F,
                    0.8F
            );
            hasLeaped = true;
        }

        // When landing deal damage
        if (hasLeaped && sif.onGround()) {
            dealLandingDamage();
            leapCooldown = 80; // 4 seconds cooldown
            stop();
            //land particles
            if (sif.level() instanceof ServerLevel server) {
                server.sendParticles(
                        ParticleTypes.CLOUD,
                        sif.getX(),
                        sif.getY() + 1,
                        sif.getZ(),
                        50,
                        0.3,
                        0.3,
                        0.3,
                        0.02
                );
            }
        }

    }

    private void dealLandingDamage() {

        for (LivingEntity entity : sif.level().getEntitiesOfClass(
                LivingEntity.class,
                sif.getBoundingBox().inflate(4.0),
                e -> e != sif)) {

            entity.hurt(sif.damageSources().mobAttack(sif),4.0F);
        }

    }

    @Override
    public void stop() {
        hasLeaped = false;
    }
}
