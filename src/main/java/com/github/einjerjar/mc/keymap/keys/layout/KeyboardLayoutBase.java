package com.github.einjerjar.mc.keymap.keys.layout;

import com.github.einjerjar.mc.keymap.keys.BasicKeyData;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Accessors(fluent = true, chain = true)
public class KeyboardLayoutBase {
    @Getter protected static HashMap<String, KeyboardLayoutBase> layouts = new HashMap<>();
    @Getter protected List<List<BasicKeyData>> basic = new ArrayList<>();
    @Getter protected List<List<BasicKeyData>> extra = new ArrayList<>();
    @Getter protected List<List<BasicKeyData>> mouse = new ArrayList<>();
    @Getter protected List<List<BasicKeyData>> numpad = new ArrayList<>();
    @Getter protected String name;
    @Getter protected String code;

    public KeyboardLayoutBase(String name, String code) {
        this.name = name;
        this.code = code;
        layouts.put(code, this);
    }

    @Nullable
    public static KeyboardLayoutBase layoutWithCode(String code) {
        return layouts.get(code);
    }
}
