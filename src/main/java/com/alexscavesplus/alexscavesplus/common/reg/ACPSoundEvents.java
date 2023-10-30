package com.alexscavesplus.alexscavesplus.common.reg;

import com.alexscavesplus.alexscavesplus.AlexsCavesPlus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACPSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AlexsCavesPlus.MODID);

    public static final RegistryObject<SoundEvent> AJOLOTODON_CHIRP = registerSoundEvent("ajolotodon_chirp");
    public static final RegistryObject<SoundEvent> AJOLOTODON_CHIRP_AIR = registerSoundEvent("ajolotodon_chirp_air");
    public static final RegistryObject<SoundEvent> AJOLOTODON_ATTACK = registerSoundEvent("ajolotodon_attack");
    public static final RegistryObject<SoundEvent> AJOLOTODON_HURT = registerSoundEvent("ajolotodon_hurt");
    public static final RegistryObject<SoundEvent> AJOLOTODON_DEATH = registerSoundEvent("ajolotodon_death");
    public static final RegistryObject<SoundEvent> MARSH_DISC = registerSoundEvent("marsh_disc");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = new ResourceLocation(AlexsCavesPlus.MODID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }
    public static void register(IEventBus event){
        SOUND_EVENTS.register(event);
    }
}
