package com.kakaouo.mods.kacontroller.utils;

public record GamePadThumbSticks(StickValue left, StickValue right) {
    public record StickValue(float x, float y) {}
}
