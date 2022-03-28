package com.github.einjerjar.mc.keymap.utils;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.List;

public class Utils {
    public static final Style styleKey = new Style(TextColor.fromRgb(0xff_55ff55), true, true, false, false, false, null, null, "", null);
    public static final Style styleKeybind = new Style(TextColor.fromRgb(0xff_ffffff), false, false, false, false, false, null, null, "", null);
    public static final Style styleSeparator = new Style(TextColor.fromRgb(0xff_555555), false, false, false, false, false, null, null, "", null);
    public static final Style styleError = new Style(TextColor.fromRgb(0xff_ff5555), true, false, false, false, false, null, null, "", null);
    public static final Style stylePassive = new Style(TextColor.fromRgb(0xff_aaaaaa), false, false, false, false, false, null, null, "", null);
    public static final Style styleSimpleBold = new Style(TextColor.fromRgb(0xff_ffffff), false, false, false, false, false, null, null, "", null);

    public static <E> E safeGet(List<E> l, int i) {
        if (l.size() > 0) return l.get(i);
        return null;
    }

    public static <E> E safeGet(List<E> l, int i, E or) {
        if (l.size() > 0) return l.get(i);
        return or;
    }

    public static class NIL {
        public static final Text TEXT = Text.of("");
    }

    public static <E> E or(E x, E y) {
        if (x != null) return x;
        return y;
    }

    public static void drawBoxFilled(DrawableHelper d, MatrixStack m, int x, int y, int w, int h, int colBorder, int colFill) {
        if (d == null) {
            KeymapMain.LOGGER.warn("Empty DrawableHelper passed to drawBox");
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
        int x2 = x + w;
        int y2 = y + h;
        DrawableHelper.fill(m, x, y, x2, y2, color);
    }

    public static void drawQuad(Tessellator ts, BufferBuilder bb, int left, int right, int top, int bottom, int color, boolean init_and_build) {
        if (init_and_build) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            bb.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        }

        SColor _color = new SColor(color);
        int    a      = _color.a;
        int    r      = _color.r;
        int    g      = _color.g;
        int    b      = _color.b;

        bb.vertex(left, bottom, 0.0D).color(r, g, b, a).next();
        bb.vertex(right, bottom, 0.0D).color(r, g, b, a).next();
        bb.vertex(right, top, 0.0D).color(r, g, b, a).next();
        bb.vertex(left, top, 0.0D).color(r, g, b, a).next();

        if (init_and_build) {
            ts.draw();
        }
    }

    public static class SColor {
        public int a;
        public int r;
        public int g;
        public int b;

        public SColor(float a, float r, float g, float b) {
            this.a = (int) (a * 255);
            this.r = (int) (r * 255);
            this.g = (int) (g * 255);
            this.b = (int) (b * 255);
        }

        public SColor(int color) {
            this.a = color >> 24 & 0xff;
            this.r = color >> 16 & 0xff;
            this.g = color >> 8 & 0xff;
            this.b = color & 0xff;
        }

        public SColor(int a, int r, int g, int b) {
            this.a = a;
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
}
