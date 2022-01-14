package com.kakaouo.mods.kacontroller.utils;

import com.kakaouo.mods.kacontroller.KaControllerClient;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

public class XInputNative {
    private static boolean supported = false;

    public static boolean isSupported() {
        return supported;
    }

    static {
        ModContainer container = FabricLoader.getInstance().getModContainer("kacontroller")
                .orElseThrow(() -> new AssertionError("Mod ID mismatch?"));

        boolean failedDuringCopy = false;

        File libFolder = new File(FabricLoader.getInstance().getConfigDir().toString(), "../mods/KaController/");
        if(!libFolder.exists()) {
            failedDuringCopy = !libFolder.mkdir();
        }

        if(failedDuringCopy) {
            KaControllerClient.logger.info("Failed to make a folder at " + libFolder);
        }

        File libFile = new File(libFolder, "KaController.dll");
        if(!libFile.exists() && !failedDuringCopy) {
            InputStream stream = null;
            FileOutputStream output = null;
            try {
                libFile.createNewFile();
                stream = XInputNative.class.getClassLoader().getResourceAsStream("KaController.dll");
                output = new FileOutputStream(libFile);
                output.write(stream.readAllBytes());
            } catch(IOException ex) {
                KaControllerClient.logger.warn("Failed to copy library: " + ex);
                failedDuringCopy = true;
            } catch(Exception ex) {
                KaControllerClient.logger.error("Failed to copy library: " + ex);
                failedDuringCopy = true;
            } finally {
                try {
                    if (output != null) output.close();
                } catch(IOException ex) {
                    // ignored
                }

                try {
                    if(stream != null) stream.close();
                } catch (IOException ex) {
                    // ignored
                }
            }
        }

        if(!failedDuringCopy) {
            String path = libFile.getAbsolutePath();
            KaControllerClient.logger.info("Loading native library from " + path);

            try {
                System.load(path);
                supported = true;
            } catch (UnsatisfiedLinkError error) {
                KaControllerClient.logger.warn("Failed to load library: " + error);
            }
        }
    }

    public record StateResult(int result, RawGamePadState state) {}

    public record BatteryInfoResult(int result, short batteryType, short batteryLevel) {}

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

    public static native BatteryInfoResult internalGetBatteryInfo(PlayerIndex index);

    public static native void internalSetState(PlayerIndex index, float leftMotor, float rightMotor);

    public static void load() {}
}

