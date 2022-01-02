package com.kakaouo.mods.kacontroller.utils;

import java.util.Map;
import java.util.stream.Collectors;

public record GamePadButtons(
        ButtonState start, ButtonState back,
        ButtonState leftStick, ButtonState rightStick,
        ButtonState leftShoulder, ButtonState rightShoulder, ButtonState guide,
        ButtonState a, ButtonState b, ButtonState x, ButtonState y) {
    public GamePadButtons(short buttons)  {
        this(
                ButtonState.fromButton(buttons, ButtonConstants.START),
                ButtonState.fromButton(buttons, ButtonConstants.BACK),
                ButtonState.fromButton(buttons, ButtonConstants.LEFT_THUMB),
                ButtonState.fromButton(buttons, ButtonConstants.RIGHT_THUMB),
                ButtonState.fromButton(buttons, ButtonConstants.LEFT_SHOULDER),
                ButtonState.fromButton(buttons, ButtonConstants.RIGHT_SHOULDER),
                ButtonState.fromButton(buttons, ButtonConstants.GUIDE),
                ButtonState.fromButton(buttons, ButtonConstants.A),
                ButtonState.fromButton(buttons, ButtonConstants.B),
                ButtonState.fromButton(buttons, ButtonConstants.X),
                ButtonState.fromButton(buttons, ButtonConstants.Y)
        );
    }

    public Map<String, ButtonState> buttons() {
        return Map.ofEntries(
                Map.entry("Start", start),
                Map.entry("Back", back),
                Map.entry("LS", leftStick),
                Map.entry("RS", rightStick),
                Map.entry("LB", leftShoulder),
                Map.entry("RB", rightShoulder),
                Map.entry("Guide", guide),
                Map.entry("A", a),
                Map.entry("B", b),
                Map.entry("X", x),
                Map.entry("Y", y)
        );
    }

    public Map<String, ButtonState> pressedButtons() {
        return buttons().entrySet().stream()
                .filter(entry -> entry.getValue().isPressed())
                .collect(KakaUtils.mapCollector());
    }
}