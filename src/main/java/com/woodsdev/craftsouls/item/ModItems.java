package com.woodsdev.craftsouls.item;

import com.woodsdev.craftsouls.craftsouls;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.AttackRange;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(craftsouls.MODID);

    public static final DeferredItem<Item> GREATSWORD_OF_ARTORIAS =
            ITEMS.registerItem(
                    "greatsword_of_artorias",
                    props -> new Item(
                            props
                                    .sword(
                                            ToolMaterial.DIAMOND,
                                            12,        //
                                            -3.5f     // VERY slow = heavy
                                    )
                                    .component(
                                            DataComponents.ATTACK_RANGE,
                                            new AttackRange(
                                                    0.0f,   // min range
                                                    5.5f,   // max range (normal player)
                                                    0.0f,   // min creative range
                                                    5.5f,   // max creative range
                                                    0.5f,   // hitbox margin
                                                    1.0f    // mob scaling factor
                                            )
                                    )
                                    .rarity(Rarity.EPIC)
                                    .fireResistant()
                    )
            );

    public static final DeferredItem<Item> GOLEM_AXE =
            ITEMS.registerItem(
                    "golem_axe",
                    props -> new Item(
                            props
                                    .sword(
                                            ToolMaterial.DIAMOND,
                                            16,        //
                                            -3.8f     // VERY slow = heavy
                                    )
                                    .component(
                                            DataComponents.ATTACK_RANGE,
                                            new AttackRange(
                                                    0.0f,   // min range
                                                    5.5f,   // max range (normal player)
                                                    0.0f,   // min creative range
                                                    5.5f,   // max creative range
                                                    0.5f,   // hitbox margin
                                                    1.0f    // mob scaling factor
                                            )
                                    )
                                    .rarity(Rarity.EPIC)
                                    .fireResistant()
                    )
            );
}