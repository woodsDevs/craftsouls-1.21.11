package com.woodsdev.craftsouls.client;

import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

@EventBusSubscriber(modid = "craftsouls", value = Dist.CLIENT)
public class DodgeAnimationHandler {

    public static int dodgeTick = -1;
    public static final int DODGE_DURATION = 18;

    private static float directionSign = 1f;

    public static void startDodge(boolean dodgeRight) {
        directionSign = dodgeRight ? 1f : -1f;
        dodgeTick = 0;
        Player player = Minecraft.getInstance().player;
        player.level().playSound(player,player, SoundEvents.WIND_CHARGE_THROW,SoundSource.PLAYERS,0.5f,1f);
    }
    //pre
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (dodgeTick < 0) return;
        if (Minecraft.getInstance().player == null) return;
        Player player = Minecraft.getInstance().player;

        player.level().addParticle(
                    ParticleTypes.CLOUD,
                    player.getX() , player.getY(), player.getZ(),
                    0.02, 0.0, 0.02
            );

        HumanoidModel<?> model =
                (HumanoidModel<?>) ((LivingEntityRenderer) event.getRenderer()).getModel();

        float t = Mth.clamp((dodgeTick + event.getPartialTick()) / DODGE_DURATION, 0f, 1f);

        float windup   = 0f;
        float launch   = 0f;
        float recovery = 0f;

        if (t < 0.18f) {
            windup = easeInCubic(t / 0.10f);
        } else if (t < 0.78f) {
            launch = easeInOutQuad((t - 0.10f) / 0.60f);
        } else {
            recovery = easeOutQuad((t - 0.78f) / 0.22f);
        }

        float compression  = windup * 0.35f;
        float rollProgress = launch;
        float tuck         = (float) Math.sin(rollProgress * Math.PI);
        float impact       = 1f - recovery;

        // sideways (Z axis), 360 degrees
        float rollAngle = 360f * rollProgress * directionSign;

        // Lean the torso into the roll direction
        float leanRad = (float) Math.toRadians(15f * tuck * directionSign);

        float scaleY  = 1f - (tuck * 0.45f) - (impact * 0.08f);

        float pivotY  = 1.8f - (tuck * 0.4f);

        // torso
        model.body.xRot =
                compression * 0.8f
                        + tuck       * 0.6f
                        - recovery   * 0.5f;
        model.body.zRot = leanRad;

        // head
        model.head.xRot = model.body.xRot * 0.8f;
        model.head.zRot = model.body.zRot * 0.5f;

        // Arms
        float armSweep = tuck * 3f;
        if (directionSign > 0) {
            model.rightArm.xRot = -armSweep;
            model.rightArm.zRot = leanRad - 0.4f;
            model.leftArm.xRot  =  armSweep * 0.6f;
            model.leftArm.zRot  = leanRad + 0.2f;
        } else {
            model.leftArm.xRot  = -armSweep;
            model.leftArm.zRot  = leanRad + 0.4f;
            model.rightArm.xRot =  armSweep * 0.6f;
            model.rightArm.zRot = leanRad - 0.2f;
        }

        // legs
        float legFold = tuck * 3f;
        model.leftLeg.xRot  = legFold - compression * 0.4f;
        model.rightLeg.xRot = legFold - compression * 0.4f;
        model.leftLeg.zRot  = leanRad * 0.25f;
        model.rightLeg.zRot = leanRad * 0.25f;

        //sideways roll around Z, pivoted at feet
        var ps = event.getPoseStack();
        ps.pushPose();

        // squash offset
        float vertOffset = -compression * 0.08f - tuck * 0.12f - impact * 0.10f;
        ps.translate(0, vertOffset, 0);

        // Pivot at feet level, roll sideways around Z
        ps.translate(0, pivotY, 0);
        ps.mulPose(Axis.ZP.rotationDegrees(rollAngle)); // negative so right-roll tips right
        ps.scale(1f, scaleY, 1f);
        ps.translate(0, -pivotY, 0);


    }
    //post
    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        if (dodgeTick < 0) return;
        if (Minecraft.getInstance().player == null) return;

        HumanoidModel<?> model = (HumanoidModel<?>) ((LivingEntityRenderer) event.getRenderer()).getModel();

        // Reset every rotation so vanilla animation isn't corrupted
        model.body.xRot     = 0f; model.body.zRot     = 0f;
        model.head.xRot     = 0f; model.head.zRot     = 0f;
        model.leftArm.xRot  = 0f; model.leftArm.zRot  = 0f;
        model.rightArm.xRot = 0f; model.rightArm.zRot = 0f;
        model.leftLeg.xRot  = 0f; model.leftLeg.zRot  = 0f;
        model.rightLeg.xRot = 0f; model.rightLeg.zRot = 0f;

        event.getPoseStack().popPose();
    }


    public static void tick() {
        if (dodgeTick >= 0) {
            dodgeTick++;
            if (dodgeTick > DODGE_DURATION) dodgeTick = -1;
        }
    }

    private static float easeInCubic(float t)    { return t * t * t; }
    private static float easeOutQuad(float t)    { return 1f - (1f - t) * (1f - t); }
    private static float easeInOutQuad(float t)  {
        return t < 0.5f ? 2f * t * t : 1f - (float) Math.pow(-2f * t + 2f, 2) / 2f;
    }
}