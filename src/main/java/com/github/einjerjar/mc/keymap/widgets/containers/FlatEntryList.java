package com.github.einjerjar.mc.keymap.widgets.containers;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.widgets.FlatWidgetBase;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public abstract class FlatEntryList<T extends FlatEntryList.FlatEntry<T>> extends FlatWidgetBase implements Selectable {
    MinecraftClient client;
    int x;
    int y;
    int w;
    int h;
    int entryHeight;

    int scrollOffset = 0;
    int scrollBarW = 6;
    int scrollBarX;

    boolean hovered;
    boolean dragging;
    boolean focused;
    boolean visible;

    List<T> entries = new ArrayList<>();

    T hoveredEntry;
    T selectedEntry;

    int lastDragY;
    int lastClickX;

    public FlatEntryList(int x, int y, int w, int h, int entryHeight) {
        this.client = MinecraftClient.getInstance();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.entryHeight = entryHeight;
        this.scrollBarX = x + w - scrollBarW;
    }

    @Override
    public SelectionType getType() {
        return focused
               ? SelectionType.FOCUSED
               : hovered
                 ? SelectionType.HOVERED
                 : SelectionType.NONE;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        hovered = isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h;
    }

    public void renderScrollbar(MatrixStack matrices) {
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder bb = ts.getBuffer();
        int scroll = h / getContentHeight();
        if (scroll >= 1) {
            scrollOffset = 0;
            return;
        }

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bb.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        // TODO: make final vars
        int colScrollBg = 0x88_000000;
        int colScrollFg = 0xaa_ffffff;

        int scrollHeight = (int)(h / (float) getContentHeight() * h);
        int scrollTop = (int)(scrollOffset / (float) getContentHeight() * h);

        // KeymapMain.LOGGER.info(String.format(
        //     "sh %s :: st %s :: ch %s :: h %s",
        //     scrollHeight,
        //     scrollTop,
        //     getContentHeight(),
        //     h
        // ));

        // left right top bottom
        Utils.drawQuad(ts, bb, scrollBarX, x + w, y, y + h, colScrollBg, false);
        Utils.drawQuad(ts, bb, scrollBarX, x + w, y + scrollTop, y + scrollTop + scrollHeight, colScrollFg, false);

        ts.draw();
        RenderSystem.disableBlend();
    }

    public void renderList(MatrixStack matrices) {
        for (int i = 0; i < entries.size(); i++) {
            T entry = entries.get(i);
            entry.render(matrices, x, y + i * entryHeight - scrollOffset, w - scrollBarW, entryHeight, false, 0);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        dragging = true;
        lastDragY = (int) (mouseY - y);
        lastClickX = (int) mouseX;
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        int direction = -1;
        int scroll = h / getContentHeight();
        if (scroll >= 1) return false;

        if (!(mouseX > left() && mouseX < right() && mouseY > top() && mouseY < bottom())) return false;
        if (!dragging) return false;

        if (lastClickX > scrollBarX && lastClickX < scrollBarX + scrollBarW) direction *= -1;

        if (mouseY - y != lastDragY) {
            // positive if down
            int delta = (int)(mouseY - y - lastDragY) * direction;
            lastDragY = (int) (mouseY - y);
            scrollOffset += delta;

            // clamp scroll
            if (scrollOffset < 0) scrollOffset = 0;
            if (scrollOffset > getContentHeight() - h) scrollOffset = getContentHeight() - h;
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        // TODO: click on entry
        if (lastDragY == mouseY) {}
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public FlatEntryList<T> addEntry(T entry) {
        entries.add(entry);
        return this;
    }

    public FlatEntryList<T> clearEntries() {
        entries.clear();
        return this;
    }

    public T getEntry(int index) {
        return this.entries.get(index);
    }

    public int getEntryCount() {
        return this.entries.size();
    }

    public int getContentHeight() {
        return getEntryCount() * entryHeight;
    }

    public int left() {
        return x;
    }

    public int right() {
        return x + w;
    }

    public int top() {
        return y;
    }

    public int bottom() {
        return y + h;
    }

    public abstract static class FlatEntry<T extends FlatEntryList.FlatEntry<T>> {
        protected TextRenderer tr;
        protected boolean hovered = false;
        protected boolean selected = false;

        protected ColorGroup colors = ColorGroup.NORMAL;

        public void updateState() {
            colors = selected
                     ? ColorGroup.RED
                     : ColorGroup.NORMAL;
        }

        public FlatEntry() {
            this.tr = MinecraftClient.getInstance().textRenderer;
        }

        public abstract void render(MatrixStack matrices, int x, int y, int w, int h, boolean hovered, float delta);
    }
}
