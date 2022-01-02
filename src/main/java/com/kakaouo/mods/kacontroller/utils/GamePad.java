package com.kakaouo.mods.kacontroller.utils;

import com.kakaouo.mods.kacontroller.KaControllerClient;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GamePad {
    private PlayerIndex index;
    private GamePadState state = new GamePadState(false, XInputNative.RawGamePadState.zero(), GamePadDeadZone.NONE);
    private List<Consumer<String>> buttonDownListeners = new ArrayList<>();
    private List<Consumer<String>> buttonUpListeners = new ArrayList<>();

    GamePad(PlayerIndex index) {
        this.index = index;
    }

    public void pollInput() {
        if(!XInputNative.isSupported()) return;
        XInputNative.StateResult result = XInputNative.internalGetState(index);
        var oldState = this.state;
        this.state = new GamePadState(result.result() == 0, result.state(), GamePadDeadZone.INDEPENDENT_AXES);

        this.state.getAllPressedButtonStates().forEach((name, state) -> {
            if(!oldState.getAllPressedButtonStates().containsKey(name)) {
                buttonDownListeners.forEach(consumer -> {
                    consumer.accept(name);
                });
            }
        });

        oldState.getAllPressedButtonStates().forEach((name, state) -> {
            if(!this.state.getAllPressedButtonStates().containsKey(name)) {
                buttonUpListeners.forEach(consumer -> {
                    consumer.accept(name);
                });
            }
        });
    }

    public void addButtonDownListener(Consumer<String> listener) {
        buttonDownListeners.add(listener);
    }

    public void addButtonUpListener(Consumer<String> listener) {
        buttonUpListeners.add(listener);
    }

    public GamePadState getState() {
        return this.state;
    }

    public void setVibration(float leftMotor, float rightMotor) {
        if(!XInputNative.isSupported()) return;
        XInputNative.internalSetState(index, leftMotor, rightMotor);
    }

    public GamePadBatteryInfo getBatteryInfo() {
        if(!XInputNative.isSupported()) return GamePadBatteryInfo.disconnected();
        XInputNative.BatteryInfoResult result = XInputNative.internalGetBatteryInfo(index);
        if(result.result() != 0) {
            return GamePadBatteryInfo.disconnected();
        }

        return new GamePadBatteryInfo(
                BatteryType.fromRaw(result.batteryType()),
                BatteryLevel.fromRaw(result.batteryLevel()));
    }

    public boolean isConnected() {
        return state.isConnected();
    }
}
