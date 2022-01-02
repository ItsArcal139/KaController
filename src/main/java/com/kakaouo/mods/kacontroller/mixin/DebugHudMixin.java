package com.kakaouo.mods.kacontroller.mixin;

import com.kakaouo.mods.kacontroller.utils.ButtonState;
import com.kakaouo.mods.kacontroller.utils.GamePad;
import com.kakaouo.mods.kacontroller.utils.GamePadManager;
import com.kakaouo.mods.kacontroller.utils.XInputNative;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.entity.Entity;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DebugHud.class)
public abstract class DebugHudMixin {
    @Inject(method = "getLeftText", at = @At("RETURN"), cancellable = true)
    public void getLeftText(CallbackInfoReturnable<List<String>> cir) {
        List<String> list = cir.getReturnValue();

        list.add("--");
        list.add("[XInput] (provided by KaController)");

        boolean supported = XInputNative.isSupported();
        list.add("Enabled: " + (supported ? Formatting.GREEN + "true" : Formatting.RED + "false"));

        if(supported) {
            int index = 1;
            for(GamePad pad : GamePadManager.getGamePads()) {
                list.add(String.format("Gamepad #%d: %s", index++, pad.isConnected() ? Formatting.GREEN + "Connected" : Formatting.RED + "Disconnected"));
                if(pad.isConnected()) {
                    list.add(" - Battery: " + pad.getBatteryInfo().toString());

                    var state = pad.getState();
                    StringBuilder sb = new StringBuilder();
                    for(String button : state.getAllPressedButtonStates().keySet()) {
                        sb.append(", ").append(button);
                    }
                    String str = sb.length() > 0 ? sb.substring(2) : "<none>";
                    list.add(" - Buttons: " + str);

                    var triggers = state.getTriggers();
                    list.add(String.format(" - Triggers: L: %f, R: %f", triggers.left(), triggers.right()));

                    var sticks = state.getThumbSticks();
                    list.add(String.format(" - Sticks: L: %s, R: %s", sticks.left().toShortString(), sticks.right().toShortString()));
                }
            }
        }

        list.add("--");
    }
}
