package com.woodsdev.craftsouls.entity.custom;

import com.woodsdev.craftsouls.entity.ai.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

public class IronGolemBossEntity extends Monster implements Enemy {
    //Grab attack settings
    private static final double GRAB_RANGE = 3.0D; //how far player can be grabbed from
    private static final int GRAB_HOLD_TICKS = 35; //how long player is held
    private static final int GRAB_COOLDOWN = 180; //how long till grab again
    private static final double horizontalStrength = 3.0D; //throw length
    private static final double verticalStrength = 1.0D;  //throw height
    private boolean hasTeleported = false;

    //stomp settings
    private static final int STOMP_DURATION = 60; //4 seconds(60 ticks)
    private static final float STOMP_DAMAGE = 5.0F;
    private static final double STOMP_RANGE = 4.0D;
    //axe attack
    private boolean axeAttacking = false;
    private int axeTick = 0;
    private static final int AXE_DURATION = 40;
    private static final float AXE_DAMAGE = 10f;
    //ranmged cooldown
    private int rangedCooldown = 0;
    private static final int RANGED_COOLDOWN_MAX = 100;

    //RUNTIME STATE
    private int grabTick = 0;
    private int grabCooldown = 0;
    private Player grabbedPlayer = null;
    private boolean grabbing = false;
    private boolean stomping = false;
    private int stompTick = 0;
    private boolean projectileAttacking = false;
    private int projectileTick = 0;
    private static final int PROJECTILE_DURATION = 30;
    private Player projectileTarget;
    public float walkProgress = 0.0f; // increases while moving
    public boolean isMoving = false;   // whether the boss is walking


    // data ACCESSORS
    public static final EntityDataAccessor<Boolean> GRABBING =
            SynchedEntityData.defineId(IronGolemBossEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> STOMPING =
            SynchedEntityData.defineId(IronGolemBossEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> AXE_ATTACK =
            SynchedEntityData.defineId(IronGolemBossEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> PROJECTILE_ATTACK =
            SynchedEntityData.defineId(IronGolemBossEntity.class, EntityDataSerializers.BOOLEAN);





    public IronGolemBossEntity(EntityType<? extends IronGolemBossEntity> entityType, Level level) {super(entityType, level);}

    protected void registerGoals(){
        //custom goals
        this.goalSelector.addGoal(0, new CloseRangeAttackGoal(this));
        this.goalSelector.addGoal(2, new AxeAttackGoal(this));
        this.goalSelector.addGoal(3, new RangedAttackGoal(this));
        //mc goals
        this.goalSelector.addGoal(4, new SlowWalkGoal(this)); // slow pressure walk
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 60));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 100.0D).add(Attributes.MOVEMENT_SPEED, 0.1D).add(Attributes.FOLLOW_RANGE, 30.0D).add(Attributes.ATTACK_DAMAGE,10.0D);
    }

    // See the Data and Networking article for information about these methods.
    @Override
    protected void readAdditionalSaveData(ValueInput input) {}

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {}

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GRABBING, false);
        builder.define(STOMPING, false);
        builder.define(AXE_ATTACK, false);
        builder.define(PROJECTILE_ATTACK,false);
    }

    //invunerable fix
    @Override
    public boolean isInvulnerable() {
        return false;
    }


    //start the grab method
    public void startGrab(Player player) {
        this.grabbing = true;
        this.grabbedPlayer = player;
        this.grabCooldown = GRAB_COOLDOWN;
        this.grabTick = 0;
        this.entityData.set(GRABBING, true); //start anim
    }
    //start stomp method
    public void startStomp() {
        this.stomping = true;
        this.stompTick = 0;
        this.entityData.set(STOMPING, true);
    }
    //start axe attack
    public void startAxeAttack() {
        axeAttacking = true;
        axeTick = 0;
        this.entityData.set(AXE_ATTACK, true);
    }
    public void startProjectileAttack(Player target) {
        projectileAttacking = true;
        projectileTick = 0;
        projectileTarget = target;
        this.entityData.set(PROJECTILE_ATTACK, true);
    }



    //grab getters and setters
    public boolean isGrabbing(){
        return grabbing;
    }
    public int getGrabCooldown(){
        return grabCooldown;
    }
    public int getGrabTick() {
        return grabTick;
    }
    //stomp getters setters
    public boolean isStomping() {
        return stomping;
    }
    //ace getter
    public boolean isAxeAttacking() {return axeAttacking;}
    //busy
    public boolean isBusy() {
        return grabbing || stomping || axeAttacking || projectileAttacking;
    }

    public int getRangedCooldown() {
        return rangedCooldown;
    }


    @Override
    public void tick() {
        super.tick();


        if (!this.level().isClientSide()) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

            //cooldown countdown
            if(grabCooldown > 0){
                grabCooldown--;
            }
            if (rangedCooldown > 0) {
                rangedCooldown--;
            }


            //grab logic
            if (grabbing && grabbedPlayer!=null){
                grabTick++;
                level().playSound(null, blockPosition(), SoundEvents.COPPER_CHEST_WEATHERED_CLOSE, SoundSource.HOSTILE, 1.5F, 0.5F);

                // Teleport once at animation pickup frame
                if (grabTick == 20 && !hasTeleported) {
                    hasTeleported = true;
                }

                //keep player til throw
                if (hasTeleported) {

                    // locate hand
                    double x = this.getX() -2.5D;
                    double y = this.getY() + 7.0D;
                    double z = this.getZ();

                    grabbedPlayer.teleportTo(x, y, z);
                    grabbedPlayer.setDeltaMovement(0, 0, 0);
                    grabbedPlayer.fallDistance = 0;
                }
                if (grabTick >= GRAB_HOLD_TICKS) {
                    throwPlayer();
                    level().playSound(null, blockPosition(), SoundEvents.COPPER_CHEST_WEATHERED_OPEN, SoundSource.HOSTILE, 1.5F, 0.5F);
                    level().playSound(null, blockPosition(), SoundEvents.ALLAY_THROW, SoundSource.HOSTILE, 1.5F, 0.5F);
                }
            }
            //stomp logic
            if (stomping) {
                stompTick++;

                //damage + stagger at impact
                if (stompTick == 20) {
                    performStomp();
                    level().playSound(null, blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.HOSTILE, 1.5F, 0.5F);

                    level().playSound(null,blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 1.0F,0.5F);
                }

                if (stompTick >= STOMP_DURATION) {
                    stopStomp();
                }
            }
            //axe logic
            if (axeAttacking) {
                axeTick++;

                if (axeTick == 20) { // impact frame
                    performAxeHit();
                    spawnAxeDust();
                    level().playSound(null, blockPosition(), SoundEvents.ANVIL_HIT, SoundSource.HOSTILE, 1.5F, 0.5F);
                    level().playSound(null, blockPosition(), SoundEvents.COPPER_CHEST_OXIDIZED_CLOSE, SoundSource.HOSTILE, 1.5F, 0.5F);
                    level().playSound(null, blockPosition(), SoundEvents.COPPER_CHEST_OXIDIZED_OPEN, SoundSource.HOSTILE, 1.5F, 0.5F);
                }

                if (axeTick >= AXE_DURATION) {
                    stopAxeAttack();
                }
            }
            if (projectileAttacking) {
                projectileTick++;

                if (projectileTick == 15) { // fire on animation frame
                    shootProjectile(projectileTarget);
                    level().playSound(null, blockPosition(), SoundEvents.AXE_STRIP, SoundSource.HOSTILE, 1.5F, 0.5F);
                }

                if (projectileTick >= PROJECTILE_DURATION) {
                    projectileAttacking = false;
                    projectileTick = 0;
                    projectileTarget = null;
                    this.entityData.set(PROJECTILE_ATTACK, false);
                }

            }

        }
    }

    //throw player logic
    private void throwPlayer() {
        if (grabbedPlayer != null) {

            // thrown whereverf boss faces
            float yawRad = (float) Math.toRadians(this.getYRot());

            double dx = -Math.sin(yawRad);
            double dz =  Math.cos(yawRad);

            grabbedPlayer.setDeltaMovement(
                    dx * horizontalStrength,
                    verticalStrength,
                    dz * horizontalStrength
            );

            grabbedPlayer.hurtMarked = true;

        }

        grabbedPlayer = null;
        grabbing = false;
        this.entityData.set(GRABBING, false); //turn off anim
        hasTeleported = false;

    }

    //perform stomp logic
    private void performStomp() {
        for(Player player : this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(STOMP_RANGE))) {
            //damage
            player.hurt(damageSources().mobAttack(this), STOMP_DAMAGE);

            //Knock upward slightly
            player.setDeltaMovement(player.getDeltaMovement().x, 0.5D, player.getDeltaMovement().z);
            //stagger
            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                    MobEffects.SLOWNESS,
                    90,  // 3 seconds (adjustable)
                    10,  // high amplifier = almost frozen
                    false,
                    false
            ));
        }
    }

    //stop stomp
    private void stopStomp() {
        stomping = false;
        stompTick = 0;
        this.entityData.set(STOMPING, false);
    }

    //axe
    private void performAxeHit() {
        for (Player player : this.level().getEntitiesOfClass(
                Player.class,
                this.getBoundingBox().inflate(4.0D))) {

            player.hurt(damageSources().mobAttack(this), AXE_DAMAGE);
        }
    }
    private void stopAxeAttack() {
        axeAttacking = false;
        axeTick = 0;
        this.entityData.set(AXE_ATTACK, false);
    }
    //axe dust XD
    private void spawnAxeDust() {
        if (level().isClientSide()) return;
        ServerLevel serverLevel = (ServerLevel) level();

        double radius = 3.0D;

        for (int i = 0; i < 30; i++) {
            double angle = random.nextDouble() * Math.PI * 2;
            double offsetX = Math.cos(angle) * radius;
            double offsetZ = Math.sin(angle) * radius;

            serverLevel.sendParticles(
                    ParticleTypes.CLOUD,
                    this.getX() + offsetX,
                    this.getY(),
                    this.getZ() + offsetZ,
                    20,     // count
                    2.0,    // x spread
                    0.4,    // y spread
                    2.0,    // z spread
                    0.01   // speed
            );        }
    }



    public void shootProjectile(Player target) {

        if (target == null) return;
        double dx = target.getX() - this.getX();
        double dy = target.getY(0.5D) - (this.getY() + 3.0D);
        double dz = target.getZ() - this.getZ();

        Vec3 direction = new Vec3(
                target.getX() - this.getX(),
                target.getY(0.5D) - (this.getY() + 3.0D),
                target.getZ() - this.getZ()
        ).normalize();

        WitherSkull skull = new WitherSkull(
                EntityType.WITHER_SKULL,
                this.level()
        );

        skull.setOwner(this);

        skull.setPos(this.getX(), this.getY() + 3.0D, this.getZ());

        skull.shoot(dx, dy, dz, 0.5F, 0.5F);

        skull.setDangerous(false); // smaller explosion

        this.level().addFreshEntity(skull);

        rangedCooldown = 100; // 5 second cooldown
    }



    private final ServerBossEvent bossEvent =
            new ServerBossEvent( Component.translatable("entity.craftsouls.irongiant"), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);

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
