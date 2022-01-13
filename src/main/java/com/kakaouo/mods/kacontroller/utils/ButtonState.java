package com.kakaouo.mods.kacontroller.utils;

public enum ButtonState {
    PRESSED, RELEASED;

    public static ButtonState fromButton(short pressed, ControllerButtons button) {
        return (pressed & button.getFlag()) != 0 ? PRESSED : RELEASED;
    }

    public boolean isPressed() {
        return this == PRESSED;
    }
}
