package com.kakaouo.mods.kacontroller.utils;

public record GamePadBatteryInfo(BatteryType type, BatteryLevel level) {
    private static final GamePadBatteryInfo _fallback = new GamePadBatteryInfo(BatteryType.DISCONNECTED, BatteryLevel.EMPTY);

    public static GamePadBatteryInfo disconnected() {
        return _fallback;
    }
}

