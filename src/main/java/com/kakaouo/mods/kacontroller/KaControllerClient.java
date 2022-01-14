package com.kakaouo.mods.kacontroller;

import com.kakaouo.mods.kacontroller.utils.GamePad;
import com.kakaouo.mods.kacontroller.utils.GamePadManager;
import com.kakaouo.mods.kacontroller.utils.PlayerIndex;
import com.kakaouo.mods.kacontroller.utils.XInputNative;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.dedicated.gui.PlayerListGui;
import net.minecraft.util.Hand;
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
                case RB -> inv.selectedSlot = ++inv.selectedSlot % 9;
                case LB -> inv.selectedSlot = inv.selectedSlot == 0 ? 8 : --inv.selectedSlot;
                case DPAD_UP -> {
                    if(!player.isSpectator() && player.dropSelectedItem(false)) {
                        player.swingHand(Hand.MAIN_HAND);
                    }
                }
                case DPAD_DOWN -> {
                    Perspective perspective = client.options.getPerspective();
                    client.options.setPerspective(client.options.getPerspective().next());
                    if (perspective.isFirstPerson() != client.options.getPerspective().isFirstPerson()) {
                        client.gameRenderer.onCameraEntitySet(client.options.getPerspective().isFirstPerson() ? client.getCameraEntity() : null);
                    }
                    client.worldRenderer.scheduleTerrainUpdate();
                }
                case BACK -> client.options.keyPlayerList.setPressed(true);
            }
        });

        pad.addButtonUpListener(button -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            if(player == null) return;

            switch (button) {
                case BACK -> client.options.keyPlayerList.setPressed(false);
            }
        });
    }
}
