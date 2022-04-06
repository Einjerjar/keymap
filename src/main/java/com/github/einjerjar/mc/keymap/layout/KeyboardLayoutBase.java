package com.github.einjerjar.mc.keymap.layout;

import com.github.einjerjar.mc.keymap.KeyLayoutConfig;

import java.util.List;

public abstract class KeyboardLayoutBase {
    public static List<KeyboardLayoutBase> layouts;
    public String name;

    public KeyboardLayoutBase(String name) {
        this.name = name;
        layouts.add(this);
    }

    public abstract List<List<KeyLayoutConfig.BasicKeyData>> getKeys();
    public abstract List<List<KeyLayoutConfig.BasicKeyData>> getExtra();
    public abstract List<List<KeyLayoutConfig.BasicKeyData>> getMouse();
    public abstract List<List<KeyLayoutConfig.BasicKeyData>> getNumpad();
}
