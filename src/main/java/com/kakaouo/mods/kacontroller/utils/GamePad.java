package com.kakaouo.mods.kacontroller.utils;

public class GamePad {
    public static GamePadState getState(PlayerIndex index) {
        return getState(index, GamePadDeadZone.INDEPENDENT_AXES);
    }

    public static GamePadState getState(PlayerIndex index, GamePadDeadZone deadZone) {
        XInputNative.StateResult result = XInputNative.internalGetState(index);
        return new GamePadState(result.result() == 0, result.state(), deadZone);
    }

    public static void setVibration(PlayerIndex index, float leftMotor, float rightMotor) {
        XInputNative.internalSetState(index, leftMotor, rightMotor);
    }
}
