package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.screen.KeymappingScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class Main implements ModInitializer {
    public static final String MOD_ID = "keymap";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static KeyBinding KBOpenKBScreen;

    @Override
    public void onInitialize() {
        LOGGER.info("KeymapInit");

        KBOpenKBScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.keymap.cfg.open",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_GRAVE_ACCENT,
            "key.keymap.cfg.title"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KBOpenKBScreen.wasPressed()) {
                client.setScreen(new KeymappingScreen());
            }
        });
    }
}
