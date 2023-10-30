package com.alexscavesplus.alexscavesplus.client.model;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.common.entity.AjolotodonEntity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.basic.BasicModelPart;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LerpingModel;
import org.checkerframework.checker.units.qual.A;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import javax.annotation.concurrent.Immutable;


public class AjoltodonModel<T extends AjolotodonEntity & LerpingModel> extends GeoModel<T> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/ajoltodon.png");
    private static final ResourceLocation BABY_TEXTURE = new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/baby_ajoltodon.png");
    private static final ResourceLocation BUDDY_TEXTURE = new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/buddy_ajoltodon.png");
    private static final ResourceLocation BABY_BUDDY_TEXTURE = new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/baby_buddy_ajolotodon.png");

    private static final ResourceLocation NORMAL_MODEL = new ResourceLocation(AlexsCavesPlus.MODID, "geo/ajolotodon.geo.json");
    private static final ResourceLocation BABY_MODEL = new ResourceLocation(AlexsCavesPlus.MODID, "geo/baby_ajolotodon.geo.json");
    private static final ResourceLocation AJOLOTODON_ANIMATION = new ResourceLocation(AlexsCavesPlus.MODID, "animations/ajolotodon.animation.json");

    @Override
    public ResourceLocation getModelResource(AjolotodonEntity entity) {
        return entity.isBaby() ? BABY_MODEL : NORMAL_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(AjolotodonEntity entity) {
        return entity.isBaby() && entity.hasCustomName() && "buddy".equalsIgnoreCase(entity.getName().getString()) ? BABY_BUDDY_TEXTURE :
                entity.hasCustomName() && "buddy".equalsIgnoreCase(entity.getName().getString()) ? BUDDY_TEXTURE :
                        entity.isBaby() ? BABY_TEXTURE :
                                TEXTURE;
    }

    @Override
    public ResourceLocation getAnimationResource(AjolotodonEntity entity) {
        return AJOLOTODON_ANIMATION;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if (head != null){
            EntityModelData entityModelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityModelData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityModelData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
