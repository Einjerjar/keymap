package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.screen.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        // return parent -> AutoConfig.getConfigScreen(KeymapConfig.class, parent).get();
        return ConfigScreen::get;
    }
}
