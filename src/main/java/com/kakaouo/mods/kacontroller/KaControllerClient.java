package com.kakaouo.mods.kacontroller;

import com.kakaouo.mods.kacontroller.utils.GamePad;
import com.kakaouo.mods.kacontroller.utils.GamePadManager;
import com.kakaouo.mods.kacontroller.utils.PlayerIndex;
import com.kakaouo.mods.kacontroller.utils.XInputNative;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Environment(EnvType.CLIENT)
public class KaControllerClient implements ClientModInitializer {
    public static Logger logger = LogManager.getLogger("KaControllerClient");

    @Override
    public void onInitializeClient() {
        XInputNative.load();

        GamePad pad = GamePadManager.getGamePad(PlayerIndex.ONE);
        pad.addButtonDownListener(button -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            if(player == null) return;

            PlayerInventory inv = player.getInventory();
            switch (button) {
                case "RB" -> inv.selectedSlot = ++inv.selectedSlot % 9;
                case "LB" -> inv.selectedSlot = inv.selectedSlot == 0 ? 8 : --inv.selectedSlot;
            }
        });
    }
}
