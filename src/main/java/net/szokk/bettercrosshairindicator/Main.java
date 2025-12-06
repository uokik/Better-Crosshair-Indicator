package net.szokk.bettercrosshairindicator;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.szokk.bettercrosshairindicator.gui.CrosshairConfigScreen;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("bettercrosshairindicator");
    private static KeyBinding configKey;

    @Override
    public void onInitialize() {
        LOGGER.info("BetterCrosshairIndicator Initialized :v");

        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.bettercrosshairindicator.config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.bettercrosshairindicator"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (configKey.wasPressed()) {
                client.setScreen(new CrosshairConfigScreen(client.currentScreen));
            }
        });
    }
}