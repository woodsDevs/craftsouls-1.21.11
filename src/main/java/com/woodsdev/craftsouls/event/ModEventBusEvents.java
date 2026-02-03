package com.woodsdev.craftsouls.event;

import com.woodsdev.craftsouls.craftsouls;
import com.woodsdev.craftsouls.entity.ModEntities;
import com.woodsdev.craftsouls.entity.client.PinwheelModel;
import com.woodsdev.craftsouls.entity.client.PinwheelRenderer;
import com.woodsdev.craftsouls.entity.client.SifModel;
import com.woodsdev.craftsouls.entity.custom.PinwheelEntity;
import com.woodsdev.craftsouls.entity.custom.SifEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;


public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(PinwheelModel.LAYER_LOCATION, PinwheelModel::createBodyLayer);
        event.registerLayerDefinition(SifModel.LAYER_LOCATION, SifModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.PINWHEEL.get(), PinwheelEntity.createAttributes().build());
        event.put(ModEntities.SIF.get(), SifEntity.createAttributes().build());
    }
}
