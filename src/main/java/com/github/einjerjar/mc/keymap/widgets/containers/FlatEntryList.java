package com.github.einjerjar.mc.keymap.widgets.containers;

import com.github.einjerjar.mc.keymap.screen.Tooltipped;
import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.SimpleV2;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.FlatWidgetBase;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true, chain = true)
public abstract class FlatEntryList<T extends FlatEntryList.FlatEntry<T>> extends FlatWidgetBase implements Selectable, Tooltipped {
    protected boolean dragging;
    protected int entryHeight;
    protected int scrollBarW = 6;
    protected int scrollBarX;

    @Getter protected List<T> entries = new ArrayList<>();
    protected MinecraftClient client;
    protected T hoveredEntry;
    protected T selectedEntry;

    protected double scrollOffset = 0;
    protected double lastDragY;
    protected double lastClickY;
    protected double lastClickX;
    protected double lastScrollPosY;
    protected boolean canDeselectEntry = true;

    protected SimpleV2 pad = new SimpleV2(4);

    @Setter protected CommonAction onSelectedAction;

    public FlatEntryList(int x, int y, int w, int h, int entryHeight) {
        super(x, y, w, h);
        this.client = MinecraftClient.getInstance();
        this.entryHeight = entryHeight;
        this.scrollBarX = x + w - scrollBarW;
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

    public void setSelectedIndex(Integer i) {
        if (i < 0) return;
        if (i > entries.size() - 1) return;
        T e = entries.get(i);
        if (this.selectedEntry != null) this.selectedEntry.selected = false;
        e.selected = true;
        this.selectedEntry = e;
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

    public void setScrollOffset(double scrollOffset) {
        this.scrollOffset = Math.min(Math.max(getContentHeight() - h + pad.y * 2, 0), Math.max(0, scrollOffset));
    }

    public void setScrollOffsetR(double scrollOffset) {
        setScrollOffset(this.scrollOffset + scrollOffset);
    }

    public FlatEntryList<T> addEntry(T entry) {
        entries.add(entry);
        return this;
    }

    public FlatEntryList<T> clearEntries() {
        entries.clear();
        return this;
    }

    @Override
    public void updateSize() {
        this.scrollBarX = x + w - scrollBarW;
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
            int index = (int) ((mouseY - y + scrollOffset - pad.y) / (float) entryHeight);
            if (index >= 0 && index < entries.size()) hoveredEntry = entries.get(index);
        }
        renderWidget(matrices, mouseX, mouseY, delta);
    }

    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderScrollbar();
        renderList(matrices);
        if (drawBorder) {
            WidgetUtils.drawBoxOutline(this, matrices, left(), top(), right() - left(), bottom() - top(), 0xFFFFFFFF);
        }

        // drawCenteredText(matrices, tr, new LiteralText(String.format(
        //     "x:%s y:%s w:%s h:%s",
        //     x, y, w, h
        // )), x, y, 0xff_00ff00);
        // WidgetUtils.drawCenteredText(matrices, tr, new LiteralText("A"), x + w - scrollBarW, y, w, h, true, false, false, 0xff_ff0000);
    }

    public void renderScrollbar() {
        Tessellator   ts = Tessellator.getInstance();
        BufferBuilder bb = ts.getBuffer();
        int           ch = getContentHeight();
        int           eh = h - pad.y * 2;
        if (ch == 0) return;
        int scroll = (eh) / ch;
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

        int scrollHeight = (int) (eh / (float) getContentHeight() * eh);
        int scrollTop    = (int) (scrollOffset / (float) getContentHeight() * eh);

        // left right top bottom
        Utils.drawQuad(ts, bb, scrollBarX, x + w, y + pad.y, y + eh + pad.y, colScrollBg, false);
        Utils.drawQuad(ts, bb, scrollBarX, x + w, y + scrollTop + pad.y, y + scrollTop + scrollHeight + pad.y, colScrollFg, false);

        ts.draw();
    }

    public void renderList(MatrixStack matrices) {
        for (int i = 0; i < entries.size(); i++) {
            T   entry = entries.get(i);
            int _x    = x + pad.x;
            int _y    = y + i * entryHeight - (int) scrollOffset + pad.y;
            int _w    = w - scrollBarW - pad.x * 2;
            int _h    = entryHeight;

            if (_y + _h < top() + entryHeight - 1 || _y > bottom() - entryHeight - 1) continue;

            entry.render(matrices, _x, _y, _w, _h, entry == hoveredEntry, 0);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!hovered) return false;

        scrollOffset += amount * -12;
        if (scrollOffset < 0) scrollOffset = 0;
        if (scrollOffset > getContentHeight() - h + pad.y * 2)
            scrollOffset = Math.max(getContentHeight() - h + pad.y * 2, 0);

        return true;
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
        if (getContentHeight() == 0) return false;
        int scroll = h / getContentHeight();
        if (scroll >= 1) return false;
        boolean onScroll = false;

        // if (!(mouseX > left() && mouseX < right() && mouseY > top() && mouseY < bottom())) return false;
        if (!dragging) return false;

        if (lastClickX > scrollBarX) {
            onScroll = true;
        }

        if (mouseY - y != lastDragY) {
            if (onScroll) {
                // mojank magic, still doesn't make much sense for me tho
                double d = Math.max(1d, getContentHeight() - h);
                int    j = Math.min(h, Math.max(32, (int) ((float) (h * h) / (float) (this.getContentHeight() + 10))));
                double e = Math.max(1d, d / (double) (h - j));

                setScrollOffsetR(deltaY * e * 0.85);
            } else {
                double delta = (mouseY - y - lastClickY) * -1;
                lastDragY = (mouseY - y);
                setScrollOffset(lastScrollPosY + delta);
            }
        }
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        if (Math.abs(lastClickY - (mouseY - y)) < 3) {
            if (selectedEntry != null) selectedEntry.selected = false;
            if (!(canDeselectEntry && hoveredEntry == null)) {
                selectedEntry = hoveredEntry;
            }
            if (selectedEntry != null) {
                selectedEntry.selected = true;
                playClickSound();
                if (onSelectedAction != null) onSelectedAction.run(this);
            }
            return true;
        }
        return false;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
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

        @Override
        public List<Text> getToolTips() {
            return tooltips;
        }

        @Override
        public Text getFirstToolTip() {
            return Utils.safeGet(getToolTips(), 0, null);
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
    }
}
