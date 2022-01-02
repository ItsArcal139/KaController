package com.kakaouo.mods.kacontroller.mixin;

import com.kakaouo.mods.kacontroller.utils.GamePad;
import com.kakaouo.mods.kacontroller.utils.GamePadManager;
import com.kakaouo.mods.kacontroller.utils.PlayerIndex;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntityMixin {
    @Shadow public abstract void setSprinting(boolean sprinting);

    private long lastDamageTime = -1;

    @Inject(method = "damage", at = @At("RETURN"))
    public void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        lastDamageTime = System.currentTimeMillis();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        GamePad pad = GamePadManager.getGamePad(PlayerIndex.ONE);
        var state = pad.getState();

        if(state.getButtons().leftStick().isPressed()) {
            setSprinting(true);
        }

        if(lastDamageTime < 0) return;
        long deltaDamageTime = System.currentTimeMillis() - lastDamageTime;
        float duration = 500;

        if(deltaDamageTime > duration) {
            pad.setVibration(0, 0);
            return;
        }
        float strength = clamp((duration - deltaDamageTime) / duration) * (Math.max(lastDamageTaken, 1) / getMaxHealth() * 1.5f);
        pad.setVibration(strength, strength);
    }

    private float clamp(float value) {
        return Math.max(Math.min(value, 1), 0);
    }
}
