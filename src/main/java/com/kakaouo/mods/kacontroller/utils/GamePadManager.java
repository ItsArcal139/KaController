package com.kakaouo.mods.kacontroller.utils;

import com.kakaouo.mods.kacontroller.utils.GamePad;
import com.kakaouo.mods.kacontroller.utils.PlayerIndex;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public enum GamePadManager {
    ;

    private static final GamePad[] gamepads = new GamePad[] {
            new GamePad(PlayerIndex.ONE),
            new GamePad(PlayerIndex.TWO),
            new GamePad(PlayerIndex.THREE),
            new GamePad(PlayerIndex.FOUR)
    };

    public static GamePad getGamePad(PlayerIndex index) {
        return gamepads[index.ordinal()];
    }

    public static Collection<GamePad> getGamePads() {
        return Arrays.stream(gamepads).toList();
    }

    public static void pollInputs() {
        for(GamePad pad : gamepads) {
            pad.pollInput();
        }
    }
}
