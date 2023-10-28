package com.alexscavesplus.alexscavesplus.common.event;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import com.alexscavesplus.alexscavesplus.common.entity.AjolotodonEntity;
import com.alexscavesplus.alexscavesplus.common.reg.ACPEntityType;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AlexsCavesPlus.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ACPEventClientBus {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event){
        event.put(ACPEntityType.AJOLTODON.get(), AjolotodonEntity.setAttributes().build());
    }

}
