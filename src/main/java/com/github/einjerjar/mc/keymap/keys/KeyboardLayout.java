package com.github.einjerjar.mc.keymap.keys;

import com.github.einjerjar.mc.keymap.KeyLayoutConfig;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class KeyboardLayout {
    public static class KeyboardKey {
        public String text;
        public int extraWidth = 0;
        public int extraHeight = 0;
        public boolean enabled = true;
        public int keyCode = 0;
        public InputUtil.Key key;
        public InputUtil.Type type;

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
