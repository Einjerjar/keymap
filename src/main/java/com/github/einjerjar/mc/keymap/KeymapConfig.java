package com.github.einjerjar.mc.keymap;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = KeymapMain.MOD_ID)
public class KeymapConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean replaceKeybindScreen = true;
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean keybindKeyOnHover = true;
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean keybindNameOnHover = true;
}
