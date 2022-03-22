package com.github.einjerjar.mc.keymap.screen;

import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class KeyboardLayout {
    public static class KeyboardKey {
        public String text;
        public int extraWidth = 0;
        public int extraHeight = 0;
        public boolean enabled = true;
        public int keyCode = 0;
        public InputUtil.Key key;

        private void init(String text, int keyCode, int extraWidth, int extraHeight, InputUtil.Type type, boolean enabled) {
            this.text = text;
            this.extraWidth = extraWidth;
            this.extraHeight = extraHeight;
            this.enabled = enabled;
            this.keyCode = keyCode;
            this.key = type.createFromCode(keyCode);
        }

        public KeyboardKey(String text, int keyCode, int extraWidth, int extraHeight, InputUtil.Type type, boolean enabled) {
            init(text, keyCode, extraWidth, extraHeight, type, enabled);
        }

        public KeyboardKey(String text, int keyCode, int extraWidth, int extraHeight, boolean enabled) {
            init(text, keyCode, extraWidth, extraHeight, InputUtil.Type.KEYSYM, enabled);
        }

        public KeyboardKey(String text, int keyCode, int extraWidth, int extraHeight) {
            init(text, keyCode, extraWidth, extraHeight, InputUtil.Type.KEYSYM, true);
        }

        public KeyboardKey(String text, int keyCode, int extraWidth, int extraHeight, InputUtil.Type type) {
            init(text, keyCode, extraWidth, extraHeight, type, true);
        }

        public KeyboardKey(String text, int keyCode, int extraWidth, InputUtil.Type type) {
            init(text, keyCode, extraWidth, 0, type, true);
        }

        public KeyboardKey(String text, int keyCode, int extraWidth) {
            init(text, keyCode, extraWidth, 0, InputUtil.Type.KEYSYM, true);
        }

        public KeyboardKey(String text, int keyCode) {
            init(text, keyCode, 0, 0, InputUtil.Type.KEYSYM, true);
        }

        public Text getHoverText() {
            return key.getLocalizedText();
        }
    }

    public static ArrayList<ArrayList<KeyboardKey>> getKeys() {
        return new ArrayList<>() {{
            add(new ArrayList<>() {{
                add(new KeyboardKey("ESC", InputUtil.GLFW_KEY_ESCAPE, 14));
                add(new KeyboardKey("1", InputUtil.GLFW_KEY_F1, 2));
                add(new KeyboardKey("2", InputUtil.GLFW_KEY_F2, 2));
                add(new KeyboardKey("3", InputUtil.GLFW_KEY_F3, 2));
                add(new KeyboardKey("4", InputUtil.GLFW_KEY_F4, 2));
                add(new KeyboardKey("5", InputUtil.GLFW_KEY_F5, 2));
                add(new KeyboardKey("6", InputUtil.GLFW_KEY_F6, 2));
                add(new KeyboardKey("7", InputUtil.GLFW_KEY_F7, 2));
                add(new KeyboardKey("8", InputUtil.GLFW_KEY_F8, 2));
                add(new KeyboardKey("9", InputUtil.GLFW_KEY_F9, 2));
                add(new KeyboardKey("10", InputUtil.GLFW_KEY_F10, 2));
                add(new KeyboardKey("11", InputUtil.GLFW_KEY_F11, 2));
                add(new KeyboardKey("12", InputUtil.GLFW_KEY_F12, 2));
            }});
            add(new ArrayList<>() {{
                add(new KeyboardKey("`", InputUtil.GLFW_KEY_GRAVE_ACCENT));
                add(new KeyboardKey("1", InputUtil.GLFW_KEY_1));
                add(new KeyboardKey("2", InputUtil.GLFW_KEY_2));
                add(new KeyboardKey("3", InputUtil.GLFW_KEY_3));
                add(new KeyboardKey("4", InputUtil.GLFW_KEY_4));
                add(new KeyboardKey("5", InputUtil.GLFW_KEY_5));
                add(new KeyboardKey("6", InputUtil.GLFW_KEY_6));
                add(new KeyboardKey("7", InputUtil.GLFW_KEY_7));
                add(new KeyboardKey("8", InputUtil.GLFW_KEY_8));
                add(new KeyboardKey("9", InputUtil.GLFW_KEY_9));
                add(new KeyboardKey("0", InputUtil.GLFW_KEY_0));
                add(new KeyboardKey("-", InputUtil.GLFW_KEY_MINUS));
                add(new KeyboardKey("=", InputUtil.GLFW_KEY_EQUAL));
                add(new KeyboardKey("BSP", InputUtil.GLFW_KEY_BACKSPACE, 20));
            }});
            add(new ArrayList<>() {{
                add(new KeyboardKey("TAB", InputUtil.GLFW_KEY_TAB, 10));
                add(new KeyboardKey("Q", InputUtil.GLFW_KEY_Q));
                add(new KeyboardKey("W", InputUtil.GLFW_KEY_W));
                add(new KeyboardKey("E", InputUtil.GLFW_KEY_E));
                add(new KeyboardKey("R", InputUtil.GLFW_KEY_R));
                add(new KeyboardKey("T", InputUtil.GLFW_KEY_T));
                add(new KeyboardKey("Y", InputUtil.GLFW_KEY_Y));
                add(new KeyboardKey("U", InputUtil.GLFW_KEY_U));
                add(new KeyboardKey("I", InputUtil.GLFW_KEY_I));
                add(new KeyboardKey("O", InputUtil.GLFW_KEY_O));
                add(new KeyboardKey("P", InputUtil.GLFW_KEY_P));
                add(new KeyboardKey("[", InputUtil.GLFW_KEY_LEFT_BRACKET));
                add(new KeyboardKey("]", InputUtil.GLFW_KEY_RIGHT_BRACKET));
                add(new KeyboardKey("\\", InputUtil.GLFW_KEY_BACKSLASH, 10));
            }});
            add(new ArrayList<>() {{
                add(new KeyboardKey("CAP", InputUtil.GLFW_KEY_CAPS_LOCK, 10));
                add(new KeyboardKey("A", InputUtil.GLFW_KEY_A));
                add(new KeyboardKey("S", InputUtil.GLFW_KEY_S));
                add(new KeyboardKey("D", InputUtil.GLFW_KEY_D));
                add(new KeyboardKey("F", InputUtil.GLFW_KEY_F));
                add(new KeyboardKey("G", InputUtil.GLFW_KEY_G));
                add(new KeyboardKey("H", InputUtil.GLFW_KEY_H));
                add(new KeyboardKey("I", InputUtil.GLFW_KEY_I));
                add(new KeyboardKey("J", InputUtil.GLFW_KEY_J));
                add(new KeyboardKey("K", InputUtil.GLFW_KEY_K));
                add(new KeyboardKey("L", InputUtil.GLFW_KEY_L));
                add(new KeyboardKey(";", InputUtil.GLFW_KEY_SEMICOLON));
                add(new KeyboardKey("'", InputUtil.GLFW_KEY_APOSTROPHE));
                add(new KeyboardKey("ENT", InputUtil.GLFW_KEY_ENTER, 10));
            }});
            add(new ArrayList<>() {{
                add(new KeyboardKey("LSH", InputUtil.GLFW_KEY_LEFT_SHIFT, 28));
                add(new KeyboardKey("Z", InputUtil.GLFW_KEY_Z));
                add(new KeyboardKey("X", InputUtil.GLFW_KEY_X));
                add(new KeyboardKey("C", InputUtil.GLFW_KEY_C));
                add(new KeyboardKey("V", InputUtil.GLFW_KEY_V));
                add(new KeyboardKey("B", InputUtil.GLFW_KEY_B));
                add(new KeyboardKey("N", InputUtil.GLFW_KEY_N));
                add(new KeyboardKey("M", InputUtil.GLFW_KEY_M));
                add(new KeyboardKey(",", InputUtil.GLFW_KEY_COMMA));
                add(new KeyboardKey(".", InputUtil.GLFW_KEY_PERIOD));
                add(new KeyboardKey("/", InputUtil.GLFW_KEY_SLASH));
                add(new KeyboardKey("RSH", InputUtil.GLFW_KEY_RIGHT_SHIFT, 28));
            }});
            add(new ArrayList<>() {{
                add(new KeyboardKey("LCT", InputUtil.GLFW_KEY_LEFT_CONTROL, 10));
                add(new KeyboardKey("LWN", InputUtil.GLFW_KEY_LEFT_SUPER, 10, 0, false));
                add(new KeyboardKey("LAL", InputUtil.GLFW_KEY_LEFT_ALT, 10));
                add(new KeyboardKey("SPC", InputUtil.GLFW_KEY_SPACE, 58));
                add(new KeyboardKey("RAL", InputUtil.GLFW_KEY_RIGHT_ALT, 10));
                add(new KeyboardKey("RWN", InputUtil.GLFW_KEY_RIGHT_SUPER, 10, 0, false));
                add(new KeyboardKey("MNU", 348, 10));
                add(new KeyboardKey("RCT", InputUtil.GLFW_KEY_RIGHT_CONTROL, 10));
            }});
        }};
    }

    public static ArrayList<ArrayList<KeyboardKey>> getNumpad() {
        return new ArrayList<>() {{
            add(new ArrayList<>() {{
                add(new KeyboardKey("NM", InputUtil.GLFW_KEY_NUM_LOCK));
                add(new KeyboardKey("/", 331));
                add(new KeyboardKey("*", InputUtil.GLFW_KEY_KP_MULTIPLY));
                add(new KeyboardKey("-", 3333));
            }});
            add(new ArrayList<>() {{
                add(new KeyboardKey("7", InputUtil.GLFW_KEY_KP_7));
                add(new KeyboardKey("8", InputUtil.GLFW_KEY_KP_8));
                add(new KeyboardKey("9", InputUtil.GLFW_KEY_KP_9));
                add(new KeyboardKey("+", InputUtil.GLFW_KEY_KP_ADD, 0, 18));
            }});
            add(new ArrayList<>() {{
                add(new KeyboardKey("4", InputUtil.GLFW_KEY_KP_4));
                add(new KeyboardKey("5", InputUtil.GLFW_KEY_KP_5));
                add(new KeyboardKey("6", InputUtil.GLFW_KEY_KP_6));
            }});
            add(new ArrayList<>() {{
                add(new KeyboardKey("1", InputUtil.GLFW_KEY_KP_1));
                add(new KeyboardKey("2", InputUtil.GLFW_KEY_KP_2));
                add(new KeyboardKey("3", InputUtil.GLFW_KEY_KP_3));
                add(new KeyboardKey("EN", InputUtil.GLFW_KEY_KP_ENTER, 0, 18));
            }});
            add(new ArrayList<>() {{
                add(new KeyboardKey("0", InputUtil.GLFW_KEY_KP_0, 18));
                add(new KeyboardKey(".", InputUtil.GLFW_KEY_KP_DECIMAL));
            }});
        }};
    }

    public static ArrayList<ArrayList<KeyboardKey>> getMouse() {
        return new ArrayList<>() {{
            add(new ArrayList<>() {{
                add(new KeyboardKey("ML", GLFW.GLFW_MOUSE_BUTTON_1, 6, InputUtil.Type.MOUSE));
                add(new KeyboardKey("MM", GLFW.GLFW_MOUSE_BUTTON_3, 6, InputUtil.Type.MOUSE));
                add(new KeyboardKey("MR", GLFW.GLFW_MOUSE_BUTTON_2, 6, InputUtil.Type.MOUSE));
            }});
        }};
    }

    public static ArrayList<ArrayList<KeyboardKey>> getExtra() {
        return new ArrayList<>() {{
            add(new ArrayList<>() {{
                add(new KeyboardKey("INS", InputUtil.GLFW_KEY_INSERT, 6));
                add(new KeyboardKey("HME", InputUtil.GLFW_KEY_HOME, 6));
                add(new KeyboardKey("PGU", InputUtil.GLFW_KEY_PAGE_UP, 6));
            }});
            add(new ArrayList<>() {{
                add(new KeyboardKey("DEL", InputUtil.GLFW_KEY_DELETE, 6));
                add(new KeyboardKey("END", InputUtil.GLFW_KEY_END, 6));
                add(new KeyboardKey("PGD", InputUtil.GLFW_KEY_PAGE_DOWN, 6));
            }});
            add(new ArrayList<>() {{
                add(new KeyboardKey("<", InputUtil.GLFW_KEY_LEFT));
                add(new KeyboardKey("^", InputUtil.GLFW_KEY_UP));
                add(new KeyboardKey("v", InputUtil.GLFW_KEY_DOWN));
                add(new KeyboardKey(">", InputUtil.GLFW_KEY_RIGHT));
            }});
        }};
    }

    // public static ArrayList<ArrayList<KeyboardKey>> keys = new ArrayList<>(){{
    //     add(new ArrayList<>(){{
    //         add(new KeyboardKey("ESC", 15));
    //         add(new KeyboardKey("F1"));
    //         add(new KeyboardKey("F2"));
    //         add(new KeyboardKey("F3"));
    //         add(new KeyboardKey("F4"));
    //         add(new KeyboardKey("F5"));
    //         add(new KeyboardKey("F6"));
    //         add(new KeyboardKey("F7"));
    //         add(new KeyboardKey("F8"));
    //         add(new KeyboardKey("F9"));
    //         add(new KeyboardKey("F10"));
    //         add(new KeyboardKey("F11"));
    //         add(new KeyboardKey("F12"));
    //     }});
    //     add(new ArrayList<>(){{
    //         add(new KeyboardKey("`"));
    //         add(new KeyboardKey("1"));
    //         add(new KeyboardKey("2"));
    //         add(new KeyboardKey("3"));
    //         add(new KeyboardKey("4"));
    //         add(new KeyboardKey("5"));
    //         add(new KeyboardKey("6"));
    //         add(new KeyboardKey("7"));
    //         add(new KeyboardKey("8"));
    //         add(new KeyboardKey("9"));
    //         add(new KeyboardKey("0"));
    //         add(new KeyboardKey("-"));
    //         add(new KeyboardKey("="));
    //         add(new KeyboardKey("BSP"));
    //     }});
    //     add(new ArrayList<>(){{
    //         add(new KeyboardKey("TAB"));
    //         add(new KeyboardKey("Q"));
    //         add(new KeyboardKey("W"));
    //         add(new KeyboardKey("E"));
    //         add(new KeyboardKey("R"));
    //         add(new KeyboardKey("T"));
    //         add(new KeyboardKey("Y"));
    //         add(new KeyboardKey("U"));
    //         add(new KeyboardKey("I"));
    //         add(new KeyboardKey("O"));
    //         add(new KeyboardKey("P"));
    //         add(new KeyboardKey("["));
    //         add(new KeyboardKey("]"));
    //         add(new KeyboardKey("\\"));
    //     }});
    //     add(new ArrayList<>(){{
    //         add(new KeyboardKey("CAP"));
    //         add(new KeyboardKey("A"));
    //         add(new KeyboardKey("S"));
    //         add(new KeyboardKey("D"));
    //         add(new KeyboardKey("F"));
    //         add(new KeyboardKey("G"));
    //         add(new KeyboardKey("H"));
    //         add(new KeyboardKey("I"));
    //         add(new KeyboardKey("J"));
    //         add(new KeyboardKey("K"));
    //         add(new KeyboardKey("L"));
    //         add(new KeyboardKey(";"));
    //         add(new KeyboardKey("'"));
    //         add(new KeyboardKey("ENT"));
    //     }});
    //     add(new ArrayList<>(){{
    //         add(new KeyboardKey("LSH"));
    //         add(new KeyboardKey("Z"));
    //         add(new KeyboardKey("X"));
    //         add(new KeyboardKey("C"));
    //         add(new KeyboardKey("V"));
    //         add(new KeyboardKey("B"));
    //         add(new KeyboardKey("N"));
    //         add(new KeyboardKey("M"));
    //         add(new KeyboardKey(","));
    //         add(new KeyboardKey("."));
    //         add(new KeyboardKey("/"));
    //         add(new KeyboardKey("RSH"));
    //     }});
    //     add(new ArrayList<>(){{
    //         add(new KeyboardKey("LCT"));
    //         add(new KeyboardKey("LWN"));
    //         add(new KeyboardKey("LAL"));
    //         add(new KeyboardKey("SPC", 20));
    //         add(new KeyboardKey("RAL"));
    //         add(new KeyboardKey("RWN"));
    //         add(new KeyboardKey("CTX"));
    //         add(new KeyboardKey("RCT"));
    //     }});
    // }};
}
