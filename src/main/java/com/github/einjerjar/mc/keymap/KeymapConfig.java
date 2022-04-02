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

    public boolean keyButtonModName = true;
    public boolean keyButtonTrim = false;
    public boolean malilibSupport = true;
    public boolean keyButtonMalilibKeybinds = true;

    // public boolean wrapTooltip = false;
    // public int wrapTooltipWidth = 300;

    @ConfigEntry.Gui.Tooltip()
    public boolean debug = false;
}
