package com.github.einjerjar.mc.keymap.keys;

import com.github.einjerjar.mc.keymap.KeyLayoutConfig;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.util.InputUtil;

public class KeyboardLayout {
    @Accessors(fluent = true, chain = true)
    public static class KeyboardKey {
        @Getter protected String text;
        @Getter protected int extraWidth = 0;
        @Getter protected int extraHeight = 0;
        @Getter protected boolean enabled = true;
        @Getter protected int keyCode = 0;
        @Getter protected InputUtil.Key key;
        @Getter protected InputUtil.Type type;

        public KeyboardKey(KeyLayoutConfig.BasicKeyData data) {
            init(
                data.text,
                data.keyCode,
                data.extraWidth,
                data.extraHeight,
                data.mouse ? InputUtil.Type.MOUSE : InputUtil.Type.KEYSYM,
                data.enabled
            );
        }

        private void init(String text, int keyCode, int extraWidth, int extraHeight, InputUtil.Type type, boolean enabled) {
            this.text = text;
            this.extraWidth = extraWidth;
            this.extraHeight = extraHeight;
            this.enabled = enabled;
            this.keyCode = keyCode;
            this.type = type;
            this.key = type.createFromCode(keyCode);
        }
    }
}
