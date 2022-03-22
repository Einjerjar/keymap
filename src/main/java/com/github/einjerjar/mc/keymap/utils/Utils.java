package com.github.einjerjar.mc.keymap.utils;

import com.github.einjerjar.mc.keymap.Main;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class Utils {
    public static class NIL {
        public static final Text TEXT = Text.of("");
    }

    public static <E> E orNull(E x, E y) {
        if (x != null) return x;
        return y;
    }

    public static void drawBoxFilled(DrawableHelper d, MatrixStack m, int x, int y, int w, int h, int colBorder, int colFill) {
        if (d == null) {
            Main.LOGGER.warn("Empty DrawableHelper passed to drawBox");
            return;
        }

        fillBox(d, m, x + 1, y + 1, w - 2, h - 2, colFill);
        drawBoxOutline(d, m, x, y, w, h, colBorder);
    }

    public static void drawBoxOutline(DrawableHelper d, MatrixStack m, int x, int y, int w, int h, int color) {
        drawHorizontalLine(d, m, x, y, w, color);
        drawHorizontalLine(d, m, x, y + h - 1, w, color);
        drawVerticalLine(d, m, x, y + 1, h - 2, color);
        drawVerticalLine(d, m, x + w - 1, y + 1, h - 2, color);
    }

    public static void drawHorizontalLine(DrawableHelper d, MatrixStack m, int x, int y, int w, int color) {
        fillBox(d, m, x, y, w, 1, color);
    }

    public static void drawVerticalLine(DrawableHelper d, MatrixStack m, int x, int y, int h, int color) {
        fillBox(d, m, x, y, 1, h, color);
    }

    public static void fillBox(DrawableHelper d, MatrixStack m, int x, int y, int w, int h, int color) {
        int x1 = x;
        int y1 = y;
        int x2 = x + w;
        int y2 = y + h;
        DrawableHelper.fill(m, x1, y1, x2, y2, color);
    }
}
