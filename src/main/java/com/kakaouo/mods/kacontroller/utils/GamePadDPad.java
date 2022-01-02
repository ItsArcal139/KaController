package com.kakaouo.mods.kacontroller.utils;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record GamePadDPad(
        ButtonState up, ButtonState down,
        ButtonState left, ButtonState right) {
    public GamePadDPad(short buttons) {
        this(
                ButtonState.fromButton(buttons, ButtonConstants.DPAD_UP),
                ButtonState.fromButton(buttons, ButtonConstants.DPAD_DOWN),
                ButtonState.fromButton(buttons, ButtonConstants.DPAD_LEFT),
                ButtonState.fromButton(buttons, ButtonConstants.DPAD_RIGHT)
        );
    }

    public Map<String, ButtonState> buttons() {
        return Map.ofEntries(
                Map.entry("Up", up),
                Map.entry("Down", down),
                Map.entry("Left", left),
                Map.entry("Right", right)
        );
    }

    public Map<String, ButtonState> pressedButtons() {
        return buttons().entrySet().stream()
                .filter(entry -> entry.getValue().isPressed())
                .collect(KakaUtils.mapCollector());
    }
}
