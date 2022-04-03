package com.github.einjerjar.mc.keymap.widgets.containers;

import com.github.einjerjar.mc.keymap.screen.Tooltipped;
import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.widgets.FlatWidgetBase;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class FlatEntryList<T extends FlatEntryList.FlatEntry<T>> extends FlatWidgetBase implements Selectable, Tooltipped {
    public boolean dragging;
    protected MinecraftClient client;
    protected int entryHeight;
    protected double scrollOffset = 0;
    protected int scrollBarW = 6;
    protected int scrollBarX;
    protected List<T> entries = new ArrayList<>();
    // protected List<Text> tooltips = new ArrayList<>();

    protected T hoveredEntry;
    protected T selectedEntry;

    protected double lastDragY;
    protected double lastClickY;
    protected double lastClickX;
    protected double lastScrollPosY;

    public FlatEntryList(int x, int y, int w, int h, int entryHeight) {
        super(x, y, w, h);
        this.client = MinecraftClient.getInstance();
        this.entryHeight = entryHeight;
        this.scrollBarX = x + w - scrollBarW;
    }

    @Override
    protected void updateSize() {
        this.scrollBarX = x + w - scrollBarW;
    }

    @Override
    public List<Text> getToolTips() {
        return hoveredEntry != null ? hoveredEntry.getToolTips() : new ArrayList<>();
    }

    @Override
    public Text getFirstToolTip() {
        return hoveredEntry != null ? hoveredEntry.getFirstToolTip() : new LiteralText("");
    }

    @Override
    public SelectionType getType() {
        return focused
               ? SelectionType.FOCUSED
               : hovered
                 ? SelectionType.HOVERED
                 : SelectionType.NONE;
    }

    @Nullable
    public T getSelectedEntry() {
        return selectedEntry;
    }

    public void setSelectedEntry(@Nullable T selectedEntry) {
        if (this.selectedEntry != null) this.selectedEntry.selected = false;
        this.selectedEntry = selectedEntry;
        if (selectedEntry != null) selectedEntry.selected = true;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX > x && mouseX < x + w && mouseY > y && mouseY < y + h;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        hovered = isMouseOver(mouseX, mouseY);

        hoveredEntry = null;

        if (hovered && mouseX < scrollBarX) {
            int index = (int) ((mouseY - y + scrollOffset) / (float) entryHeight);
            if (index >= 0 && index < entries.size()) hoveredEntry = entries.get(index);
        }
        renderWidget(matrices, mouseX, mouseY, delta);
    }

    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderScrollbar();
        renderList(matrices);
    }

    public void renderScrollbar() {
        Tessellator   ts = Tessellator.getInstance();
        BufferBuilder bb = ts.getBuffer();
        int           ch = getContentHeight();
        if (ch == 0) return;
        int scroll = h / ch;
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

        int scrollHeight = (int) (h / (float) getContentHeight() * h);
        int scrollTop    = (int) (scrollOffset / (float) getContentHeight() * h);

        // left right top bottom
        Utils.drawQuad(ts, bb, scrollBarX, x + w, y, y + h, colScrollBg, false);
        Utils.drawQuad(ts, bb, scrollBarX, x + w, y + scrollTop, y + scrollTop + scrollHeight, colScrollFg, false);

        ts.draw();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!hovered) return false;

        scrollOffset += amount * -12;
        if (scrollOffset < 0) scrollOffset = 0;
        if (scrollOffset > getContentHeight() - h) scrollOffset = Math.max(getContentHeight() - h, 0);

        return true;
    }

    public void renderList(MatrixStack matrices) {
        for (int i = 0; i < entries.size(); i++) {
            T   entry = entries.get(i);
            int _x    = x;
            int _y    = y + i * entryHeight - (int) scrollOffset;
            int _w    = w - scrollBarW;
            int _h    = entryHeight;

            if (_y + _h < top() + 8 || _y > bottom() - 8) continue;

            entry.render(matrices, _x, _y, _w, _h, entry == hoveredEntry, 0);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!hovered) return false;
        dragging = true;
        lastDragY = mouseY - y;
        lastClickX = mouseX;
        lastClickY = mouseY - y;
        lastScrollPosY = scrollOffset;
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        int direction = -1;
        if (getContentHeight() == 0) return false;
        int scroll = h / getContentHeight();
        if (scroll >= 1) return false;

        // if (!(mouseX > left() && mouseX < right() && mouseY > top() && mouseY < bottom())) return false;
        if (!dragging) return false;

        if (lastClickX > scrollBarX && lastClickX < scrollBarX + scrollBarW) direction *= -1;

        if (mouseY - y != lastDragY) {
            // positive if down
            double delta = (mouseY - y - lastClickY) * direction;
            lastDragY = (mouseY - y);
            scrollOffset = (lastScrollPosY + delta);

            // clamp scroll
            if (scrollOffset < 0) scrollOffset = 0;
            if (scrollOffset > getContentHeight() - h) scrollOffset = Math.max(getContentHeight() - h, 0);
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        if (Math.abs(lastClickY - (mouseY - y)) < 3) {
            if (selectedEntry != null) selectedEntry.selected = false;
            selectedEntry = hoveredEntry;
            if (selectedEntry != null) {
                selectedEntry.selected = true;
                playClickSound();
            }
            return true;
        }
        return false;
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

    public abstract static class FlatEntry<T extends FlatEntryList.FlatEntry<T>> extends DrawableHelper implements Tooltipped {
        public boolean hovered = false;
        public boolean selected = false;
        protected TextRenderer tr;
        protected ColorGroup colors = ColorGroup.NORMAL;

        protected List<Text> tooltips = new ArrayList<>();

        public FlatEntry() {
            this.tr = MinecraftClient.getInstance().textRenderer;
        }

        public void updateState() {
            colors = selected
                     ? ColorGroup.RED
                     : hovered
                       ? ColorGroup.GREEN
                       : ColorGroup.NORMAL;
        }

        public void render(MatrixStack matrices, int x, int y, int w, int h, boolean hovered, float delta) {
            this.hovered = hovered;
            updateState();
            renderWidget(matrices, x, y, w, h, hovered, delta);
        }

        public abstract void renderWidget(MatrixStack matrices, int x, int y, int w, int h, boolean hovered, float delta);

        @Override
        public List<Text> getToolTips() {
            return null;
        }

        @Override
        public Text getFirstToolTip() {
            return null;
        }
    }
}
