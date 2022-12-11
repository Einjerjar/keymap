package com.github.einjerjar.mc.keymapfabric;

import com.github.einjerjar.mc.keymap.client.gui.screen.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screens.Screen;

public class ModMenuIntegration implements ModMenuApi {
    @Override public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::configScreen;
    }

    private Screen configScreen(Screen parent) {
        return new ConfigScreen(parent);
    }
}
