package com.woodsdev.craftsouls.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.woodsdev.craftsouls.entity.custom.IronGolemBossEntity;
import com.woodsdev.craftsouls.entity.custom.PinwheelEntity;
import net.minecraft.client.model.animal.golem.IronGolemModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class IronGolemBossRenderer extends MobRenderer<IronGolemBossEntity,IronGolemBossEntityRenderState, IronGolemBossModel> {

    private static final Identifier IG_LOCATION = Identifier.fromNamespaceAndPath("craftsouls","textures/entity/irongolemboss/irongolemboss.png");

    //forward to super
    public IronGolemBossRenderer(EntityRendererProvider.Context context) {
        super(context, new IronGolemBossModel(context.bakeLayer(IronGolemBossModel.LAYER_LOCATION)), 2.0f);
    }

    @Override
    public Identifier getTextureLocation(IronGolemBossEntityRenderState ironGolemBossEntityRenderState) {
        return IG_LOCATION;
    }

    //tell render engine how to create new entity render state
    @Override
    public IronGolemBossEntityRenderState createRenderState() { return new IronGolemBossEntityRenderState();}

    // Update the render state by copying the needed values from the passed entity to the passed state.
    // Both Entity and EntityRenderState may be replaced with more concrete types,
    // based on the generic types that have been passed to the supertype.
    @Override
    public void extractRenderState(IronGolemBossEntity entity, IronGolemBossEntityRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.ageInTicks = entity.tickCount + partialTick;
        state.grabbing = entity.getEntityData().get(IronGolemBossEntity.GRABBING);
        state.grabTick = entity.getGrabTick();
        state.stomping = entity.getEntityData().get(IronGolemBossEntity.STOMPING);
        state.axeAttacking = entity.getEntityData().get(IronGolemBossEntity.AXE_ATTACK);
    }

}