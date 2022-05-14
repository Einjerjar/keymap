package com.github.einjerjar.mc.keymap.utils;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class WidgetUtils {
    public static boolean inBounds(int mouseX, int mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX <= x + w && mouseY > y && mouseY <= y + h;
    }

    public static void drawBoxFilled(DrawableHelper d, MatrixStack m, int x, int y, int w, int h, int colBorder, int colFill) {
        if (d == null) {
            KeymapMain.LOGGER().warn("Empty DrawableHelper passed to drawBox");
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

        Utils.SColor _color = new Utils.SColor(color);
        int          a      = _color.a;
        int          r      = _color.r;
        int          g      = _color.g;
        int          b      = _color.b;

        bb.vertex(left, bottom, 0.0D).color(r, g, b, a).next();
        bb.vertex(right, bottom, 0.0D).color(r, g, b, a).next();
        bb.vertex(right, top, 0.0D).color(r, g, b, a).next();
        bb.vertex(left, top, 0.0D).color(r, g, b, a).next();

        if (init_and_build) {
            ts.draw();
        }
    }


    public static void drawCenteredText(MatrixStack matrices, TextRenderer tr, Text text, int x, int y, int w, int h, boolean shadow, boolean centerX, boolean centerY, int color) {
        x = centerX ? x + w / 2 - tr.getWidth(text) / 2 : x;
        y = centerY ? y + h / 2 - tr.fontHeight / 2 : y;
        if (shadow) tr.drawWithShadow(matrices, text, x, y, color);
        else tr.draw(matrices, text, x, y, color);
    }

    public static void drawCenteredText(MatrixStack matrices, TextRenderer tr, Text text, int x, int y, int w, int h, boolean shadow, int color) {
        drawCenteredText(matrices, tr, text, x, y, w, h, true, true, true, color);
    }

    public static void drawCenteredText(MatrixStack matrices, TextRenderer tr, Text text, int x, int y, int w, int h, int color) {
        drawCenteredText(matrices, tr, text, x, y, w, h, true, color);
    }
}
