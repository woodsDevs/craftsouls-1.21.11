package com.woodsdev.craftsouls.entity.custom;

import com.woodsdev.craftsouls.entity.ai.SifLeapAttackGoal;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SifEntity extends Monster implements Enemy {
    // We inherit this constructor without the bound on the generic wildcard.
    // The bound is needed for registration below, so we add it here.
    private static final int ATTACK_LENGTH = 40;
    private static final int DAMAGE_FRAME = 15;

    public SifEntity(EntityType<? extends SifEntity> entityType, Level level) {
        super(entityType, level);
        this.setPersistenceRequired();
    }

    public void registerGoals(){
        this.goalSelector.addGoal(1, new SifLeapAttackGoal(this));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 1.0D, 25.0F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class,8.0F));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    private static final EntityDataAccessor<Boolean> ATTACKING =
            SynchedEntityData.defineId(SifEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> WALKING =
            SynchedEntityData.defineId(SifEntity.class, EntityDataSerializers.BOOLEAN);


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACKING, false);
        builder.define(WALKING, false);

    }
    //fix spawn
    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }
    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public boolean getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public boolean isWalking() {
        return this.entityData.get(WALKING);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.ATTACK_DAMAGE,100.0D);
    }
    private int attackCooldown = 0;

    public boolean isAttacking() {
        return attackCooldown > 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getTarget() == null) {
            this.getNavigation().stop();
        }

        if (!this.level().isClientSide()) {
            Player player = this.level().getNearestPlayer(this, 16);
            if (player != null) {
                this.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());

                double dist = this.distanceTo(player);

                if (dist < 4.0 && attackCooldown == 0) {
                    attackCooldown = ATTACK_LENGTH;
                }
            }

            if (attackCooldown > 0) {
                attackCooldown--;

                // HIT FRAME
                if (attackCooldown == ATTACK_LENGTH - DAMAGE_FRAME) {
                    doAttackHit();
                    level().playSound(
                            null,
                            this.blockPosition(),
                            SoundEvents.ROOTS_HIT,
                            this.getSoundSource(),
                            1.0F,
                            0.8F
                    );
                }
            }

            this.setAttacking(attackCooldown > 0);

            boolean moving = !this.getNavigation().isDone();
            this.entityData.set(WALKING, moving && attackCooldown == 0);

        }

        if (!this.level().isClientSide()) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    private void doAttackHit() {
        for (Player player : this.level().getEntitiesOfClass(
                Player.class,
                this.getBoundingBox().inflate(5.0))) {

            player.hurt(damageSources().mobAttack(this), 10.0F);
        }
    }

    private final ServerBossEvent bossEvent =
            new ServerBossEvent( Component.translatable("entity.craftsouls.sif"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
        this.bossEvent.removeAllPlayers();
    }

}
