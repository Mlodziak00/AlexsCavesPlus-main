package com.alexscavesplus.alexscavesplus.common.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class ACPValidateDetermination {
    public static BehaviorControl<LivingEntity> create(){
        return BehaviorBuilder.create((entity) -> {
            return entity.group(entity.present(MemoryModuleType.PLAY_DEAD_TICKS), entity.registered(MemoryModuleType.HURT_BY_ENTITY)).apply(entity, (memory, entityMemoryAccessor) -> {
                return (level, living, l) -> {
                    int i = entity.get(memory);
                    if (i <= 0) {
                        memory.erase();
                        entityMemoryAccessor.erase();
                        living.getBrain().useDefaultActivity();
                    } else {
                        memory.set(i - 1);
                    }

                    return true;
                };
            });
        });
    }
}
