package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.keys.BasicKeyData;
import com.github.einjerjar.mc.keymap.keys.layout.KeyboardLayoutBase;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.List;

@Config(name = KeymapMain.MOD_ID + "_keys")
public class KeyLayoutConfig implements ConfigData {
    public List<List<BasicKeyData>> keys = KeyboardLayoutBase.default_layout().basic();
    public List<List<BasicKeyData>> extra = KeyboardLayoutBase.default_layout().extra();
    public List<List<BasicKeyData>> mouse = KeyboardLayoutBase.default_layout().mouse();
    public List<List<BasicKeyData>> numpad = KeyboardLayoutBase.default_layout().numpad();
}
