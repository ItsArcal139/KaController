package com.kakaouo.mods.kacontroller.utils;

import javax.naming.ldap.Control;
import java.util.Map;

public record GamePadDPad(
        ButtonState up, ButtonState down,
        ButtonState left, ButtonState right) {
    public GamePadDPad(short buttons) {
        this(
                ButtonState.fromButton(buttons, ControllerButtons.DPAD_UP),
                ButtonState.fromButton(buttons, ControllerButtons.DPAD_DOWN),
                ButtonState.fromButton(buttons, ControllerButtons.DPAD_LEFT),
                ButtonState.fromButton(buttons, ControllerButtons.DPAD_RIGHT)
        );
    }

    public Map<ControllerButtons, ButtonState> buttons() {
        return Map.ofEntries(
                Map.entry(ControllerButtons.DPAD_UP, up),
                Map.entry(ControllerButtons.DPAD_DOWN, down),
                Map.entry(ControllerButtons.DPAD_LEFT, left),
                Map.entry(ControllerButtons.DPAD_RIGHT, right)
        );
    }

    public Map<ControllerButtons, ButtonState> pressedButtons() {
        return buttons().entrySet().stream()
                .filter(entry -> entry.getValue().isPressed())
                .collect(KakaUtils.mapCollector());
    }
}
