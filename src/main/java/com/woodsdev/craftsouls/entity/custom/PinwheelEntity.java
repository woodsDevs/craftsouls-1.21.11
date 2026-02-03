package com.woodsdev.craftsouls.entity.custom;

import com.sun.jna.platform.win32.WinDef;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class PinwheelEntity extends Monster implements Enemy {

    private static final EntityDataAccessor<Boolean> DATA_ATTACKING =
            SynchedEntityData.defineId(PinwheelEntity.class, EntityDataSerializers.BOOLEAN);
    // true while charging/attacking
    public boolean idleStarted = false;

    private int idleAnimationTimeout = 0;
    private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING =
            SynchedEntityData.defineId(PinwheelEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> IDLE =
            SynchedEntityData.defineId(PinwheelEntity.class, EntityDataSerializers.BOOLEAN);
    // We inherit this constructor without the bound on the generic wildcard.
    // The bound is needed for registration below, so we add it here.
    public PinwheelEntity(EntityType<? extends PinwheelEntity> entityType, Level level) {
        super(entityType, level);
    }

    protected void registerGoals(){
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this,Player.class,60));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class,true,true));
        this.goalSelector.addGoal(6, new FireballGoal(this));
        this.goalSelector.addGoal(8, new LeapAtTargetGoal(this,0.5f));
    }

    public void addConstantEffect(LivingEntity entity){
        entity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,999999,5,false,false));
        entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,999999,5,false,false));
    }

    public boolean isCharging() {
        return (Boolean)this.entityData.get(DATA_IS_CHARGING);
    }

    public void setCharging(boolean charging) {
        this.entityData.set(DATA_IS_CHARGING, charging);
    }

    public boolean isAttacking() {
        return this.entityData.get(DATA_ATTACKING);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.FOLLOW_RANGE, 16.0D);
    }
    // See the Data and Networking article for information about these methods.
    @Override
    protected void readAdditionalSaveData(ValueInput input) {}

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {}

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder); // required
        builder.define(IDLE, false);
        builder.define(DATA_IS_CHARGING, false);
        builder.define(DATA_ATTACKING, false);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float amount) {
        return super.hurtServer(level, damageSource, amount);
    }




    @Override
    public void tick() {
        super.tick();


        if (!this.level().isClientSide()) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        }
    }

    static class FireballGoal extends Goal {
        private final PinwheelEntity pinwheel;
        public int chargeTime;
        //constructor
        public FireballGoal(PinwheelEntity pinwheel) {this.pinwheel = pinwheel;}

        //can use is true if pinwheel's target isnt null
        public boolean canUse() { return this.pinwheel.getTarget() != null; }

        public void start() {this.chargeTime = 0;}

        public void stop() {this.pinwheel.setTarget(null);}


        public boolean reguiresUpdateEveryTick() {return true;}

        public void tick() {
            LivingEntity livingentity = this.pinwheel.getTarget();
            if (livingentity != null) {
                double d0 = (double)64.0F;
                if (livingentity.distanceToSqr(this.pinwheel) < (double)4096.0F && this.pinwheel.hasLineOfSight(livingentity)) {
                    Level level = this.pinwheel.level();
                    ++this.chargeTime;
                    if (this.chargeTime == 10 && !this.pinwheel.isSilent()) {
                        level.levelEvent((Entity)null, 1037, this.pinwheel.blockPosition(), 0);
                    }
                    if (this.chargeTime == 12) {
                        this.pinwheel.setCharging(true);
                        this.pinwheel.entityData.set(DATA_ATTACKING, true);
                        System.out.println("ATTACK=TRUE");
                    }
                    if (this.chargeTime == 20) {
                        Vec3 look = this.pinwheel.getLookAngle();
                        Vec3 spawnPos = new Vec3(this.pinwheel.getX(), this.pinwheel.getEyeY(), this.pinwheel.getZ());
                        if (!this.pinwheel.isSilent()) {
                            level.levelEvent((Entity)null, 1016, this.pinwheel.blockPosition(), 0);
                        }

                        LargeFireball largefireball = new LargeFireball(level, this.pinwheel, look.normalize(), 1);
                        largefireball.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                        level.addFreshEntity(largefireball);
                        this.chargeTime = -320;
                        this.pinwheel.entityData.set(DATA_ATTACKING, false);
                        System.out.println("ATTACK=FALSE");
                    }
                    if (this.chargeTime == 40) {
                        this.pinwheel.setCharging(false);
                        if (!this.pinwheel.idleStarted) {
                            this.pinwheel.idleStarted = true;
                        }
                    }

                } else if (this.chargeTime > 0) {
                    --this.chargeTime;
                }

                this.pinwheel.setCharging(this.chargeTime > 10);
            }

        }
        }
        private final ServerBossEvent bossEvent =
            new ServerBossEvent( Component.translatable("entity.craftsouls.pinwheel"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);

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
