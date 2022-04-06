package com.github.einjerjar.mc.keymap.layout;

import com.github.einjerjar.mc.keymap.KeyLayoutConfig;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class KeyLayoutEnglish extends KeyboardLayoutBase {
    public static final List<List<KeyLayoutConfig.BasicKeyData>> keys = new ArrayList<>() {{
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("ESC", InputUtil.GLFW_KEY_ESCAPE, 14));
            add(new KeyLayoutConfig.BasicKeyData("1", InputUtil.GLFW_KEY_F1, 2));
            add(new KeyLayoutConfig.BasicKeyData("2", InputUtil.GLFW_KEY_F2, 2));
            add(new KeyLayoutConfig.BasicKeyData("3", InputUtil.GLFW_KEY_F3, 2));
            add(new KeyLayoutConfig.BasicKeyData("4", InputUtil.GLFW_KEY_F4, 2));
            add(new KeyLayoutConfig.BasicKeyData("5", InputUtil.GLFW_KEY_F5, 2));
            add(new KeyLayoutConfig.BasicKeyData("6", InputUtil.GLFW_KEY_F6, 2));
            add(new KeyLayoutConfig.BasicKeyData("7", InputUtil.GLFW_KEY_F7, 2));
            add(new KeyLayoutConfig.BasicKeyData("8", InputUtil.GLFW_KEY_F8, 2));
            add(new KeyLayoutConfig.BasicKeyData("9", InputUtil.GLFW_KEY_F9, 2));
            add(new KeyLayoutConfig.BasicKeyData("10", InputUtil.GLFW_KEY_F10, 2));
            add(new KeyLayoutConfig.BasicKeyData("11", InputUtil.GLFW_KEY_F11, 2));
            add(new KeyLayoutConfig.BasicKeyData("12", InputUtil.GLFW_KEY_F12, 2));
        }});
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("`", InputUtil.GLFW_KEY_GRAVE_ACCENT));
            add(new KeyLayoutConfig.BasicKeyData("1", InputUtil.GLFW_KEY_1));
            add(new KeyLayoutConfig.BasicKeyData("2", InputUtil.GLFW_KEY_2));
            add(new KeyLayoutConfig.BasicKeyData("3", InputUtil.GLFW_KEY_3));
            add(new KeyLayoutConfig.BasicKeyData("4", InputUtil.GLFW_KEY_4));
            add(new KeyLayoutConfig.BasicKeyData("5", InputUtil.GLFW_KEY_5));
            add(new KeyLayoutConfig.BasicKeyData("6", InputUtil.GLFW_KEY_6));
            add(new KeyLayoutConfig.BasicKeyData("7", InputUtil.GLFW_KEY_7));
            add(new KeyLayoutConfig.BasicKeyData("8", InputUtil.GLFW_KEY_8));
            add(new KeyLayoutConfig.BasicKeyData("9", InputUtil.GLFW_KEY_9));
            add(new KeyLayoutConfig.BasicKeyData("0", InputUtil.GLFW_KEY_0));
            add(new KeyLayoutConfig.BasicKeyData("-", InputUtil.GLFW_KEY_MINUS));
            add(new KeyLayoutConfig.BasicKeyData("=", InputUtil.GLFW_KEY_EQUAL));
            add(new KeyLayoutConfig.BasicKeyData("BSP", InputUtil.GLFW_KEY_BACKSPACE, 20));
        }});
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("TAB", InputUtil.GLFW_KEY_TAB, 10));
            add(new KeyLayoutConfig.BasicKeyData("Q", InputUtil.GLFW_KEY_Q));
            add(new KeyLayoutConfig.BasicKeyData("W", InputUtil.GLFW_KEY_W));
            add(new KeyLayoutConfig.BasicKeyData("E", InputUtil.GLFW_KEY_E));
            add(new KeyLayoutConfig.BasicKeyData("R", InputUtil.GLFW_KEY_R));
            add(new KeyLayoutConfig.BasicKeyData("T", InputUtil.GLFW_KEY_T));
            add(new KeyLayoutConfig.BasicKeyData("Y", InputUtil.GLFW_KEY_Y));
            add(new KeyLayoutConfig.BasicKeyData("U", InputUtil.GLFW_KEY_U));
            add(new KeyLayoutConfig.BasicKeyData("I", InputUtil.GLFW_KEY_I));
            add(new KeyLayoutConfig.BasicKeyData("O", InputUtil.GLFW_KEY_O));
            add(new KeyLayoutConfig.BasicKeyData("P", InputUtil.GLFW_KEY_P));
            add(new KeyLayoutConfig.BasicKeyData("[", InputUtil.GLFW_KEY_LEFT_BRACKET));
            add(new KeyLayoutConfig.BasicKeyData("]", InputUtil.GLFW_KEY_RIGHT_BRACKET));
            add(new KeyLayoutConfig.BasicKeyData("\\", InputUtil.GLFW_KEY_BACKSLASH, 10));
        }});
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("CAP", InputUtil.GLFW_KEY_CAPS_LOCK, 19));
            add(new KeyLayoutConfig.BasicKeyData("A", InputUtil.GLFW_KEY_A));
            add(new KeyLayoutConfig.BasicKeyData("S", InputUtil.GLFW_KEY_S));
            add(new KeyLayoutConfig.BasicKeyData("D", InputUtil.GLFW_KEY_D));
            add(new KeyLayoutConfig.BasicKeyData("F", InputUtil.GLFW_KEY_F));
            add(new KeyLayoutConfig.BasicKeyData("G", InputUtil.GLFW_KEY_G));
            add(new KeyLayoutConfig.BasicKeyData("H", InputUtil.GLFW_KEY_H));
            add(new KeyLayoutConfig.BasicKeyData("J", InputUtil.GLFW_KEY_J));
            add(new KeyLayoutConfig.BasicKeyData("K", InputUtil.GLFW_KEY_K));
            add(new KeyLayoutConfig.BasicKeyData("L", InputUtil.GLFW_KEY_L));
            add(new KeyLayoutConfig.BasicKeyData(";", InputUtil.GLFW_KEY_SEMICOLON));
            add(new KeyLayoutConfig.BasicKeyData("'", InputUtil.GLFW_KEY_APOSTROPHE));
            add(new KeyLayoutConfig.BasicKeyData("ENT", InputUtil.GLFW_KEY_ENTER, 19));
        }});
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("LSH", InputUtil.GLFW_KEY_LEFT_SHIFT, 28));
            add(new KeyLayoutConfig.BasicKeyData("Z", InputUtil.GLFW_KEY_Z));
            add(new KeyLayoutConfig.BasicKeyData("X", InputUtil.GLFW_KEY_X));
            add(new KeyLayoutConfig.BasicKeyData("C", InputUtil.GLFW_KEY_C));
            add(new KeyLayoutConfig.BasicKeyData("V", InputUtil.GLFW_KEY_V));
            add(new KeyLayoutConfig.BasicKeyData("B", InputUtil.GLFW_KEY_B));
            add(new KeyLayoutConfig.BasicKeyData("N", InputUtil.GLFW_KEY_N));
            add(new KeyLayoutConfig.BasicKeyData("M", InputUtil.GLFW_KEY_M));
            add(new KeyLayoutConfig.BasicKeyData(",", InputUtil.GLFW_KEY_COMMA));
            add(new KeyLayoutConfig.BasicKeyData(".", InputUtil.GLFW_KEY_PERIOD));
            add(new KeyLayoutConfig.BasicKeyData("/", InputUtil.GLFW_KEY_SLASH));
            add(new KeyLayoutConfig.BasicKeyData("RSH", InputUtil.GLFW_KEY_RIGHT_SHIFT, 28));
        }});
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("LCT", InputUtil.GLFW_KEY_LEFT_CONTROL, 10));
            add(new KeyLayoutConfig.BasicKeyData("LWN", InputUtil.GLFW_KEY_LEFT_SUPER, 10, 0, false));
            add(new KeyLayoutConfig.BasicKeyData("LAL", InputUtil.GLFW_KEY_LEFT_ALT, 10));
            add(new KeyLayoutConfig.BasicKeyData("SPC", InputUtil.GLFW_KEY_SPACE, 58));
            add(new KeyLayoutConfig.BasicKeyData("RAL", InputUtil.GLFW_KEY_RIGHT_ALT, 10));
            add(new KeyLayoutConfig.BasicKeyData("RWN", InputUtil.GLFW_KEY_RIGHT_SUPER, 10, 0, false));
            add(new KeyLayoutConfig.BasicKeyData("MNU", 348, 10));
            add(new KeyLayoutConfig.BasicKeyData("RCT", InputUtil.GLFW_KEY_RIGHT_CONTROL, 10));
        }});
    }};
    public static final List<List<KeyLayoutConfig.BasicKeyData>> extra = new ArrayList<>() {{
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("INS", InputUtil.GLFW_KEY_INSERT, 6));
            add(new KeyLayoutConfig.BasicKeyData("HME", InputUtil.GLFW_KEY_HOME, 6));
            add(new KeyLayoutConfig.BasicKeyData("PGU", InputUtil.GLFW_KEY_PAGE_UP, 6));
        }});
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("DEL", InputUtil.GLFW_KEY_DELETE, 6));
            add(new KeyLayoutConfig.BasicKeyData("END", InputUtil.GLFW_KEY_END, 6));
            add(new KeyLayoutConfig.BasicKeyData("PGD", InputUtil.GLFW_KEY_PAGE_DOWN, 6));
        }});
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("<", InputUtil.GLFW_KEY_LEFT));
            add(new KeyLayoutConfig.BasicKeyData("^", InputUtil.GLFW_KEY_UP));
            add(new KeyLayoutConfig.BasicKeyData("v", InputUtil.GLFW_KEY_DOWN));
            add(new KeyLayoutConfig.BasicKeyData(">", InputUtil.GLFW_KEY_RIGHT));
        }});
    }};
    public static final List<List<KeyLayoutConfig.BasicKeyData>> mouse = new ArrayList<>() {{
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("ML", GLFW.GLFW_MOUSE_BUTTON_1, 6, 0, true));
            add(new KeyLayoutConfig.BasicKeyData("MM", GLFW.GLFW_MOUSE_BUTTON_3, 6, 0, true));
            add(new KeyLayoutConfig.BasicKeyData("MR", GLFW.GLFW_MOUSE_BUTTON_2, 6, 0, true));
        }});
    }};
    public static final List<List<KeyLayoutConfig.BasicKeyData>> numpad = new ArrayList<>() {{
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("NM", InputUtil.GLFW_KEY_NUM_LOCK));
            add(new KeyLayoutConfig.BasicKeyData("/", 331));
            add(new KeyLayoutConfig.BasicKeyData("*", InputUtil.GLFW_KEY_KP_MULTIPLY));
            add(new KeyLayoutConfig.BasicKeyData("-", 3333));
        }});
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("7", InputUtil.GLFW_KEY_KP_7));
            add(new KeyLayoutConfig.BasicKeyData("8", InputUtil.GLFW_KEY_KP_8));
            add(new KeyLayoutConfig.BasicKeyData("9", InputUtil.GLFW_KEY_KP_9));
            add(new KeyLayoutConfig.BasicKeyData("+", InputUtil.GLFW_KEY_KP_ADD, 0, 18));
        }});
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("4", InputUtil.GLFW_KEY_KP_4));
            add(new KeyLayoutConfig.BasicKeyData("5", InputUtil.GLFW_KEY_KP_5));
            add(new KeyLayoutConfig.BasicKeyData("6", InputUtil.GLFW_KEY_KP_6));
        }});
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("1", InputUtil.GLFW_KEY_KP_1));
            add(new KeyLayoutConfig.BasicKeyData("2", InputUtil.GLFW_KEY_KP_2));
            add(new KeyLayoutConfig.BasicKeyData("3", InputUtil.GLFW_KEY_KP_3));
            add(new KeyLayoutConfig.BasicKeyData("EN", InputUtil.GLFW_KEY_KP_ENTER, 0, 18));
        }});
        add(new ArrayList<>() {{
            add(new KeyLayoutConfig.BasicKeyData("0", InputUtil.GLFW_KEY_KP_0, 18));
            add(new KeyLayoutConfig.BasicKeyData(".", InputUtil.GLFW_KEY_KP_DECIMAL));
        }});
    }};

    public KeyLayoutEnglish() {
        super("English");
    }

    @Override
    public List<List<KeyLayoutConfig.BasicKeyData>> getKeys() {
        return keys;
    }

    @Override
    public List<List<KeyLayoutConfig.BasicKeyData>> getExtra() {
        return extra;
    }

    @Override
    public List<List<KeyLayoutConfig.BasicKeyData>> getMouse() {
        return mouse;
    }

    @Override
    public List<List<KeyLayoutConfig.BasicKeyData>> getNumpad() {
        return numpad;
    }
}
