package com.alexscavesplus.alexscavesplus.common.entity.Ai;

import com.alexscavesplus.alexscavesplus.common.entity.AjolotodonEntity;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

public class DeterminationBehaviour extends Behavior<AjolotodonEntity> {
    public DeterminationBehaviour() {
        super(ImmutableMap.of(MemoryModuleType.PLAY_DEAD_TICKS, MemoryStatus.VALUE_PRESENT, MemoryModuleType.HURT_BY_ENTITY, MemoryStatus.VALUE_PRESENT), 200);
    }

    protected boolean checkExtraStartConditions(ServerLevel level, AjolotodonEntity ajolotodon) {
        return ajolotodon.isInWaterOrBubble();
    }

    protected boolean canStillUse(ServerLevel level, AjolotodonEntity ajolotodon, long l) {
        return ajolotodon.isInWaterOrBubble() && ajolotodon.getBrain().hasMemoryValue(MemoryModuleType.PLAY_DEAD_TICKS);
    }

    protected void start(ServerLevel level, AjolotodonEntity ajolotodon, long l) {
        ajolotodon.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0));
    }
}
