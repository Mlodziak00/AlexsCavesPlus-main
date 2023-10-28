package com.alexscavesplus.alexscavesplus.client.render;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.client.model.AjoltodonModel;
import com.alexscavesplus.alexscavesplus.common.entity.AjolotodonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class AjoltodonRenderer extends GeoEntityRenderer<AjolotodonEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/ajoltodon.png");
    private static final ResourceLocation BABY_TEXTURE = new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/baby_ajoltodon.png");
    private static final ResourceLocation BUDDY_TEXTURE = new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/buddy_ajoltodon.png");
    private static final ResourceLocation BABY_BUDDY_TEXTURE = new ResourceLocation(AlexsCavesPlus.MODID, "textures/entity/baby_buddy_ajolotodon.png");

    public AjoltodonRenderer(EntityRendererProvider.Context context) {
        super(context, new AjoltodonModel());
    }

    @Override
    public ResourceLocation getTextureLocation(AjolotodonEntity entity) {
        return entity.isBaby() && entity.hasCustomName() && "buddy".equalsIgnoreCase(entity.getName().getString()) ? BABY_BUDDY_TEXTURE :
                entity.hasCustomName() && "buddy".equalsIgnoreCase(entity.getName().getString()) ? BUDDY_TEXTURE :
                        entity.isBaby() ? BABY_TEXTURE :
                                TEXTURE;
    }

    @Override
    public void render(AjolotodonEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}

