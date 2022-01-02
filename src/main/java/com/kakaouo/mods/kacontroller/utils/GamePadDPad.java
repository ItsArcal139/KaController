package com.kakaouo.mods.kacontroller.utils;

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
}
