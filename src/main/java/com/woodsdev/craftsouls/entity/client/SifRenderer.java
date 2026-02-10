package com.woodsdev.craftsouls.entity.client;

import com.woodsdev.craftsouls.entity.custom.SifEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

public class SifRenderer extends MobRenderer<SifEntity, SifEntityRenderState, SifModel> {
    private static final Identifier SIF_LOCATION = Identifier.fromNamespaceAndPath("craftsouls", "textures/entity/sif/sif.png");

    public SifRenderer(EntityRendererProvider.Context context) {
        super(context, new SifModel(context.bakeLayer(SifModel.LAYER_LOCATION)), 0.25F);
    }

    @Override
    public Identifier getTextureLocation(SifEntityRenderState sifEntityRenderState) {
        return SIF_LOCATION;
    }

    @Override
    public SifEntityRenderState createRenderState() {
        return new SifEntityRenderState();
    }

    @Override
    public void extractRenderState(SifEntity entity, SifEntityRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.ageInTicks = entity.tickCount + partialTick;
        state.setAttacking(entity.getAttacking());
        state.walking = entity.isWalking();

    }

}
