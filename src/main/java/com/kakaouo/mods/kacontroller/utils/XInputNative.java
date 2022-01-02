package com.kakaouo.mods.kacontroller.utils;

import com.kakaouo.mods.kacontroller.KaControllerClient;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.Optional;

public class XInputNative {
    private static boolean supported = false;

    static {
        Optional<ModContainer> container = FabricLoader.getInstance().getModContainer("kacontroller");
        if(container.isEmpty()) {
            throw new AssertionError("Mod ID mismatch?");
        }

        String path = container.get().getPath("KaController.dll").toAbsolutePath().toString();
        KaControllerClient.logger.info("Loading native library from " + path);

        try {
            System.load(path);
            supported = true;
        } catch (UnsatisfiedLinkError error) {
            KaControllerClient.logger.warn("Failed to load library: " + error);
        }
    }

    public record StateResult(int result, RawGamePadState state) {}

    public record RawGamePadState(int packetNumber, RawGamePad gamepad) {
        public static RawGamePadState zero() {
            return new RawGamePadState(0, RawGamePad.zero());
        }
    }

    public record RawGamePad(
            short buttons, byte leftTrigger, byte rightTrigger,
            short thumbLX, short thumbLY,
            short thumbRX, short thumbRY) {
        public static RawGamePad zero() {
            return new RawGamePad(
                    (short)0, (byte)0, (byte)0,
                    (short)0, (short)0, (short)0, (short)0);
        }
    }

    public static native StateResult internalGetState(PlayerIndex index);

    public static native StateResult internalSetState(PlayerIndex index, float leftMotor, float rightMotor);

    public static void load() {}
}

