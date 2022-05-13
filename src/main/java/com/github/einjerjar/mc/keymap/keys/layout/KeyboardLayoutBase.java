package com.github.einjerjar.mc.keymap.keys.layout;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.BasicKeyData;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
    @Getter protected final static String default_code = "en_us";
    @Getter @Setter protected static KeyboardLayoutBase default_layout;

    public KeyboardLayoutBase(String name, String code) {
        this.name = name;
        this.code = code;
        layouts.put(code, this);
    }

    public static KeyboardLayoutBase layoutWithCode(String code) {
        code = code.toLowerCase();
        // KeymapMain.LOGGER().info("trying to load key layout for [{}]", code);
        if (layouts.containsKey(code)) {
            return layouts.get(code);
        }
        KeymapMain.LOGGER().warn("key layout for [{}] was not found, using en_us", code);
        return layouts.get("en_us");
    }
}
