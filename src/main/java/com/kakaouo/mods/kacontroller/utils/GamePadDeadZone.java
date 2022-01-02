package com.kakaouo.mods.kacontroller.utils;

public enum GamePadDeadZone {
    CIRCULAR,
    INDEPENDENT_AXES,
    NONE;

    public static final int LEFT_STICK_DEADZONE = 7849;
    public static final int RIGHT_STICK_DEADZONE = 8689;
    public static final int TRIGGER_DEADZONE = 30;

    public static float applyTriggerDeadZone(short value, GamePadDeadZone deadZoneMode) {
        if(value < 0) {
            value += 256;
        }

        if(deadZoneMode == NONE) {
            return applyDeadZone(value, 255, 0);
        } else {
            return applyDeadZone(value, 255, TRIGGER_DEADZONE);
        }
    }

    public static GamePadThumbSticks.StickValue applyLeftStickDeadZone(short x, short y, GamePadDeadZone deadZoneMode) {
        return applyStickDeadZone(x, y, deadZoneMode, LEFT_STICK_DEADZONE);
    }

    public static GamePadThumbSticks.StickValue applyRightStickDeadZone(short x, short y, GamePadDeadZone deadZoneMode) {
        return applyStickDeadZone(x, y, deadZoneMode, RIGHT_STICK_DEADZONE);
    }

    private static GamePadThumbSticks.StickValue applyStickDeadZone(short x, short y, GamePadDeadZone deadZoneMode, int deadZoneSize) {
        if(deadZoneMode == CIRCULAR) {
            float distanceFromCenter = (float)Math.sqrt((long)x * (long)x + (long)y * (long)y);
            float coefficient = applyDeadZone(distanceFromCenter, Short.MAX_VALUE, deadZoneSize);
            coefficient = coefficient > 0 ? coefficient / distanceFromCenter : 0;

            return new GamePadThumbSticks.StickValue(
                    clamp(x * coefficient),
                    clamp(y * coefficient)
            );
        } else if(deadZoneMode == INDEPENDENT_AXES) {
            return new GamePadThumbSticks.StickValue(
                    applyDeadZone(x, Short.MAX_VALUE, deadZoneSize),
                    applyDeadZone(y, Short.MAX_VALUE, deadZoneSize)
            );
        } else {
            return new GamePadThumbSticks.StickValue(
                    applyDeadZone(x, Short.MAX_VALUE, 0),
                    applyDeadZone(y, Short.MAX_VALUE, 0)
            );
        }
    }

    private static float clamp(float value) {
        return value < -1 ? -1 : (value > 1 ? 1 : value);
    }

    private static float applyDeadZone(float value, float maxValue, float deadZoneSize) {
        if(value < -deadZoneSize) {
            value += deadZoneSize;
        } else if(value > deadZoneSize) {
            value -= deadZoneSize;
        } else {
            return 0;
        }

        value /= maxValue - deadZoneSize;
        return clamp(value);
    }
}
