package com.kakaouo.mods.kacontroller.utils;

import com.kakaouo.mods.kacontroller.KaControllerClient;

import java.util.HashSet;
import java.util.Set;

public enum BatteryLevel {
    EMPTY,
    LOW,
    MEDIUM,
    FULL;

    private static final Set<Short> warnedLevel = new HashSet<>();

    public static BatteryLevel fromRaw(short value) {
        for(BatteryLevel level : values()) {
            if(level.ordinal() == value) {
                return level;
            }
        }

        if(!warnedLevel.contains(value)) {
            KaControllerClient.logger.warn("[XInput] Unknown battery level: " + String.format("0x%x", value));
            warnedLevel.add(value);
        }
        return FULL;
    }
}
