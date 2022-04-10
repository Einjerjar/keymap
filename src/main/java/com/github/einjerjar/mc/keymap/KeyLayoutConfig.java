package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.keys.BasicKeyData;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayoutEnglish;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.List;

@Config(name = KeymapMain.MOD_ID + "_keys")
public class KeyLayoutConfig implements ConfigData {

    public List<List<BasicKeyData>> keys = KeyLayoutEnglish.keys();
    public List<List<BasicKeyData>> extra = KeyLayoutEnglish.extra();
    public List<List<BasicKeyData>> mouse = KeyLayoutEnglish.mouse();
    public List<List<BasicKeyData>> numpad = KeyLayoutEnglish.numpad();
}
