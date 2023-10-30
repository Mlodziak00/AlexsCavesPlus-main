package com.alexscavesplus.alexscavesplus;

import com.alexscavesplus.alexscavesplus.common.reg.ACPBlocks;
import com.alexscavesplus.alexscavesplus.common.reg.ACPEntityType;
import com.alexscavesplus.alexscavesplus.common.reg.ACPItems;
import com.alexscavesplus.alexscavesplus.common.reg.ACPSoundEvents;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AlexsCavesPlus.MODID)
public class AlexsCavesPlus
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "alexscavesplus";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public AlexsCavesPlus()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ACPItems.register(modEventBus);
        ACPBlocks.register(modEventBus);
        ACPEntityType.register(modEventBus);
        ACPSoundEvents.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }


    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

}
