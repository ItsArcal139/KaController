package com.kakaouo.mods.kacontroller.utils;

public record GamePadButtons(
        ButtonState start, ButtonState back,
        ButtonState leftStick, ButtonState rightStick,
        ButtonState leftShoulder, ButtonState rightShoudler, ButtonState guide,
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
}