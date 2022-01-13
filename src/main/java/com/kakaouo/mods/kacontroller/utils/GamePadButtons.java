package com.kakaouo.mods.kacontroller.utils;

import java.util.Map;

public record GamePadButtons(
        ButtonState start, ButtonState back,
        ButtonState leftStick, ButtonState rightStick,
        ButtonState leftShoulder, ButtonState rightShoulder, ButtonState guide,
        ButtonState a, ButtonState b, ButtonState x, ButtonState y) {
    public GamePadButtons(short buttons)  {
        this(
                ButtonState.fromButton(buttons, ControllerButtons.START),
                ButtonState.fromButton(buttons, ControllerButtons.BACK),
                ButtonState.fromButton(buttons, ControllerButtons.LS),
                ButtonState.fromButton(buttons, ControllerButtons.RS),
                ButtonState.fromButton(buttons, ControllerButtons.LB),
                ButtonState.fromButton(buttons, ControllerButtons.RB),
                ButtonState.fromButton(buttons, ControllerButtons.GUIDE),
                ButtonState.fromButton(buttons, ControllerButtons.A),
                ButtonState.fromButton(buttons, ControllerButtons.B),
                ButtonState.fromButton(buttons, ControllerButtons.X),
                ButtonState.fromButton(buttons, ControllerButtons.Y)
        );
    }

    public Map<ControllerButtons, ButtonState> buttons() {
        return Map.ofEntries(
                Map.entry(ControllerButtons.START, start),
                Map.entry(ControllerButtons.BACK, back),
                Map.entry(ControllerButtons.LS, leftStick),
                Map.entry(ControllerButtons.RS, rightStick),
                Map.entry(ControllerButtons.LB, leftShoulder),
                Map.entry(ControllerButtons.RB, rightShoulder),
                Map.entry(ControllerButtons.GUIDE, guide),
                Map.entry(ControllerButtons.A, a),
                Map.entry(ControllerButtons.B, b),
                Map.entry(ControllerButtons.X, x),
                Map.entry(ControllerButtons.Y, y)
        );
    }

    public Map<ControllerButtons, ButtonState> pressedButtons() {
        return buttons().entrySet().stream()
                .filter(entry -> entry.getValue().isPressed())
                .collect(KakaUtils.mapCollector());
    }
}