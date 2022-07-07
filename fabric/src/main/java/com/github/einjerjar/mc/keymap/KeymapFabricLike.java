package com.github.einjerjar.mc.keymap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class KeymapFabricLike implements ClientModInitializer {
    public static File configDirProvider(String name) {
        return new File(FabricLoader.getInstance().getConfigDir().resolve(name).toUri());
    }

    @Override public void onInitializeClient() {
        Keymap.init();
    }
}
