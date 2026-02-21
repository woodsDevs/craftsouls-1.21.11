package com.woodsdev.craftsouls.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.feline.Cat;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class DodgeKeyRegister {

    public static KeyMapping dodgeLeft;
    public static KeyMapping dodgeRight;
    public static KeyMapping toggleDodge;

    private static final KeyMapping.Category CATEGORY =
            KeyMapping.Category.register(
                    Identifier.fromNamespaceAndPath("craftsouls", "keys")
            );

    @SubscribeEvent
    public void onKeyRegister(RegisterKeyMappingsEvent event) {

        dodgeLeft = new KeyMapping(
                "key.craftsouls.dodge_left",
                GLFW.GLFW_KEY_A,
                CATEGORY
        );

        dodgeRight = new KeyMapping(
                "key.craftsouls.dodge_right",
                GLFW.GLFW_KEY_D,
                CATEGORY
        );

        toggleDodge = new KeyMapping(
                "key.craftsouls.toggle_dodge",
                GLFW.GLFW_KEY_O,
                CATEGORY
        );

        event.register(toggleDodge);
        event.register(dodgeLeft);
        event.register(dodgeRight);
    }
}
