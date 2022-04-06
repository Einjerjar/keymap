package com.github.einjerjar.mc.keymap;

import com.github.einjerjar.mc.keymap.layout.KeyLayoutEnglish;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.List;

@Config(name = KeymapMain.MOD_ID + "_keys")
public class KeyLayoutConfig implements ConfigData {

    public List<List<BasicKeyData>> keys = KeyLayoutEnglish.keys;
    public List<List<BasicKeyData>> extra = KeyLayoutEnglish.extra;
    public List<List<BasicKeyData>> mouse = KeyLayoutEnglish.mouse;
    public List<List<BasicKeyData>> numpad = KeyLayoutEnglish.numpad;

    public static class BasicKeyData {
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
}
