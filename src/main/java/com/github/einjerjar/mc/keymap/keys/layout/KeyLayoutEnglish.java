package com.github.einjerjar.mc.keymap.keys.layout;

import com.github.einjerjar.mc.keymap.keys.BasicKeyData;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class KeyLayoutEnglish extends KeyboardLayoutBase {
    public KeyLayoutEnglish() {
        super("English");
        keys = new ArrayList<>() {{
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
            add(new ArrayList<>() {{
                add(new BasicKeyData("TAB", InputUtil.GLFW_KEY_TAB, 10));
                add(new BasicKeyData("Q", InputUtil.GLFW_KEY_Q));
                add(new BasicKeyData("W", InputUtil.GLFW_KEY_W));
                add(new BasicKeyData("E", InputUtil.GLFW_KEY_E));
                add(new BasicKeyData("R", InputUtil.GLFW_KEY_R));
                add(new BasicKeyData("T", InputUtil.GLFW_KEY_T));
                add(new BasicKeyData("Y", InputUtil.GLFW_KEY_Y));
                add(new BasicKeyData("U", InputUtil.GLFW_KEY_U));
                add(new BasicKeyData("I", InputUtil.GLFW_KEY_I));
                add(new BasicKeyData("O", InputUtil.GLFW_KEY_O));
                add(new BasicKeyData("P", InputUtil.GLFW_KEY_P));
                add(new BasicKeyData("[", InputUtil.GLFW_KEY_LEFT_BRACKET));
                add(new BasicKeyData("]", InputUtil.GLFW_KEY_RIGHT_BRACKET));
                add(new BasicKeyData("\\", InputUtil.GLFW_KEY_BACKSLASH, 10));
            }});
            add(new ArrayList<>() {{
                add(new BasicKeyData("CAP", InputUtil.GLFW_KEY_CAPS_LOCK, 19));
                add(new BasicKeyData("A", InputUtil.GLFW_KEY_A));
                add(new BasicKeyData("S", InputUtil.GLFW_KEY_S));
                add(new BasicKeyData("D", InputUtil.GLFW_KEY_D));
                add(new BasicKeyData("F", InputUtil.GLFW_KEY_F));
                add(new BasicKeyData("G", InputUtil.GLFW_KEY_G));
                add(new BasicKeyData("H", InputUtil.GLFW_KEY_H));
                add(new BasicKeyData("J", InputUtil.GLFW_KEY_J));
                add(new BasicKeyData("K", InputUtil.GLFW_KEY_K));
                add(new BasicKeyData("L", InputUtil.GLFW_KEY_L));
                add(new BasicKeyData(";", InputUtil.GLFW_KEY_SEMICOLON));
                add(new BasicKeyData("'", InputUtil.GLFW_KEY_APOSTROPHE));
                add(new BasicKeyData("ENT", InputUtil.GLFW_KEY_ENTER, 19));
            }});
            add(new ArrayList<>() {{
                add(new BasicKeyData("LSH", InputUtil.GLFW_KEY_LEFT_SHIFT, 28));
                add(new BasicKeyData("Z", InputUtil.GLFW_KEY_Z));
                add(new BasicKeyData("X", InputUtil.GLFW_KEY_X));
                add(new BasicKeyData("C", InputUtil.GLFW_KEY_C));
                add(new BasicKeyData("V", InputUtil.GLFW_KEY_V));
                add(new BasicKeyData("B", InputUtil.GLFW_KEY_B));
                add(new BasicKeyData("N", InputUtil.GLFW_KEY_N));
                add(new BasicKeyData("M", InputUtil.GLFW_KEY_M));
                add(new BasicKeyData(",", InputUtil.GLFW_KEY_COMMA));
                add(new BasicKeyData(".", InputUtil.GLFW_KEY_PERIOD));
                add(new BasicKeyData("/", InputUtil.GLFW_KEY_SLASH));
                add(new BasicKeyData("RSH", InputUtil.GLFW_KEY_RIGHT_SHIFT, 28));
            }});
            add(new ArrayList<>() {{
                add(new BasicKeyData("LCT", InputUtil.GLFW_KEY_LEFT_CONTROL, 10));
                add(new BasicKeyData("LWN", InputUtil.GLFW_KEY_LEFT_SUPER, 10, 0, false));
                add(new BasicKeyData("LAL", InputUtil.GLFW_KEY_LEFT_ALT, 10));
                add(new BasicKeyData("SPC", InputUtil.GLFW_KEY_SPACE, 58));
                add(new BasicKeyData("RAL", InputUtil.GLFW_KEY_RIGHT_ALT, 10));
                add(new BasicKeyData("RWN", InputUtil.GLFW_KEY_RIGHT_SUPER, 10, 0, false));
                add(new BasicKeyData("MNU", 348, 10));
                add(new BasicKeyData("RCT", InputUtil.GLFW_KEY_RIGHT_CONTROL, 10));
            }});
        }};
        extra = new ArrayList<>() {{
            add(new ArrayList<>() {{
                add(new BasicKeyData("INS", InputUtil.GLFW_KEY_INSERT, 6));
                add(new BasicKeyData("HME", InputUtil.GLFW_KEY_HOME, 6));
                add(new BasicKeyData("PGU", InputUtil.GLFW_KEY_PAGE_UP, 6));
            }});
            add(new ArrayList<>() {{
                add(new BasicKeyData("DEL", InputUtil.GLFW_KEY_DELETE, 6));
                add(new BasicKeyData("END", InputUtil.GLFW_KEY_END, 6));
                add(new BasicKeyData("PGD", InputUtil.GLFW_KEY_PAGE_DOWN, 6));
            }});
            add(new ArrayList<>() {{
                add(new BasicKeyData("<", InputUtil.GLFW_KEY_LEFT));
                add(new BasicKeyData("^", InputUtil.GLFW_KEY_UP));
                add(new BasicKeyData("v", InputUtil.GLFW_KEY_DOWN));
                add(new BasicKeyData(">", InputUtil.GLFW_KEY_RIGHT));
            }});
        }};
        mouse = new ArrayList<>() {{
            add(new ArrayList<>() {{
                add(new BasicKeyData("ML", GLFW.GLFW_MOUSE_BUTTON_1, 6, 0, true));
                add(new BasicKeyData("MM", GLFW.GLFW_MOUSE_BUTTON_3, 6, 0, true));
                add(new BasicKeyData("MR", GLFW.GLFW_MOUSE_BUTTON_2, 6, 0, true));
            }});
        }};
        numpad = new ArrayList<>() {{
            add(new ArrayList<>() {{
                add(new BasicKeyData("NM", InputUtil.GLFW_KEY_NUM_LOCK));
                add(new BasicKeyData("/", 331));
                add(new BasicKeyData("*", InputUtil.GLFW_KEY_KP_MULTIPLY));
                add(new BasicKeyData("-", 3333));
            }});
            add(new ArrayList<>() {{
                add(new BasicKeyData("7", InputUtil.GLFW_KEY_KP_7));
                add(new BasicKeyData("8", InputUtil.GLFW_KEY_KP_8));
                add(new BasicKeyData("9", InputUtil.GLFW_KEY_KP_9));
                add(new BasicKeyData("+", InputUtil.GLFW_KEY_KP_ADD, 0, 18));
            }});
            add(new ArrayList<>() {{
                add(new BasicKeyData("4", InputUtil.GLFW_KEY_KP_4));
                add(new BasicKeyData("5", InputUtil.GLFW_KEY_KP_5));
                add(new BasicKeyData("6", InputUtil.GLFW_KEY_KP_6));
            }});
            add(new ArrayList<>() {{
                add(new BasicKeyData("1", InputUtil.GLFW_KEY_KP_1));
                add(new BasicKeyData("2", InputUtil.GLFW_KEY_KP_2));
                add(new BasicKeyData("3", InputUtil.GLFW_KEY_KP_3));
                add(new BasicKeyData("EN", InputUtil.GLFW_KEY_KP_ENTER, 0, 18));
            }});
            add(new ArrayList<>() {{
                add(new BasicKeyData("0", InputUtil.GLFW_KEY_KP_0, 18));
                add(new BasicKeyData(".", InputUtil.GLFW_KEY_KP_DECIMAL));
            }});
        }};
    }
}
