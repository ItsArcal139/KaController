package com.kakaouo.mods.kacontroller.utils;

public enum ControllerButtons {
    DPAD_UP(0x1, "Up"),
    DPAD_DOWN(0x2, "Down"),
    DPAD_LEFT(0x4, "Left"),
    DPAD_RIGHT(0x8, "Right"),
    START(0x10, "Start"),
    BACK(0x20, "Back"),
    LS(0x40, "LS"),
    RS(0x80, "RS"),
    LB(0x100, "LB"),
    RB(0x200, "RB"),
    GUIDE(0x400, "Guide"),
    A(0x1000, "A"),
    B(0x2000, "B"),
    X(0x4000, "X"),
    Y(0x8000, "Y");

    private int flag;
    private String name;

    ControllerButtons(int flag, String name) {
        this.flag = flag;
        this.name = name;
    }

    public int getFlag() {
        return flag;
    }

    public String getName() {
        return name;
    }
}
