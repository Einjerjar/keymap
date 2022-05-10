package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.keys.BasicKeyData;
import com.github.einjerjar.mc.keymap.keys.layout.KeyboardLayoutBase;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.List;

@Config(name = KeymapMain.MOD_ID + "_keys")
public class KeyLayoutConfig implements ConfigData {
    public List<List<BasicKeyData>> keys = KeyboardLayoutBase.layoutWithCode("en_us").basic();
    public List<List<BasicKeyData>> extra = KeyboardLayoutBase.layoutWithCode("en_us").extra();
    public List<List<BasicKeyData>> mouse = KeyboardLayoutBase.layoutWithCode("en_us").mouse();
    public List<List<BasicKeyData>> numpad = KeyboardLayoutBase.layoutWithCode("en_us").numpad();
}
