package com.github.einjerjar.mc.keymap.utils;

import com.github.einjerjar.mc.keymap.Main;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
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

    public static void drawQuad(Tessellator ts, BufferBuilder bb, int left, int right, int top, int bottom, int color, boolean init_and_build) {
        if (init_and_build) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            bb.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        }

        SColor _color = new SColor(color);

        bb.vertex(left, bottom, 0.0D).color(_color.r, _color.g, _color.b, _color.a).next();
        bb.vertex(right, bottom, 0.0D).color(_color.r, _color.g, _color.b, _color.a).next();
        bb.vertex(right, top, 0.0D).color(_color.r, _color.g, _color.b, _color.a).next();
        bb.vertex(left, top, 0.0D).color(_color.r, _color.g, _color.b, _color.a).next();

        if (init_and_build) {
            ts.draw();
        }
    }

    public static class SColor {
        public float a;
        public float r;
        public float g;
        public float b;

        public SColor(float a, float r, float g, float b) {
            this.a = a;
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public SColor(int color) {
            this.a = (float) ((0xff_000000 & color) / 255.0);
            this.r = (float) ((0x00_ff0000 & color) / 255.0);
            this.g = (float) ((0x00_00ff00 & color) / 255.0);
            this.b = (float) ((0x00_0000ff & color) / 255.0);
        }

        public SColor(int a, int r, int g, int b) {
            this.a = (float) (a / 255.0);
            this.r = (float) (r / 255.0);
            this.g = (float) (g / 255.0);
            this.b = (float) (b / 255.0);
        }
    }
}
