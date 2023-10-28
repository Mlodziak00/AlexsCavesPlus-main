package com.alexscavesplus.alexscavesplus.common.reg;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.schedule.Activity;

public class ACPActivity {

    public static final Activity AJOLT_DETERMINATION = createActivity("ajolt_determination");


    private static Activity createActivity(String location){
        return Registry.register(BuiltInRegistries.ACTIVITY, location, new Activity(location));
    }
}
