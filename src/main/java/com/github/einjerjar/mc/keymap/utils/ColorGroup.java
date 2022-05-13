package com.github.einjerjar.mc.keymap.utils;

public class ColorGroup {
    public static final ColorGroup NORMAL = new ColorGroup();
    public static final ColorGroup GREEN = ColorGroup.fromBaseColor(0x44FF44);
    public static final ColorGroup RED = ColorGroup.fromBaseColor(0xFF4444);
    public static final ColorGroup YELLOW = ColorGroup.fromBaseColor(0xFFFF44);
    public static final ColorGroup BLUE = ColorGroup.fromBaseColor(0x4444FF);
    public static final ColorGroup ORANGE = ColorGroup.fromBaseColor(0xFF8844);
    public ColorSet bg = ColorSet.BG;
    public ColorSet text = ColorSet.TEXT;
    public ColorSet border = ColorSet.BORDER;

    public static ColorGroup fromBaseColor(int col) {
        return new ColorGroup() {{
            bg = ColorSet.fromColor(col, ColorSet.ColorSetType.BG);
            text = ColorSet.fromColor(col, ColorSet.ColorSetType.TEXT);
            border = ColorSet.fromColor(col, ColorSet.ColorSetType.BORDER);
        }};
    }
}
