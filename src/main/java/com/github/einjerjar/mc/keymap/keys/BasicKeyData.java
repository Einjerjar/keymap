package com.github.einjerjar.mc.keymap.keys;

public class BasicKeyData {
    public String text;
    public int keyCode;
    public int extraWidth;
    public int extraHeight;
    public boolean enabled;
    public boolean mouse;

    public BasicKeyData(String text, int keyCode, int extraWidth, int extraHeight, boolean enabled, boolean mouse) {
        init(text, keyCode, extraWidth, extraHeight, enabled, mouse);
    }

    public BasicKeyData(String text, int keyCode, int extraWidth, int extraHeight) {
        init(text, keyCode, extraWidth, extraHeight, true, false);
    }

    public BasicKeyData(String text, int keyCode, int extraWidth, int extraHeight, boolean enabled) {
        init(text, keyCode, extraWidth, extraHeight, enabled, false);
    }

    public BasicKeyData(String text, int keyCode) {
        init(text, keyCode, 0, 0, true, false);
    }

    public BasicKeyData(String text, int keyCode, boolean enabled) {
        init(text, keyCode, 0, 0, enabled, false);
    }

    public BasicKeyData(String text, int keyCode, boolean enabled, boolean mouse) {
        init(text, keyCode, 0, 0, enabled, mouse);
    }

    public BasicKeyData(String text, int keyCode, int extraWidth) {
        init(text, keyCode, extraWidth, 0, true, false);
    }

    protected void init(String text, int keyCode, int extraWidth, int extraHeight, boolean enabled, boolean mouse) {
        this.text = text;
        this.keyCode = keyCode;
        this.extraWidth = extraWidth;
        this.extraHeight = extraHeight;
        this.enabled = enabled;
        this.mouse = mouse;
    }
}