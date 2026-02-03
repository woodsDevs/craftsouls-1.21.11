package com.woodsdev.craftsouls.entity;

import com.woodsdev.craftsouls.craftsouls;
import com.woodsdev.craftsouls.entity.custom.PinwheelEntity;
import com.woodsdev.craftsouls.entity.custom.SifEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.swing.*;
import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES =
            DeferredRegister.createEntities(craftsouls.MODID);

    //build entity
    public static final Supplier<EntityType<PinwheelEntity>> PINWHEEL = ENTITY_TYPES.register("pinwheel",
            () -> EntityType.Builder.of(PinwheelEntity::new, MobCategory.MONSTER).sized(1.0f,2.5f).build(ResourceKey.create(
                    Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath("craftsouls","pinwheel"))));

    public static final Supplier<EntityType<SifEntity>> SIF = ENTITY_TYPES.register("sif",
            () -> EntityType.Builder.of(SifEntity::new, MobCategory.MONSTER).sized(1.0f,2.5f).build(ResourceKey.create(
                    Registries.ENTITY_TYPE,
                    Identifier.fromNamespaceAndPath("craftsouls","sif"))));

    //register
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
