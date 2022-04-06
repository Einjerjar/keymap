package com.github.einjerjar.mc.keymap;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = KeymapMain.MOD_ID)
public class KeymapConfig implements ConfigData {
    @ConfigEntry.Gui.PrefixText()
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean replaceKeybindScreen = true;
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean malilibSupport = true;

    @ConfigEntry.Gui.PrefixText()
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean keybindNameOnHover = true;
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean keybindKeyOnHover = true;

    @ConfigEntry.Gui.PrefixText()
    @ConfigEntry.Gui.Tooltip()
    public boolean keyButtonModName = true;
    @ConfigEntry.Gui.Tooltip(count = 2)
    public boolean keyButtonMalilibKeybinds = true;

    @ConfigEntry.Gui.PrefixText()
    @ConfigEntry.Gui.Tooltip()
    public boolean debug = false;
}
