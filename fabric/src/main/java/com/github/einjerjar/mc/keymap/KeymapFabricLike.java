package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.client.gui.screen.ConfigScreen;
import com.github.einjerjar.mc.keymap.client.gui.screen.ConfigScreenShared;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.cross.*;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.gameevent.GameEvent;

import java.io.File;

public class KeymapFabricLike implements ClientModInitializer {
    public static File configDirProvider(String name) {
        return new File(FabricLoader.getInstance().getConfigDir().resolve(name).toUri());
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
