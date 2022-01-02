package com.kakaouo.mods.kacontroller.utils;

public class GamePadState {
    private boolean isConnected;
    private int packetNumber;
    private GamePadButtons buttons;
    private GamePadDPad dPad;
    private GamePadThumbSticks thumbSticks;
    private GamePadTriggers triggers;

    public GamePadState(boolean isConnected, XInputNative.RawGamePadState rawState, GamePadDeadZone deadZone) {
        this.isConnected = isConnected;

        if (!isConnected) {
            rawState = XInputNative.RawGamePadState.zero();
        }

        XInputNative.RawGamePad pad = rawState.gamepad();
        packetNumber = rawState.packetNumber();
        buttons = new GamePadButtons(pad.buttons());
        dPad = new GamePadDPad(pad.buttons());
        thumbSticks = new GamePadThumbSticks(
                GamePadDeadZone.applyLeftStickDeadZone(pad.thumbLX(), pad.thumbLY(), deadZone),
                GamePadDeadZone.applyRightStickDeadZone(pad.thumbRX(), pad.thumbRY(), deadZone)
        );
        triggers = new GamePadTriggers(
                GamePadDeadZone.applyTriggerDeadZone(pad.leftTrigger(), deadZone),
                GamePadDeadZone.applyTriggerDeadZone(pad.rightTrigger(), deadZone)
        );
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public int getPacketNumber() {
        return this.packetNumber;
    }

    public GamePadButtons getButtons() {
        return this.buttons;
    }

    public GamePadDPad getDPad() {
        return this.dPad;
    }

    public GamePadThumbSticks getThumbSticks() {
        return this.thumbSticks;
    }

    public GamePadTriggers getTriggers() {
        return this.triggers;
    }

    @Override
    public String toString() {
        return "GamePadState{" +
                "isConnected=" + isConnected +
                ", buttons=" + buttons +
                ", dPad=" + dPad +
                ", thumbSticks=" + thumbSticks +
                ", triggers=" + triggers +
                '}';
    }
}
