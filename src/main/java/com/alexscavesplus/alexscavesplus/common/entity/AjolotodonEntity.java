package com.alexscavesplus.alexscavesplus.common.entity;

import com.alexscavesplus.alexscavesplus.common.reg.ACPEntityType;
import com.alexscavesplus.alexscavesplus.common.reg.ACPSoundEvents;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Map;

public class AjolotodonEntity extends Animal implements GeoEntity, LerpingModel {
    private boolean isSpin;

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RandomSwimmingGoal(this, 8, 5));
    }

    @Nullable
    protected BlockPos jukebox;
    private final Map<String, Vector3f> modelRotationValues = Maps.newHashMap();
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public AjolotodonEntity(EntityType<? extends AjolotodonEntity> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new AjolotodonEntity.AxolotlMoveControl(this);
        this.lookControl = new AjolotodonEntity.AxolotlLookControl(this, 20);
        this.setMaxUpStep(1.0F);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new AmphibiousPathNavigation(this, level);
    }
    @Override
    public Map<String, Vector3f> getModelRotationValues() {
        return this.modelRotationValues;
    }

    @Override
    public float getWalkTargetValue(BlockPos pPos) {
        return 0.0F;
    }


    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 34.0f)
                .add(Attributes.FOLLOW_RANGE, 20.0f)
                .add(Attributes.ATTACK_DAMAGE, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 1.0f);
    }
    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();
        if (!this.isNoAi()) {
            this.handleAirSupply(i);
        }
    }

    protected void handleAirSupply(int pAirSupply) {
        if (this.isAlive() && !this.isInWaterRainOrBubble()) {
            this.setAirSupply(pAirSupply - 1);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(this.damageSources().dryOut(), 2.0F);
            }
        } else {
            this.setAirSupply(this.getMaxAirSupply());
        }
    }

    public int getMaxAirSupply() {
        return 4800;
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }


    public boolean isFood(ItemStack stack){
        return stack.is(ItemTags.FISHES);
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return true;
    }
    static class AxolotlLookControl extends SmoothSwimmingLookControl {
        public AxolotlLookControl(AjolotodonEntity ajolotodon, int pMaxYRotFromCenter) {
            super(ajolotodon, pMaxYRotFromCenter);
        }

        public void tick() {
            super.tick();
        }
    }

    /*@Override
    public void travel(Vec3 pTravelVector) {
        if (this.isControlledByLocalInstance() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(pTravelVector);
        }

    }*/
    static class AxolotlMoveControl extends SmoothSwimmingMoveControl {
        private final AjolotodonEntity ajolotodon;

        public AxolotlMoveControl(AjolotodonEntity ajolotodon) {
            super(ajolotodon, 85, 10, 0.1F, 0.5F, false);
            this.ajolotodon = ajolotodon;
        }

        public void tick() {
            super.tick();
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean flag = entity.hurt(damageSources().mobAttack(this), (float)((int)this.getAttributeValue(Attributes.ATTACK_DAMAGE)));
        if (flag) {
            this.doEnchantDamageEffects(this,entity);
            this.playSound(ACPSoundEvents.AJOLOTODON_ATTACK.get(), 1.0F, 1.0F);
        }
        return flag;
    }

    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ACPSoundEvents.AJOLOTODON_HURT.get();
    }

    @Nullable
    protected SoundEvent getDeathSound() {
        return ACPSoundEvents.AJOLOTODON_DEATH.get();
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? ACPSoundEvents.AJOLOTODON_CHIRP.get() : ACPSoundEvents.AJOLOTODON_CHIRP_AIR.get();
    }

    @Override
    public void playAmbientSound() {
        super.playAmbientSound();
    }

    protected @NotNull SoundEvent getSwimSplashSound() {
        return SoundEvents.AXOLOTL_SPLASH;
    }

    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.AXOLOTL_SWIM;
    }
    public static void onStopAttacking(AjolotodonEntity ajolotodon, LivingEntity pTarget) {
        if (pTarget.isDeadOrDying()) {
            DamageSource damagesource = pTarget.getLastDamageSource();
            if (damagesource != null) {
                ajolotodon.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 0, false, false));

            }
        }
    }



    @Override
    public int getMaxHeadXRot() {
        return 1;
    }

    @Override
    public int getMaxHeadYRot() {
        return 0;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return ACPEntityType.AJOLTODON.get().create(serverLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        if (this.isSpin){
            controllerRegistrar.add(new AnimationController<>(this, "animcontroller", 0, this::predicate));
        } else {
            controllerRegistrar.add(new AnimationController<>(this, "animcontroller", 20, this::predicate));
        }
    }

    @Override
    public void aiStep() {
        if (this.jukebox == null || !this.jukebox.closerToCenterThan(this.position(), 3.46D) || !this.level().getBlockState(this.jukebox).is(Blocks.JUKEBOX)) {
            this.isSpin = false;
            this.jukebox = null;
        }
        super.aiStep();
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        if (tAnimationState.isMoving() && this.isInWater()){
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.ajolotodon.swim", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        if (this.isSpin()){
            tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.ajolotodon.spin", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        }
        tAnimationState.getController().setAnimation(RawAnimation.begin().then("animation.ajolotodon.idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    public void setRecordPlayingNearby(BlockPos pPos, boolean pIsSpin){
        this.jukebox = pPos;
        this.isSpin = pIsSpin;
    }

    public boolean isSpin(){
        return this.isSpin;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public boolean canSwimInFluidType(FluidType type) {
        return super.canSwimInFluidType(type);
    }

}
