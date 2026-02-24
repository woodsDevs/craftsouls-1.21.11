package com.woodsdev.craftsouls.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = "craftsouls", value = Dist.CLIENT)
public class AtmosphereHandler {

    public static boolean DARK_SOULS_MODE = true;

    private static boolean gammaModified = false;
    private static double originalGamma = 1.0;

    // fog distance

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        if (!DARK_SOULS_MODE) return;

        event.setNearPlaneDistance(0.0F);
        event.setFarPlaneDistance(160.0F); // Closer fog = heavier mood
    }

    // fog Color

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        if (!DARK_SOULS_MODE) return;

        event.setRed(0.28F);
        event.setGreen(0.28F);
        event.setBlue(0.32F);
    }

    //cliernttick

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) {
            restoreGamma(mc);
            return;
        }

        if (DARK_SOULS_MODE) {
            applyGammaDarkening(mc);
        } else {
            restoreGamma(mc);
        }

        if (!DARK_SOULS_MODE) return;

        LocalPlayer player = mc.player;
        if (player == null) return;

        for (int i = 0; i < 8; i++) {
            double x = player.getX() + (mc.level.random.nextDouble() - 0.5) * 10;
            double y = player.getY() + mc.level.random.nextDouble() * 3;
            double z = player.getZ() + (mc.level.random.nextDouble() - 0.5) * 10;

            mc.level.addParticle(
                    ParticleTypes.ASH,
                    x, y, z,
                    0.02, 0.0, 0.02
            );
        }
    }

    //screen overlay

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.Post event) {
        if (!DARK_SOULS_MODE) return;

        Minecraft mc = Minecraft.getInstance();
        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        // Slight dark overlay
        event.getGuiGraphics().fill(
                0, 0, width, height,
                0x11000000 // subtle darkness (increase to 0x44000000 for stronger)
        );
    }

    // gamma
    private static void applyGammaDarkening(Minecraft mc) {
        if (!gammaModified) {
            originalGamma = mc.options.gamma().get();
            gammaModified = true;
        }

        mc.options.gamma().set(0.01); // Lower = darker world
    }

    private static void restoreGamma(Minecraft mc) {
        if (gammaModified) {
            mc.options.gamma().set(originalGamma);
            gammaModified = false;
        }
    }
}