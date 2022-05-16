package com.github.einjerjar.mc.keymap.keys.layout;

import com.github.einjerjar.mc.keymap.keys.BasicKeyData;
import net.minecraft.client.util.InputUtil;

import java.util.ArrayList;

public class KeyLayoutDE extends KeyboardLayoutBase {
    public KeyLayoutDE() {
        super("Deutsch", "de", null);
        basic = new ArrayList<>() {{
            add(new ArrayList<>() {{
                add(new BasicKeyData("ESC", InputUtil.GLFW_KEY_ESCAPE, 14));
                add(new BasicKeyData("1", InputUtil.GLFW_KEY_F1, 2));
                add(new BasicKeyData("2", InputUtil.GLFW_KEY_F2, 2));
                add(new BasicKeyData("3", InputUtil.GLFW_KEY_F3, 2));
                add(new BasicKeyData("4", InputUtil.GLFW_KEY_F4, 2));
                add(new BasicKeyData("5", InputUtil.GLFW_KEY_F5, 2));
                add(new BasicKeyData("6", InputUtil.GLFW_KEY_F6, 2));
                add(new BasicKeyData("7", InputUtil.GLFW_KEY_F7, 2));
                add(new BasicKeyData("8", InputUtil.GLFW_KEY_F8, 2));
                add(new BasicKeyData("9", InputUtil.GLFW_KEY_F9, 2));
                add(new BasicKeyData("10", InputUtil.GLFW_KEY_F10, 2));
                add(new BasicKeyData("11", InputUtil.GLFW_KEY_F11, 2));
                add(new BasicKeyData("12", InputUtil.GLFW_KEY_F12, 2));
            }});
            add(new ArrayList<>() {{
                add(new BasicKeyData("`", InputUtil.GLFW_KEY_GRAVE_ACCENT));
                add(new BasicKeyData("1", InputUtil.GLFW_KEY_1));
                add(new BasicKeyData("2", InputUtil.GLFW_KEY_2));
                add(new BasicKeyData("3", InputUtil.GLFW_KEY_3));
                add(new BasicKeyData("4", InputUtil.GLFW_KEY_4));
                add(new BasicKeyData("5", InputUtil.GLFW_KEY_5));
                add(new BasicKeyData("6", InputUtil.GLFW_KEY_6));
                add(new BasicKeyData("7", InputUtil.GLFW_KEY_7));
                add(new BasicKeyData("8", InputUtil.GLFW_KEY_8));
                add(new BasicKeyData("9", InputUtil.GLFW_KEY_9));
                add(new BasicKeyData("0", InputUtil.GLFW_KEY_0));
                add(new BasicKeyData("-", InputUtil.GLFW_KEY_MINUS));
                add(new BasicKeyData("=", InputUtil.GLFW_KEY_EQUAL));
                add(new BasicKeyData("BSP", InputUtil.GLFW_KEY_BACKSPACE, 20));
            }});
        }};
    }
}
