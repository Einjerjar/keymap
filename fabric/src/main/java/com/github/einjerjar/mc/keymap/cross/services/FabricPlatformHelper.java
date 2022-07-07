package com.github.einjerjar.mc.keymap.cross.services;

import com.github.einjerjar.mc.keymap.client.gui.screen.ConfigScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;

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

    @Override public Screen configScreen(Screen parent) {
        return ConfigScreen.getScreen(parent);
    }

    @Override public IKeybindHelper keybindHelper() {
        return new FabricKeybindHelper();
    }

    @Override public ITickHelper tickHelper() {
        return new FabricTickHelper();
    }
}
