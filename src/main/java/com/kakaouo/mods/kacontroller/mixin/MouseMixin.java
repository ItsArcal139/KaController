package com.kakaouo.mods.kacontroller.mixin;

import com.kakaouo.mods.kacontroller.utils.GamePad;
import com.kakaouo.mods.kacontroller.utils.GamePadManager;
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
    private long lastMillis = -1;

    private boolean lastLeftTriggered = false;
    private boolean lastRightTriggered = false;

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
        var state = GamePadManager.getGamePad(PlayerIndex.ONE).getState();
        if(!state.isConnected()) return;

        long current = System.currentTimeMillis();
        long deltaMillis = current - lastMillis;
        if(lastMillis == -1) deltaMillis = 0;

        float sensitivity = (float)MinecraftClient.getInstance().options.mouseSensitivity;
        float multiplier = deltaMillis / sensitivity;
        var rightThumb = state.getThumbSticks().right();
        var distance = applyDualZone((float)rightThumb.getDistanceToCenter());
        cursorDeltaX += Math.cos(rightThumb.getAngle()) * distance * multiplier;
        cursorDeltaY -= Math.sin(rightThumb.getAngle()) * distance * multiplier;

        if(MinecraftClient.getInstance().currentScreen != null) return;
        var triggers = state.getTriggers();

        if(triggers.right() > 0.01) {
            int action = Math.round(triggers.right());
            boolean clicked = action == 1;
            if(leftButtonClicked != clicked) {
                this.onMouseButton(MinecraftClient.getInstance().getWindow().getHandle(),
                        GLFW.GLFW_MOUSE_BUTTON_LEFT, action, 0);
            }
            lastLeftTriggered = true;
        } else {
            if(lastLeftTriggered) {
                lastLeftTriggered = false;
                if(leftButtonClicked) {
                    this.onMouseButton(MinecraftClient.getInstance().getWindow().getHandle(),
                            GLFW.GLFW_MOUSE_BUTTON_LEFT, 0, 0);
                }
            }
        }

        if(triggers.left() > 0.01) {
            int action = Math.round(triggers.left());
            boolean clicked = action == 1;
            if(rightButtonClicked != clicked) {
                this.onMouseButton(MinecraftClient.getInstance().getWindow().getHandle(),
                        GLFW.GLFW_MOUSE_BUTTON_RIGHT, action, 0);
            }
            lastRightTriggered = true;
        } else {
            if(lastRightTriggered) {
                lastRightTriggered = false;
                if(rightButtonClicked) {
                    this.onMouseButton(MinecraftClient.getInstance().getWindow().getHandle(),
                            GLFW.GLFW_MOUSE_BUTTON_RIGHT, 0, 0);
                }
            }
        }

        lastMillis = current;
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
