package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.keys.layout.KeyLayoutLoader;
import com.github.einjerjar.mc.keymap.keys.layout.KeyboardLayoutBase;
import com.github.einjerjar.mc.keymap.screen.KeyMappingScreen;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
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
@Accessors(fluent = true, chain = true)
public class KeymapMain implements ModInitializer {
    @Getter protected static final String MOD_ID = "keymap";
    @Getter protected static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    @Getter protected static KeymapConfig cfg;
    @Getter protected static boolean malilibAvailable = false;
    @Getter @Setter protected static KeyboardLayoutBase keys;

    protected static KeyBinding KBOpenKBScreen;

    public static void reloadLayouts(String code) {
        KeyboardLayoutBase kb = KeyboardLayoutBase.layoutWithCode(code);
        KeymapMain.LOGGER().info("Loaded [{} :: {}]", kb.name(), kb.code());
        KeymapMain.keys(kb);
    }

    public static void reloadLayouts() {
        reloadLayouts("en_us");
    }

    @Override
    public void onInitialize() {
        KeymapMain.LOGGER().info("Keymap Init");

        new KeyLayoutLoader().loadLayouts();
        KeymapConfig.load();
        cfg = KeymapConfig.instance();

        if (cfg.malilibSupport) {
            Optional<ModContainer> malilibContainer = FabricLoader.getInstance().getModContainer("malilib");
            if (malilibContainer.isPresent()) {
                ModContainer malilibMod = malilibContainer.get();
                KeymapMain.LOGGER().info(String.format("Found malilib version %s, keymap support will be enabled",
                    malilibMod.getMetadata().getVersion().getFriendlyString()));
                malilibAvailable = true;
            }
        }

        KBOpenKBScreen = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.keymap.cfg.open",
            InputUtil.Type.KEYSYM,
            InputUtil.GLFW_KEY_GRAVE_ACCENT,
            "key.keymap.cfg.title"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (KBOpenKBScreen.wasPressed()) {
                client.setScreen(new KeyMappingScreen());
            }
        });
    }
}
