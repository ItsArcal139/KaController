package com.kakaouo.mods.kacontroller.utils;

public enum ButtonState {
    PRESSED, RELEASED;

    public static ButtonState fromButton(short pressed, ButtonConstants button) {
        return (pressed & button.getFlag()) != 0 ? PRESSED : RELEASED;
    }
}
