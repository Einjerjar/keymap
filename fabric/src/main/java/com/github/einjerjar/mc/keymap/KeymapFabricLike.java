package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.client.gui.screen.ConfigScreen;
import com.github.einjerjar.mc.keymap.client.gui.screen.ConfigScreenShared;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.cross.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class KeymapFabricLike implements ClientModInitializer {
    public static File configDirProvider() {
        return new File(FabricLoader.getInstance().getConfigDir().resolve("keymap.json").toUri());
    }

    @Override public void onInitializeClient() {
        KeymapConfig.configDirProvider(KeymapFabricLike::configDirProvider);
        ConfigScreenShared.provider(ConfigScreen::getScreen);
        CrossKeybindShared.provider(CrossKeybind::execute);
        TickEventRegistrarShared.provider(TickEventRegistrar::execute);
        IntegrationRegistrarShared.provider(IntegrationRegistrar::execute);

        Keymap.init();
    }
}
