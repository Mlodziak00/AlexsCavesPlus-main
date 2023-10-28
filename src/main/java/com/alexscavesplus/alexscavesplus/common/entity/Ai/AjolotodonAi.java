package com.alexscavesplus.alexscavesplus.common.entity.Ai;

import com.alexscavesplus.alexscavesplus.common.entity.AjolotodonEntity;
import com.alexscavesplus.alexscavesplus.common.reg.ACPEntityType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.animal.axolotl.ValidatePlayDead;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class AjolotodonAi {
    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
    private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 0.2F;
    private static final float SPEED_MULTIPLIER_ON_LAND = 0.15F;
    private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 0.5F;
    private static final float SPEED_MULTIPLIER_WHEN_CHASING_IN_WATER = 0.6F;
    private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT_IN_WATER = 0.6F;

    public static Brain<?> makeBrain(Brain<AjolotodonEntity> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        initPlayDeadActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initPlayDeadActivity(Brain<AjolotodonEntity> pBrain) {
        pBrain.addActivityAndRemoveMemoriesWhenStopped(Activity.PLAY_DEAD, ImmutableList.of(Pair.of(0, new DeterminationBehaviour()), Pair.of(1, EraseMemoryIf.create(BehaviorUtils::isBreeding, MemoryModuleType.PLAY_DEAD_TICKS))), ImmutableSet.of(Pair.of(MemoryModuleType.PLAY_DEAD_TICKS, MemoryStatus.VALUE_PRESENT)), ImmutableSet.of(MemoryModuleType.PLAY_DEAD_TICKS));
    }

    private static void initFightActivity(Brain<AjolotodonEntity> pBrain) {
        pBrain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 0, ImmutableList.of(StopAttackingIfTargetInvalid.create(AjolotodonEntity::onStopAttacking), SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(AjolotodonAi::getSpeedModifierChasing), MeleeAttack.create(20), EraseMemoryIf.<Mob>create(BehaviorUtils::isBreeding, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET);
    }

    private static void initCoreActivity(Brain<AjolotodonEntity> pBrain) {
        pBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), ValidatePlayDead.create(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
    }

    private static void initIdleActivity(Brain<AjolotodonEntity> pBrain) {
        pBrain.addActivity(Activity.IDLE, ImmutableList.of(Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), Pair.of(1, new AnimalMakeLove(ACPEntityType.AJOLTODON.get(), 0.2F)), Pair.of(2, new RunOne<>(ImmutableList.of(Pair.of(new FollowTemptation(AjolotodonAi::getSpeedModifier), 1), Pair.of(BabyFollowAdult.create(ADULT_FOLLOW_RANGE, AjolotodonAi::getSpeedModifierFollowingAdult), 1)))), Pair.of(3, StartAttacking.create(AjolotodonAi::findNearestValidAttackTarget)), Pair.of(3, TryFindWater.create(6, 0.15F)), Pair.of(4, new GateBehavior<>(ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, ImmutableList.of(Pair.of(RandomStroll.swim(0.5F), 2), Pair.of(RandomStroll.stroll(0.15F, false), 2), Pair.of(SetWalkTargetFromLookTarget.create(AjolotodonAi::canSetWalkTargetFromLookTarget, AjolotodonAi::getSpeedModifier, 3), 3), Pair.of(BehaviorBuilder.triggerIf(Entity::isInWaterOrBubble), 5), Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), 5))))));
    }

    private static boolean canSetWalkTargetFromLookTarget(LivingEntity p_182381_) {
        Level level = p_182381_.level();
        Optional<PositionTracker> optional = p_182381_.getBrain().getMemory(MemoryModuleType.LOOK_TARGET);
        if (optional.isPresent()) {
            BlockPos blockpos = optional.get().currentBlockPosition();
            return level.isWaterAt(blockpos) == p_182381_.isInWaterOrBubble();
        } else {
            return false;
        }
    }

    public static void updateActivity(AjolotodonEntity ajolotodon) {
        Brain<AjolotodonEntity> brain = ajolotodon.getBrain();
        Activity activity = brain.getActiveNonCoreActivity().orElse((Activity)null);
        if (activity != Activity.PLAY_DEAD) {
            brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.PLAY_DEAD, Activity.FIGHT, Activity.IDLE));
            if (activity == Activity.FIGHT && brain.getActiveNonCoreActivity().orElse((Activity)null) != Activity.FIGHT) {
                brain.setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 2400L);
            }
        }

    }

    private static float getSpeedModifierChasing(LivingEntity p_149289_) {
        return p_149289_.isInWaterOrBubble() ? 0.6F : 0.15F;
    }

    private static float getSpeedModifierFollowingAdult(LivingEntity p_149295_) {
        return p_149295_.isInWaterOrBubble() ? 0.6F : 0.15F;
    }

    private static float getSpeedModifier(LivingEntity p_149301_) {
        return p_149301_.isInWaterOrBubble() ? 0.5F : 0.15F;
    }

    private static Optional<? extends LivingEntity> findNearestValidAttackTarget(AjolotodonEntity ajolotodon) {
        return BehaviorUtils.isBreeding(ajolotodon) ? Optional.empty() : ajolotodon.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
    }

    public static Ingredient getTemptations() {
        return Ingredient.of(ItemTags.AXOLOTL_TEMPT_ITEMS);
    }
}