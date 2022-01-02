package com.kakaouo.mods.kacontroller.utils;

public enum ButtonConstants {
    DPAD_UP(0x1),
    DPAD_DOWN(0x2),
    DPAD_LEFT(0x4),
    DPAD_RIGHT(0x8),
    START(0x10),
    BACK(0x20),
    LEFT_THUMB(0x40),
    RIGHT_THUMB(0x80),
    LEFT_SHOULDER(0x100),
    RIGHT_SHOULDER(0x200),
    GUIDE(0x400),
    A(0x1000),
    B(0x2000),
    X(0x4000),
    Y(0x8000);

    private int flag;

    ButtonConstants(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }
}
