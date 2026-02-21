package com.woodsdev.craftsouls.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

public class StaminaOverlay {

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Post event) {

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) return;
        if (mc.options.hideGui) return;//hide on f1

        // read stamina from persistent data
        int stamina = player.getPersistentData()
                .getInt("craftsouls_stamina")
                .orElse(100); // default if not set


        GuiGraphics gui = event.getGuiGraphics();

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // Position aligned with hunger bar (right side)
        int width = 80;
        int height = 3;

        int x = screenWidth / 2 + 10;  // hunger start position
        int y = screenHeight - 47;

        int filled = (int)((stamina / 100f) * width);

        // Border
        gui.fill(x - 1, y - 1, x + width + 1, y + height + 1, 0xFF000000);

        // Background
        gui.fill(x, y, x + width, y + height, 0xFF1A1A1A);

        // Gradient fill
        gui.fillGradient(
                x, y,
                x + filled, y + height,
                0xFF2EFF00,
                0x00AA00
        );

        // Low stamina pulse
        if (stamina < 25) {
            float pulse = (float)(Math.sin(System.currentTimeMillis() / 150.0) * 0.5f + 0.5f);
            int red = (int)(200 + 55 * pulse);
            gui.fill(x, y, x + filled, y + height, (0xFF << 24) | (red << 16));
        }


    }
}
