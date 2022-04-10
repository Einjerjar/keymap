package com.github.einjerjar.mc.keymap.keys.layout;

import com.github.einjerjar.mc.keymap.keys.BasicKeyData;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true, chain = true)
public abstract class KeyboardLayoutBase {
    @Getter protected static List<List<BasicKeyData>> keys = new ArrayList<>();
    @Getter protected static List<List<BasicKeyData>> extra = new ArrayList<>();
    @Getter protected static List<List<BasicKeyData>> mouse = new ArrayList<>();
    @Getter protected static List<List<BasicKeyData>> numpad = new ArrayList<>();
    @Getter protected static List<KeyboardLayoutBase> layouts;
    @Getter protected String name;

    public KeyboardLayoutBase(String name) {
        this.name = name;
        layouts.add(this);
    }
}
