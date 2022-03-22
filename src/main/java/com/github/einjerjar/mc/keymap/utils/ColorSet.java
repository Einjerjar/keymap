package com.github.einjerjar.mc.keymap.utils;

public class ColorSet {
    public int normal = 0x33_ffffff;
    public int hovered = 0x55_ffffff;
    public int disabled = 0x33_888888;
    public int active = 0xdd_ffffff;

    public enum ColorSetType {
        BG,
        TEXT,
        BORDER
    }

    public static ColorSet fromColor(int color, ColorSetType type) {
        color = color & 0x00_ffffff;
        int finalColor = color;

        if (type == ColorSetType.BG)
            return new ColorSet() {{
                normal = 0x33_000000 | finalColor;
                hovered = 0x55_000000 | finalColor;
                disabled = 0x11_000000 | finalColor;
                active = 0xdd_000000 | finalColor;
            }};
        if (type == ColorSetType.TEXT)
            return new ColorSet() {{
                normal = 0xff_000000 | finalColor;
                hovered = 0xff_000000 | finalColor;
                disabled = 0x55_000000 | finalColor;
                active = 0xff_000000 | ((finalColor & 0xfefefe) >> 1);
            }};

        return new ColorSet() {{
            normal = 0xff_000000 | finalColor;
            hovered = 0xff_000000 | finalColor;
            disabled = 0xff_000000 | finalColor;
            active = 0xff_000000 | finalColor;
        }};
    }

    public static final ColorSet BG = new ColorSet() {
    };
    public static final ColorSet TEXT = new ColorSet() {{
        normal = 0xff_ffffff;
        hovered = 0xff_ffffff;
        disabled = 0xff_888888;
        active = 0xff_888888;
    }};
    public static final ColorSet BORDER = new ColorSet() {{
        normal = 0xff_ffffff;
        hovered = 0xff_ffffff;
        disabled = 0xff_888888;
        active = 0xff_ffffff;
    }};

    public int getVariant(boolean isEnabled, boolean isActive, boolean isHovered) {
        return !isEnabled ? disabled : isActive ? active : isHovered ? hovered : normal;
    }

}
