package com.kakaouo.mods.kacontroller.mixin;

import com.kakaouo.mods.kacontroller.utils.GamePad;
import com.kakaouo.mods.kacontroller.utils.GamePadThumbSticks;
import com.kakaouo.mods.kacontroller.utils.PlayerIndex;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Shadow private double cursorDeltaX;

    @Shadow private double cursorDeltaY;

    @Shadow private boolean leftButtonClicked;

    @Shadow private boolean rightButtonClicked;

    @Shadow protected abstract void onMouseButton(long window, int button, int action, int mods);

    @Inject(method = "updateMouse",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/client/Mouse;client:Lnet/minecraft/client/MinecraftClient;",
                    ordinal = 6))
    public void updateMouse(CallbackInfo ci) {
        var state = GamePad.getState(PlayerIndex.ONE);
        if(!state.isConnected()) return;

        float multiplier = 20;
        var rightThumb = applyDualZone(state.getThumbSticks().right());
        cursorDeltaX += rightThumb.x() * multiplier;
        cursorDeltaY -= rightThumb.y() * multiplier;

        if(MinecraftClient.getInstance().currentScreen != null) return;
        var triggers = state.getTriggers();

        if(triggers.right() > 0.01) {
            int action = Math.round(triggers.right());
            boolean clicked = action == 1;
            if(leftButtonClicked != clicked) {
                this.onMouseButton(MinecraftClient.getInstance().getWindow().getHandle(),
                        GLFW.GLFW_MOUSE_BUTTON_LEFT, action, 0);
            }
        }

        if(triggers.left() > 0.01) {
            int action = Math.round(triggers.left());
            boolean clicked = action == 1;
            if(rightButtonClicked != clicked) {
                this.onMouseButton(MinecraftClient.getInstance().getWindow().getHandle(),
                        GLFW.GLFW_MOUSE_BUTTON_RIGHT, action, 0);
            }
        }
    }

    private float applyDualZone(float value) {
        float signum = Math.signum(value);
        value = Math.abs(value);

        float threshold = 0.875f;
        if(value > threshold) {
            value -= threshold;
            value *= (1 / (1 - threshold)) * (2 - threshold);
            value += threshold;
        }

        return value * signum;
    }

    private GamePadThumbSticks.StickValue applyDualZone(GamePadThumbSticks.StickValue value) {
        return new GamePadThumbSticks.StickValue(
                applyDualZone(value.x()),
                applyDualZone(value.y())
        );
    }
}
