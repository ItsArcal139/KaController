package com.kakaouo.mods.kacontroller;

import com.kakaouo.mods.kacontroller.utils.PlayerIndex;
import com.kakaouo.mods.kacontroller.utils.XInputNative;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class KaControllerClient implements ClientModInitializer {
    public static Logger logger = LogManager.getLogger("KaControllerClient");

    @Override
    public void onInitializeClient() {
        XInputNative.load();
    }
}
