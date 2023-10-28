package com.alexscavesplus.alexscavesplus.common.reg;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.common.entity.AjolotodonEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ACPEntityType {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AlexsCavesPlus.MODID);

    public static final RegistryObject<EntityType<AjolotodonEntity>> AJOLTODON = ENTITY_TYPES.register("ajoltodon",
            () -> EntityType.Builder.of(AjolotodonEntity::new, MobCategory.AXOLOTLS)
                    .sized(0.85F, 0.75F).build(new ResourceLocation(AlexsCavesPlus.MODID, "ajoltodon").toString()));


    // Entity Type Tags
    public static final TagKey<EntityType<?>> AJOLOTODON_HUNTABLES = createTagKey("ajolotodon_huntables");
    public static final TagKey<EntityType<?>> AJOLOTODON_HUNT = createTagKey("ajolotodon_hunt");

    // For Entity Type Keys
    private static TagKey<EntityType<?>> createTagKey(String location){
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(AlexsCavesPlus.MODID, location));
    }

    // For Registering into AlexsCavesPlus.class
    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);}
}

