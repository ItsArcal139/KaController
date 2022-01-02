package com.kakaouo.mods.kacontroller.utils;

public class NativeHelper {
    public static boolean isEnum(Object obj) {
        return Enum.class.isAssignableFrom(obj.getClass());
    }

    public static XInputNative.RawGamePad createGamePad(
            short buttons, byte leftTrigger, byte rightTrigger,
            short thumbLX, short thumbLY,
            short thumbRX, short thumbRY) {
        return new XInputNative.RawGamePad(buttons, leftTrigger, rightTrigger, thumbLX, thumbLY, thumbRX, thumbRY);
    }

    public static XInputNative.RawGamePadState createGamePadState(
            int packetNumber, XInputNative.RawGamePad gamePad) {
        return new XInputNative.RawGamePadState(packetNumber, gamePad);
    }

    public static XInputNative.StateResult createStateResult(int result, XInputNative.RawGamePadState state) {
        return new XInputNative.StateResult(result, state);
    }

    public static XInputNative.BatteryInfoResult createBatteryInfoResult(int result, short batteryType, short batteryLevel) {
        return new XInputNative.BatteryInfoResult(result, batteryType, batteryLevel);
    }
}
