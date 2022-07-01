package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.client.gui.screen.ConfigScreenShared;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuIntegration implements ModMenuApi {
    @Override public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigScreenShared.provider()::apply;
    }
}
