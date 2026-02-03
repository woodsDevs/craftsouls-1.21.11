package com.woodsdev.craftsouls.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.woodsdev.craftsouls.entity.custom.PinwheelEntity;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


public class PinwheelModel extends EntityModel<PinwheelEntityRenderState> {

    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("modid", "pinwheel"), "main");
    // In the class body
    private final AnimationState idleAnimationState = new AnimationState();
    private final AnimationState attackAnimationState = new AnimationState();

    private boolean wasAttacking = false;

    private final KeyframeAnimation idleAnimation;
    private final KeyframeAnimation attackAnimation;

    private final ModelPart root;
    private final ModelPart head1;
    private final ModelPart head2;
    private final ModelPart head3;
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart arms;
    private final ModelPart lantern;
    private final ModelPart lantern2;
    private final ModelPart lantern3;
    private final ModelPart lantern4;
    private final ModelPart lantern5;
    private final ModelPart lantern6;
    private final ModelPart body3;

    public PinwheelModel(ModelPart root) {
        super(root);
        this.root = root.getChild("root");
        this.head1 = this.root.getChild("head1");
        this.head2 = this.root.getChild("head2");
        this.head3 = this.root.getChild("head3");
        this.body1 = this.root.getChild("body1");
        this.body2 = this.root.getChild("body2");
        this.arms = this.body2.getChild("arms");
        this.lantern = this.arms.getChild("lantern");
        this.lantern2 = this.arms.getChild("lantern2");
        this.lantern3 = this.arms.getChild("lantern3");
        this.lantern4 = this.arms.getChild("lantern4");
        this.lantern5 = this.arms.getChild("lantern5");
        this.lantern6 = this.arms.getChild("lantern6");
        this.body3 = this.root.getChild("body3");

        this.idleAnimation = PinwheelAnimations.ANIM_IDLE_PINWHEEL.bake(root);
        this.attackAnimation = PinwheelAnimations.ANIM_ATTACK_PINWHEEL.bake(root);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head1 = root.addOrReplaceChild("head1", CubeListBuilder.create(), PartPose.offset(-3.0F, -30.0F, -1.0F));

        PartDefinition head1_r1 = head1.addOrReplaceChild("head1_r1", CubeListBuilder.create().texOffs(0, 4).addBox(-2.0F, -26.0F, -4.0F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 22.4F, -0.3F, -0.1396F, 0.0F, -0.0873F));

        PartDefinition head2 = root.addOrReplaceChild("head2", CubeListBuilder.create(), PartPose.offset(-4.0F, -24.0F, -1.0F));

        PartDefinition head2_r1 = head2.addOrReplaceChild("head2_r1", CubeListBuilder.create().texOffs(0, 16).addBox(-10.0F, -24.0F, -3.0F, 4.0F, 6.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, 18.3F, 5.1F, 0.1571F, 0.0F, 0.0873F));

        PartDefinition head3 = root.addOrReplaceChild("head3", CubeListBuilder.create(), PartPose.offset(-1.0F, -24.0F, -1.0F));

        PartDefinition head3_r1 = head3.addOrReplaceChild("head3_r1", CubeListBuilder.create().texOffs(19, 2).addBox(7.0F, -23.0F, -4.0F, 4.0F, 5.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.95F, 19.0F, -2.05F, -0.1879F, -0.0397F, -0.293F));

        PartDefinition body1 = root.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(5, 114).addBox(-6.0F, -5.1F, 0.4F, 11.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(2, 112).addBox(-7.0F, -2.1F, -0.6F, 13.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -32.0F, -1.0F));

        PartDefinition body2 = root.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(51, 112).addBox(-4.0F, 0.0F, -1.0F, 11.0F, 10.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -25.1F, -0.5F));

        PartDefinition arms = body2.addOrReplaceChild("arms", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body2_r1 = arms.addOrReplaceChild("body2_r1", CubeListBuilder.create().texOffs(25, 78).addBox(0.0F, -10.6F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.2F, -1.9F, 0.4F, 0.0F, 0.0F, -0.5236F));

        PartDefinition body2_r2 = arms.addOrReplaceChild("body2_r2", CubeListBuilder.create().texOffs(25, 78).addBox(0.0F, -10.6F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.6F, -2.3F, 0.7F, 0.0F, 0.0F, -0.5236F));

        PartDefinition body2_r3 = arms.addOrReplaceChild("body2_r3", CubeListBuilder.create().texOffs(19, 78).addBox(-6.0F, -10.6F, 0.0F, 7.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, -2.4F, 0.5F, 0.0F, 0.0F, -0.5236F));

        PartDefinition body2_r4 = arms.addOrReplaceChild("body2_r4", CubeListBuilder.create().texOffs(0, 75).addBox(-6.0F, -10.6F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.8F, 16.4F, 0.4F, 0.0F, 0.0F, -0.2618F));

        PartDefinition body2_r5 = arms.addOrReplaceChild("body2_r5", CubeListBuilder.create().texOffs(0, 75).addBox(-6.0F, -10.6F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.6F, 17.0F, 0.6F, 0.0F, 0.0F, -0.2618F));

        PartDefinition body2_r6 = arms.addOrReplaceChild("body2_r6", CubeListBuilder.create().texOffs(0, 75).addBox(-6.0F, -10.6F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 17.1F, 0.5F, 0.0F, 0.0F, -0.2618F));

        PartDefinition body2_r7 = arms.addOrReplaceChild("body2_r7", CubeListBuilder.create().texOffs(19, 86).addBox(-2.0F, -13.1F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.9F, 0.7F, 0.7F, 0.0F, 0.0F, 0.5672F));

        PartDefinition body2_r8 = arms.addOrReplaceChild("body2_r8", CubeListBuilder.create().texOffs(17, 84).addBox(-2.0F, -13.1F, 0.0F, 1.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.4F, 0.0F, 0.6F, 0.0F, 0.0F, 0.5672F));

        PartDefinition body2_r9 = arms.addOrReplaceChild("body2_r9", CubeListBuilder.create().texOffs(19, 86).addBox(-2.0F, -13.1F, 0.0F, 7.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.2F, -0.3F, 0.5F, 0.0F, 0.0F, 0.5672F));

        PartDefinition body2_r10 = arms.addOrReplaceChild("body2_r10", CubeListBuilder.create().texOffs(6, 82).addBox(4.0F, -13.1F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.6F, 19.6F, 0.6F, 0.0F, 0.0F, 0.3491F));

        PartDefinition body2_r11 = arms.addOrReplaceChild("body2_r11", CubeListBuilder.create().texOffs(6, 82).addBox(4.0F, -13.1F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.3F, 19.4F, 0.4F, 0.0F, 0.0F, 0.3491F));

        PartDefinition body2_r12 = arms.addOrReplaceChild("body2_r12", CubeListBuilder.create().texOffs(0, 82).addBox(-2.0F, -13.1F, 0.0F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 19.6F, 0.5F, 0.0F, 0.0F, 0.3491F));

        PartDefinition body2_r13 = arms.addOrReplaceChild("body2_r13", CubeListBuilder.create().texOffs(15, 85).addBox(4.0F, -13.1F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.7F, 13.7F, 0.5F, 0.0F, 0.0F, -0.3927F));

        PartDefinition body2_r14 = arms.addOrReplaceChild("body2_r14", CubeListBuilder.create().texOffs(22, 85).addBox(4.0F, -13.1F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.2F, 13.6F, 0.3F, 0.0F, 0.0F, -0.3927F));

        PartDefinition body2_r15 = arms.addOrReplaceChild("body2_r15", CubeListBuilder.create().texOffs(0, 87).addBox(-1.0F, -13.1F, 0.0F, 6.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, 13.6F, 0.5F, 0.0F, 0.0F, -0.3927F));

        PartDefinition body2_r16 = arms.addOrReplaceChild("body2_r16", CubeListBuilder.create().texOffs(40, 11).addBox(-8.0F, -14.6F, 1.0F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-17.3F, -0.05F, 0.1F, 0.0F, 0.0F, 0.5498F));

        PartDefinition body2_r17 = arms.addOrReplaceChild("body2_r17", CubeListBuilder.create().texOffs(40, 11).addBox(-8.0F, -14.6F, 1.0F, 11.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(25.0F, -2.55F, 0.1F, 0.0F, 0.0F, -0.5672F));

        PartDefinition body2_r18 = arms.addOrReplaceChild("body2_r18", CubeListBuilder.create().texOffs(44, 24).addBox(-8.0F, -14.6F, 1.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.0F, 10.75F, 0.0F, 0.0F, 0.0F, -0.3927F));

        PartDefinition body2_r19 = arms.addOrReplaceChild("body2_r19", CubeListBuilder.create().texOffs(44, 24).addBox(-8.0F, -14.6F, 1.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(13.3F, 25.35F, 0.0F, 0.0F, 0.0F, 0.384F));

        PartDefinition body2_r20 = arms.addOrReplaceChild("body2_r20", CubeListBuilder.create().texOffs(44, 24).addBox(-8.0F, -14.6F, 1.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, 22.85F, 0.0F, 0.0F, 0.0F, -0.2531F));

        PartDefinition body2_r21 = arms.addOrReplaceChild("body2_r21", CubeListBuilder.create().texOffs(44, 24).addBox(-8.0F, -14.6F, 1.0F, 9.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, 15.75F, 0.0F, 0.0F, 0.0F, 0.1309F));

        PartDefinition body2_r22 = arms.addOrReplaceChild("body2_r22", CubeListBuilder.create().texOffs(0, 68).addBox(-6.0F, -15.6F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.8F, 17.4F, 0.7F, 0.0F, 0.0F, 0.1309F));

        PartDefinition body2_r23 = arms.addOrReplaceChild("body2_r23", CubeListBuilder.create().texOffs(0, 68).addBox(-6.0F, -15.6F, 0.0F, 1.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 68).addBox(-6.0F, -15.6F, -0.1F, 7.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-5.0F, 17.1F, 0.6F, 0.0F, 0.0F, 0.1309F));

        PartDefinition lantern = arms.addOrReplaceChild("lantern", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body2_r24 = lantern.addOrReplaceChild("body2_r24", CubeListBuilder.create().texOffs(120, 81).addBox(-1.0F, 0.4F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.3F, -13.65F, 0.5F, 0.0F, 0.0F, -0.0175F));

        PartDefinition body2_r25 = lantern.addOrReplaceChild("body2_r25", CubeListBuilder.create().texOffs(123, 74).addBox(0.0F, 0.4F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.9F, -15.65F, 0.3F, 0.0F, 0.0F, -0.0175F));

        PartDefinition lantern2 = arms.addOrReplaceChild("lantern2", CubeListBuilder.create(), PartPose.offset(0.7F, 15.3F, 0.0F));

        PartDefinition body2_r26 = lantern2.addOrReplaceChild("body2_r26", CubeListBuilder.create().texOffs(120, 81).addBox(-1.0F, 0.4F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.3F, -13.25F, 0.6F, 0.0F, 0.0F, -0.0175F));

        PartDefinition body2_r27 = lantern2.addOrReplaceChild("body2_r27", CubeListBuilder.create().texOffs(123, 74).addBox(0.0F, 0.4F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9F, -15.25F, 0.4F, 0.0F, 0.0F, -0.0175F));

        PartDefinition lantern3 = arms.addOrReplaceChild("lantern3", CubeListBuilder.create(), PartPose.offset(0.7F, 25.5F, -0.3F));

        PartDefinition body2_r28 = lantern3.addOrReplaceChild("body2_r28", CubeListBuilder.create().texOffs(120, 81).addBox(-1.0F, 0.4F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.3F, -13.25F, 0.6F, 0.0F, 0.0F, -0.0175F));

        PartDefinition body2_r29 = lantern3.addOrReplaceChild("body2_r29", CubeListBuilder.create().texOffs(123, 74).addBox(0.0F, 0.4F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9F, -15.25F, 0.4F, 0.0F, 0.0F, -0.0175F));

        PartDefinition lantern4 = arms.addOrReplaceChild("lantern4", CubeListBuilder.create(), PartPose.offset(35.7F, -0.8F, 0.0F));

        PartDefinition body2_r30 = lantern4.addOrReplaceChild("body2_r30", CubeListBuilder.create().texOffs(120, 81).addBox(-1.0F, 0.4F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.3F, -13.25F, 0.6F, 0.0F, 0.0F, -0.0175F));

        PartDefinition body2_r31 = lantern4.addOrReplaceChild("body2_r31", CubeListBuilder.create().texOffs(123, 74).addBox(0.0F, 0.4F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9F, -15.25F, 0.4F, 0.0F, 0.0F, -0.0175F));

        PartDefinition lantern5 = arms.addOrReplaceChild("lantern5", CubeListBuilder.create(), PartPose.offset(34.0F, 12.5F, 0.0F));

        PartDefinition body2_r32 = lantern5.addOrReplaceChild("body2_r32", CubeListBuilder.create().texOffs(120, 81).addBox(-1.0F, 0.4F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.3F, -13.25F, 0.6F, 0.0F, 0.0F, -0.0175F));

        PartDefinition body2_r33 = lantern5.addOrReplaceChild("body2_r33", CubeListBuilder.create().texOffs(123, 74).addBox(0.0F, 0.4F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.9F, -15.25F, 0.4F, 0.0F, 0.0F, -0.0175F));

        PartDefinition lantern6 = arms.addOrReplaceChild("lantern6", CubeListBuilder.create(), PartPose.offset(18.0F, 11.8F, 2.0F));

        PartDefinition body2_r34 = lantern6.addOrReplaceChild("body2_r34", CubeListBuilder.create().texOffs(120, 81).addBox(-1.0F, 0.4F, 0.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.3F, 1.75F, -1.4F, 0.0F, 0.0F, -0.0175F));

        PartDefinition body2_r35 = lantern6.addOrReplaceChild("body2_r35", CubeListBuilder.create().texOffs(123, 74).addBox(0.0F, 0.4F, 1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.3F, -0.25F, -1.6F, 0.0F, 0.0F, -0.0175F));

        PartDefinition body3 = root.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(60, 86).addBox(-3.0F, 0.0F, 0.0F, 9.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -15.1F, -0.5F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(PinwheelEntityRenderState state) {
        super.setupAnim(state);

        boolean attacking = state.isAttacking();

        // ATTACK JUST STARTED
        if (attacking && !wasAttacking) {
            attackAnimationState.stop(); // hard reset
            attackAnimationState.start((int) state.ageInTicks);
            idleAnimationState.stop();
            System.out.println("ATTACK START");
        }

        // ATTACK JUST ENDED
        if (!attacking && wasAttacking) {
            attackAnimationState.stop();
            idleAnimationState.start((int) state.ageInTicks);
            System.out.println("IDLE START");
        }

        // APPLY
        if (attacking) {
            attackAnimation.apply(attackAnimationState, state.ageInTicks, 2.0f);
        } else {
            idleAnimation.apply(idleAnimationState, state.ageInTicks, .7f);
        }

        wasAttacking = attacking;

    }

}
