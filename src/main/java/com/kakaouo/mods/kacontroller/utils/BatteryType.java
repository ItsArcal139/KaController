package com.kakaouo.mods.kacontroller.utils;

import com.kakaouo.mods.kacontroller.KaControllerClient;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public enum BatteryType {
    DISCONNECTED,
    WIRED,
    ALKALINE,
    NIMH,
    UNKNOWN;

    private static final Set<Short> warnedType = new HashSet<>();

    public static BatteryType fromRaw(short value) {
        if(value == 0xff) return UNKNOWN;
        for(BatteryType type : values()) {
            if(type.ordinal() == value && type != UNKNOWN) {
                return type;
            }
        }

        if(!warnedType.contains(value)) {
            KaControllerClient.logger.warn("[XInput] Unknown battery type: " + String.format("0x%x", value));
            warnedType.add(value);
        }
        return UNKNOWN;
    }
}

