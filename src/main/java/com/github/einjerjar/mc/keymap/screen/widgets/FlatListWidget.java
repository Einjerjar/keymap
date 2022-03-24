package com.github.einjerjar.mc.keymap.screen.widgets;

import com.github.einjerjar.mc.keymap.utils.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;

import java.util.AbstractList;
import java.util.ArrayList;

public abstract class FlatListWidget<T extends FlatListWidget.FlatEntry<T>> extends AbstractParentElement implements Drawable, Selectable {
    MinecraftClient client;
    int rowWidth;
    int width;
    int height;
    int x;
    int y;
    int itemHeight;
    int scrollBarX;
    int scrollAmount = 0;
    int scrollBarWidth = 6;
    int colScrollBg = 0x88_222222;
    int colScrollFg = 0xFF_ffffff;
    boolean scrolling;
    ArrayList<T> children = new ArrayList<>();
    T selected = null;

    public FlatListWidget(int x, int y, int width, int height, int itemHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.itemHeight = itemHeight;
        this.scrollBarX = x + width - scrollBarWidth;
    }

    public int getContentHeight() {
        return children.size() * itemHeight;
    }

    public int getMaxScroll() {
        return Math.max(0, getContentHeight() - ((y + height) - y - 4));
    }

    public void render(MatrixStack m, int mouseX, int mouseY, float delta) {
        renderList(m, mouseX, mouseY, delta);
        renderScrollBar();
    }

    public void renderList(MatrixStack m, int mouseX, int mouseY, float delta) {
        int size = children.size();

        Tessellator ts = Tessellator.getInstance();
        BufferBuilder bb = ts.getBuffer();
    }

    public void renderScrollBar() {
        int maxScroll = getMaxScroll();
        if (maxScroll <= 0) return;

        int scrollPercent = height / getContentHeight();
        int scrollBarHeight = height * scrollPercent;
        int x1 = scrollBarX;
        int x2 = scrollBarX + scrollBarWidth;
        int y1 = y;
        int y2 = y + height;

        Tessellator ts = Tessellator.getInstance();
        BufferBuilder bb = ts.getBuffer();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bb.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);


        Utils.drawQuad(ts, bb, x1, x2, y1, y2, colScrollBg, false);
        Utils.drawQuad(ts, bb, x1, x2, y1, y1 + scrollBarHeight, colScrollFg, false);
        ts.draw();
    }

    public void setRowWidth(int rowWidth) {
        this.rowWidth = rowWidth;
    }

    public int getRowWidth() {
        return rowWidth;
    }

    public class FlatEntries extends AbstractList<T> {
        private final ArrayList<T> entries = new ArrayList<>();

        FlatEntries() {
        }

        @Override
        public T get(int i) {
            return entries.get(i);
        }

        @Override
        public int size() {
            return entries.size();
        }

        @Override
        public T set(int i, T e) {
            return entries.set(i, e);
        }

        @Override
        public boolean add(T e) {
            return entries.add(e);
        }

        @Override
        public T remove(int i) {
            return entries.remove(i);
        }
    }

    public abstract static class FlatEntry<T extends FlatEntry<T>> {
        public FlatEntry() {
        }

        public abstract void render(MatrixStack m, int i, int x, int y, int width, int height, int mouseX, int mouseY, boolean hovered, float delta);

        public boolean isMouseOver(int mouseX, int mouseY) {
            return false;
        }
    }
}
