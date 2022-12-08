package com.github.einjerjar.mc.keymap.cross.services;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override public String loader() {
        return "fabric";
    }

    @Override public boolean modLoaded(String modid) {
        return FabricLoader.getInstance().isModLoaded(modid);
    }

    @Override public boolean dev() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override public File config(String file) {
        return new File(FabricLoader.getInstance().getConfigDir().resolve(file).toUri());
    }

    @Override public IKeybindHelper keybindHelper() {
        return new FabricKeybindHelper();
    }

    @Override public ITickHelper tickHelper() {
        return new FabricTickHelper();
    }
}
