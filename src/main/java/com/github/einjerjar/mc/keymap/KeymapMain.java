package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.screen.KeymappingScreen;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class KeymapMain implements ModInitializer {
    public static final String MOD_ID = "keymap";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static KeymapConfig cfg;
    public static boolean malilibSupport = false;

    public static KeyBinding KBOpenKBScreen;

    @Override
    public void onInitialize() {
        LOGGER.info("KeymapInit");

        AutoConfig.register(KeymapConfig.class, JanksonConfigSerializer::new);
        cfg = AutoConfig.getConfigHolder(KeymapConfig.class).getConfig();

        Optional<ModContainer> malilibContainer = FabricLoader.getInstance().getModContainer("malilib");
        if (malilibContainer.isPresent()) {
            ModContainer malilibMod = malilibContainer.get();
            LOGGER.info(String.format("Found malilib version %s, keymap support will be enabled",
                malilibMod.getMetadata().getVersion().getFriendlyString()));
            malilibSupport = true;
        }

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
