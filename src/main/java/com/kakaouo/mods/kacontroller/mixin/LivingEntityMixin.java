package com.kakaouo.mods.kacontroller.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends EntityMixin {
    @Shadow public abstract float getMaxHealth();

    @Shadow protected float lastDamageTaken;
    @Shadow protected int lastAttackedTicks;
}
