package com.github.einjerjar.mc.widgets.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;

public class WidgetUtils {
    private WidgetUtils() {
    }

    public static double clamp(double x, double min, double max) {
        return Math.max(Math.min(x, max), min);
    }

    public static int clamp(int x, int min, int max) {
        return Math.max(Math.min(x, max), min);
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

    public static void drawQuad(Tesselator ts, BufferBuilder bb, int left, int right, int top, int bottom, int color, boolean init_and_build) {
        if (init_and_build) {
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        }

        SColor lColor = new SColor(color);
        int    a      = lColor.a;
        int    r      = lColor.r;
        int    g      = lColor.g;
        int    b      = lColor.b;

        bb.vertex(left, bottom, 0.0D).color(r, g, b, a).endVertex();
        bb.vertex(right, bottom, 0.0D).color(r, g, b, a).endVertex();
        bb.vertex(right, top, 0.0D).color(r, g, b, a).endVertex();
        bb.vertex(left, top, 0.0D).color(r, g, b, a).endVertex();

        if (init_and_build) {
            ts.end();
        }
    }
}
