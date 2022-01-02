package com.kakaouo.mods.kacontroller.utils;

public record GamePadThumbSticks(StickValue left, StickValue right) {
    public record StickValue(float x, float y) {
        public double getDistanceToCenter() {
            return Math.sqrt(x * x + y * y);
        }

        public double getAngle() {
            return Math.atan2(y, x);
        }
    }
}
