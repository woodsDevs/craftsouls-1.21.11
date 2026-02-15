package com.woodsdev.craftsouls.entity.client;

import com.woodsdev.craftsouls.entity.custom.IronGolemBossEntity;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;

public class IronGolemBossModel extends EntityModel<IronGolemBossEntityRenderState> {

    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Identifier.fromNamespaceAndPath("craftsouls", "irongolemboss"), "main");

    //class body
    private final AnimationState grabAnimationState = new AnimationState();
    private final AnimationState attackAnimationState = new AnimationState();
    private final AnimationState stompAnimationState = new AnimationState();

    private final KeyframeAnimation grabAnimation;
    private final KeyframeAnimation attackAnimation;
    private final KeyframeAnimation stompAnimation;

    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart right_arm;
    private final ModelPart left_arm;
    private final ModelPart axe;
    private final ModelPart right_leg;
    private final ModelPart left_leg;

    public IronGolemBossModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.head = root.getChild("head");
        this.right_arm = root.getChild("right_arm");
        this.left_arm = root.getChild("left_arm");
        this.axe = this.left_arm.getChild("axe");
        this.right_leg = root.getChild("right_leg");
        this.left_leg = root.getChild("left_leg");

        this.grabAnimation = IronGolemBossAnimations.ANIM_GRAB_IG.bake(root);
        this.attackAnimation = IronGolemBossAnimations.ANIM_ATTACK_IG.bake(root);
        this.stompAnimation = IronGolemBossAnimations.ANIM_STOMP_IG.bake(root);

    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 160).addBox(-36.0F, -8.0F, -24.0F, 72.0F, 48.0F, 44.0F, new CubeDeformation(0.0F))
                .texOffs(0, 90).addBox(-33.0F, 29.0F, -21.0F, 67.0F, 24.0F, 38.0F, new CubeDeformation(0.0F))
                .texOffs(7, 72).addBox(-14.5F, 38.5F, -14.5F, 29.0F, 23.0F, 25.0F, new CubeDeformation(0.5F))
                .texOffs(13, 72).addBox(-30.5F, 43.5F, -14.5F, 12.0F, 23.0F, 25.0F, new CubeDeformation(0.5F))
                .texOffs(13, 72).addBox(18.5F, 43.5F, -14.5F, 12.0F, 23.0F, 25.0F, new CubeDeformation(0.5F)), PartPose.offset(15.0F, -98.9F, 0.0F));

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(17, 15).addBox(-14.0F, -44.0F, -2.0F, 28.0F, 36.0F, 17.0F, new CubeDeformation(0.0F))
                .texOffs(383, 18).addBox(-12.0F, -43.0F, -9.0F, 24.0F, 35.0F, 23.0F, new CubeDeformation(0.0F))
                .texOffs(410, 88).addBox(-9.0F, -41.0F, -11.0F, 18.0F, 33.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(183, 276).addBox(-3.0F, -59.0F, 7.0F, 6.0F, 17.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(177, 50).addBox(-3.0F, -59.2F, -7.9F, 6.0F, 4.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offset(15.0F, -98.9F, -8.0F));

        PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(242, 508).addBox(-10.0F, -2.0F, -1.0F, 32.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9F, -30.0F, -11.5F, 0.0F, 0.0F, 1.5708F));

        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(142, 53).addBox(-5.0F, -2.0F, -16.0F, 6.0F, 4.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.9F, -54.3F, 9.3F, -0.192F, -0.637F, 0.0F));

        PartDefinition head_r2 = head.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(30, 381).addBox(-3.0F, -3.0F, -7.0F, 6.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -57.2F, -5.9F, 1.0821F, 0.0F, 0.0F));

        PartDefinition head_r3 = head.addOrReplaceChild("head_r3", CubeListBuilder.create().texOffs(269, 52).addBox(-1.0F, -2.0F, -16.0F, 5.0F, 4.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.0F, -54.5F, 9.1F, -0.1745F, 0.6545F, 0.0F));

        PartDefinition head_r4 = head.addOrReplaceChild("head_r4", CubeListBuilder.create().texOffs(57, 25).addBox(-6.0F, -145.0F, -41.0F, 6.0F, 17.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(67.0F, 65.9F, 47.0F, 0.0F, 0.0F, -0.5672F));

        PartDefinition head_r5 = head.addOrReplaceChild("head_r5", CubeListBuilder.create().texOffs(68, 24).addBox(-2.0F, -145.0F, -40.0F, 5.0F, 17.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-66.0F, 67.0F, 47.0F, 0.0F, 0.0F, 0.5672F));

        PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(243, 97).addBox(-10.0F, -4.0F, -12.0F, 16.0F, 83.0F, 21.0F, new CubeDeformation(0.0F)), PartPose.offset(-27.0F, -104.9F, 0.0F));

        PartDefinition cube_r2 = right_arm.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(409, 452).addBox(-2.0F, -24.0F, -19.0F, 9.0F, 31.0F, 36.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -5.1F, 0.0F, 0.0F, 0.0F, 1.3963F));

        PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(243, 235).addBox(-11.0F, -7.0F, -12.0F, 16.0F, 84.0F, 21.0F, new CubeDeformation(0.0F))
                .texOffs(19, 353).addBox(-17.0F, -15.0F, -17.0F, 28.0F, 28.0F, 33.0F, new CubeDeformation(0.0F)), PartPose.offset(61.0F, -101.9F, 0.0F));

        PartDefinition axe = left_arm.addOrReplaceChild("axe", CubeListBuilder.create().texOffs(261, 343).addBox(-3.9F, -1.0F, -70.0F, 3.0F, 3.0F, 78.0F, new CubeDeformation(0.0F))
                .texOffs(398, 138).addBox(-4.8F, -2.0F, -68.0F, 5.0F, 19.0F, 36.0F, new CubeDeformation(0.0F))
                .texOffs(396, 189).addBox(-3.8F, 9.0F, -70.0F, 3.0F, 10.0F, 40.0F, new CubeDeformation(0.0F))
                .texOffs(397, 239).addBox(-2.8F, 12.7F, -72.0F, 1.0F, 10.0F, 44.0F, new CubeDeformation(0.0F))
                .texOffs(392, 330).addBox(-2.5F, 18.7F, -75.0F, 1.0F, 6.0F, 50.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 67.0F, -17.0F));

        PartDefinition right_leg = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(154, 4).addBox(-12.0F, -20.0F, -10.0F, 20.0F, 72.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(139, 51).addBox(-13.0F, 44.0F, -21.4F, 22.0F, 9.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, -26.9F, 0.0F));

        PartDefinition left_leg = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(247, 4).addBox(-12.0F, -20.0F, -10.0F, 20.0F, 72.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(240, 48).addBox(-13.0F, 44.0F, -21.4F, 22.0F, 9.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(35.0F, -26.9F, 0.0F));

        return LayerDefinition.create(meshdefinition, 512, 512);
    }

    @Override
    public void setupAnim(IronGolemBossEntityRenderState state) {
        super.setupAnim(state);
        if (state.grabbing) {

            if (!grabAnimationState.isStarted()) {
                grabAnimationState.start((int) state.ageInTicks);
            }

        } else {
            grabAnimationState.stop();
        }
        if(state.stomping){
            if(!stompAnimationState.isStarted()) {
                stompAnimationState.start((int) state.ageInTicks);
            }
        }else {
            stompAnimationState.stop();
        }
        if (state.axeAttacking){
            if (!attackAnimationState.isStarted()) {
                attackAnimationState.start((int) state.ageInTicks);
            }
        }else {
            attackAnimationState.stop();
        }

        attackAnimation.apply(attackAnimationState,state.ageInTicks,2.0f);
        stompAnimation.apply(stompAnimationState,state.ageInTicks,1.0f);
        grabAnimation.apply(grabAnimationState, state.ageInTicks, 1.0f);


    }


}
