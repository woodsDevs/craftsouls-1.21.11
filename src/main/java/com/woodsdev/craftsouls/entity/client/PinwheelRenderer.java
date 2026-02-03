package com.woodsdev.craftsouls.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.woodsdev.craftsouls.craftsouls;
import com.woodsdev.craftsouls.entity.custom.PinwheelEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class PinwheelRenderer extends MobRenderer<PinwheelEntity, PinwheelEntityRenderState, PinwheelModel> {

    private static final Identifier PINWHEEL_LOCATION = Identifier.fromNamespaceAndPath("craftsouls","textures/entity/pinwheel/pinwheel.png");

    // In our constructor, we just forward to super
    public PinwheelRenderer(EntityRendererProvider.Context context) {
        super(context, new PinwheelModel(context.bakeLayer(PinwheelModel.LAYER_LOCATION)), 0.25F);
    }

    @Override
    public Identifier getTextureLocation(PinwheelEntityRenderState pinwheelEntityRenderState) {
        return PINWHEEL_LOCATION;
    }
    // Tell the render engine how to create a new entity render state.
    @Override
    public PinwheelEntityRenderState createRenderState() {
        return new PinwheelEntityRenderState();
    }

    // Update the render state by copying the needed values from the passed entity to the passed state.
    // Both Entity and EntityRenderState may be replaced with more concrete types,
    // based on the generic types that have been passed to the supertype.
    @Override
    public void extractRenderState(PinwheelEntity entity, PinwheelEntityRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.ageInTicks = entity.tickCount + partialTick;
        state.setAttacking(entity.isAttacking());

    }


}
