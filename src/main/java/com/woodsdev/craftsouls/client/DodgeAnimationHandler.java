package com.woodsdev.craftsouls.client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderPlayerEvent;

@EventBusSubscriber(modid = "craftsouls", value = Dist.CLIENT)
public class DodgeAnimationHandler {

    public static int dodgeTick = -1;
    public static final int DODGE_DURATION = 8;

    public static void startDodge() {
        dodgeTick = 0;
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (dodgeTick < 0) return;
        if (Minecraft.getInstance().player == null) return;

        float progress = (float) dodgeTick / DODGE_DURATION; // 0 → 1

        // smooth ease in/out
        float smooth = Mth.sin(progress * Mth.PI);

        // not a full spin
        float angle = smooth * 220f;

        event.getPoseStack().pushPose();

        // rotate around hips instead of head
        event.getPoseStack().translate(0, 0.8, 0);

        // main roll
        event.getPoseStack().mulPose(
                com.mojang.math.Axis.ZP.rotationDegrees(angle)
        );

        event.getPoseStack().translate(0, -0.8, 0);
    }


    @SubscribeEvent
    public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        if (dodgeTick < 0) return;
        if (Minecraft.getInstance().player == null) return;
        event.getPoseStack().popPose();
    }
    public static void tick() {
        if (dodgeTick >= 0) {
            dodgeTick++;
            if (dodgeTick > DODGE_DURATION) dodgeTick = -1;
        }
    }
}