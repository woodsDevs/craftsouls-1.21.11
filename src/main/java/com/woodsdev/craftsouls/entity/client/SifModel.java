package com.woodsdev.craftsouls.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

public class SifModel extends EntityModel<SifEntityRenderState> {

    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("modid","sif"), "main");

    private final AnimationState idleAnimationState = new AnimationState();
    private final AnimationState attackingAnimationState = new AnimationState();
    private final AnimationState walkAnimationState = new AnimationState();

    private boolean wasAttacking = false;

    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation attackingAnimation;
    private final KeyframeAnimation walkAnimation;

    private final ModelPart head;
    private final ModelPart sword;
    private final ModelPart mane;
    private final ModelPart body;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;
    private final ModelPart leg4;
    private final ModelPart tail;

    public SifModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
        this.sword = this.head.getChild("sword");
        this.mane = root.getChild("mane");
        this.body = root.getChild("body");
        this.leg1 = root.getChild("leg1");
        this.leg2 = root.getChild("leg2");
        this.leg3 = root.getChild("leg3");
        this.leg4 = root.getChild("leg4");
        this.tail = root.getChild("tail");

        this.idleAnimation = SifAnimations.ANIM_IDLE_SIF.bake(root);
        this.attackingAnimation = SifAnimations.ANIM_ATTACK_SIF.bake(root);
        this.walkAnimation = SifAnimations.ANIM_WALK_SIF.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(12, 13).addBox(-12.56F, -18.92F, -12.72F, 38.0F, 38.0F, 25.0F, new CubeDeformation(0.0F))
                .texOffs(134, 117).addBox(12.44F, -32.08F, 0.0F, 13.0F, 13.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(131, 115).addBox(-13.0F, -32.08F, 0.0F, 13.0F, 13.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(9, 89).addBox(-3.1F, -0.0472F, -31.8F, 19.0F, 19.0F, 25.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -45.5F, -7.0F));

        PartDefinition sword = head.addOrReplaceChild("sword", CubeListBuilder.create().texOffs(430, 210).addBox(-1.0F, 9.0F, 0.0F, 25.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(419, 233).addBox(-27.0F, 9.0F, 0.0F, 25.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(346, 191).addBox(-27.0F, 9.0F, -7.0F, 9.0F, 3.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(382, 110).addBox(-72.0F, 10.0F, -6.0F, 50.0F, 1.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(456, 111).addBox(-86.0F, 10.1F, -5.0F, 15.0F, 1.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(474, 149).addBox(-95.0F, 10.2F, -3.0F, 10.0F, 1.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(434, 252).addBox(-102.0F, 10.4F, 0.0F, 4.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(492, 250).addBox(-99.0F, 10.4F, -1.0F, 5.0F, 1.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(336, 236).addBox(-26.0F, 8.0F, -6.0F, 7.0F, 5.0F, 15.0F, new CubeDeformation(0.0F))
                .texOffs(375, 218).addBox(23.0F, 8.0F, -1.0F, 2.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 0.0F, -27.0F));

        PartDefinition mane = partdefinition.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(185, 11).addBox(-19.2F, -18.92F, -19.08F, 51.0F, 38.0F, 45.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, -42.32F, 18.44F, 1.5708F, 0.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(164, 123).addBox(-18.92F, -12.48F, -19.08F, 38.0F, 66.0F, 38.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.36F, -42.32F, 50.24F, 1.5708F, 0.0F, 0.0F));

        PartDefinition leg1 = partdefinition.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(78, 146).addBox(-0.28F, -0.12F, 4.64F, 13.0F, 51.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.54F, -29.6F, 82.04F));

        PartDefinition leg2 = partdefinition.addOrReplaceChild("leg2", CubeListBuilder.create().texOffs(77, 146).addBox(-0.28F, -0.12F, 4.64F, 13.0F, 51.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(8.54F, -29.6F, 82.04F));

        PartDefinition leg3 = partdefinition.addOrReplaceChild("leg3", CubeListBuilder.create().texOffs(7, 146).addBox(-0.28F, -0.12F, -6.36F, 13.0F, 51.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.54F, -29.6F, 12.08F));

        PartDefinition leg4 = partdefinition.addOrReplaceChild("leg4", CubeListBuilder.create().texOffs(6, 147).addBox(-0.28F, -0.12F, -6.36F, 13.0F, 51.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(8.54F, -29.6F, 12.08F));

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(78, 146).addBox(-0.28F, -0.12F, 1.64F, 13.0F, 51.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -55.04F, 101.12F));

        return LayerDefinition.create(meshdefinition, 512, 256);
    }

    @Override
    public void setupAnim(SifEntityRenderState state) {
        super.setupAnim(state);
        boolean attacking = state.isAttacking();
        boolean walking = state.walking;

        // attack start
        if (attacking && !wasAttacking) {
            attackingAnimationState.stop();
            attackingAnimationState.start((int) state.ageInTicks);

            walkAnimationState.stop();
            idleAnimationState.stop();
        }

        // attack end, back to idle
        if (!attacking && wasAttacking) {
            attackingAnimationState.stop();
            idleAnimationState.start((int) state.ageInTicks);
        }

        // walk start
        if (walking && !attacking) {
            if (!walkAnimationState.isStarted()) {
                walkAnimationState.start((int) state.ageInTicks);
                idleAnimationState.stop();
            }
        }

        // idle start
        if (!walking && !attacking) {
            if (!idleAnimationState.isStarted()) {
                idleAnimationState.start((int) state.ageInTicks);
                walkAnimationState.stop();
            }
        }

        // apply
        if (attacking) {
            attackingAnimation.apply(attackingAnimationState, state.ageInTicks, 2.0f);
        } else if (walking) {
            walkAnimation.apply(walkAnimationState, state.ageInTicks, 0.7f);
        } else {
            idleAnimation.apply(idleAnimationState, state.ageInTicks, 1.0f);
        }

        wasAttacking = attacking;


    }

}
