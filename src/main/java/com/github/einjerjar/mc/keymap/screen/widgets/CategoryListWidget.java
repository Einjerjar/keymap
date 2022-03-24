package com.github.einjerjar.mc.keymap.screen.widgets;

import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;

public class CategoryListWidget extends EntryListWidget<CategoryListWidget.CategoryEntry> {
    public CategoryEntry selected = null;
    public final ArrayList<String> knownCategories = new ArrayList<>();

    public CategoryListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height - 20, top + 10, bottom - 10, itemHeight);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    public int addEntry(CategoryEntry entry) {
        if (!knownCategories.contains(entry.category)) knownCategories.add(entry.category);
        return super.addEntry(entry);
    }

    public boolean removeEntry(CategoryEntry entry) {
        knownCategories.remove(entry.category);
        return super.removeEntry(entry);
    }

    @Override
    public void setSelected(CategoryEntry selected) {
        if (this.selected != null) this.selected.selected = false;
        this.selected = selected;
        if (selected != null) selected.selected = true;
    }

    public void _clearEntries() {
        super.clearEntries();
    }

    @Override
    public int getRowWidth() {
        return width - 10;
    }

    @Override
    protected int getScrollbarPositionX() {
        return width - 6 + left;
    }

    @Override
    public void render(MatrixStack m, int mouseX, int mouseY, float delta) {
        Tessellator ts = Tessellator.getInstance();
        BufferBuilder bb = ts.getBuffer();
        Utils.drawBoxFilled(this, m, left, top - 10, width, height + 20, 0xff_ffffff, 0x33_ffffff);

        renderList(m, this.left, this.top, mouseX, mouseY, delta);
        renderScrollBar(bb, ts);
    }

    public void renderScrollBar(BufferBuilder bb, Tessellator ts) {
        int scrollbarStartX = this.getScrollbarPositionX();
        int scrollbarEndX = scrollbarStartX + 6;
        int maxScroll = this.getMaxScroll();
        if (maxScroll > 0) {
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            int p = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top)) / (float) this.getMaxPosition());
            p = MathHelper.clamp(p, 32, this.bottom - this.top - 8);
            int q = (int) this.getScrollAmount() * (this.bottom - this.top - p) / maxScroll + this.top;
            if (q < this.top) {
                q = this.top;
            }

            int colScrollBg = 0x44_000000;
            int colScrollFg = 0xaa_ffffff;

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            bb.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

            Utils.drawQuad(ts, bb, scrollbarStartX, scrollbarEndX, top, bottom, colScrollBg, false);
            Utils.drawQuad(ts, bb, scrollbarStartX, scrollbarEndX, q, q + p, colScrollFg, false);

            ts.draw();
            RenderSystem.disableBlend();
        }
    }

    public boolean isScrolling(double mouseX, double mouseY, int button) {
        return button == 0 && mouseX >= this.getScrollbarPositionX() && mouseX < (this.getScrollbarPositionX() + 6);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        CategoryEntry e = getEntryAtPosition(mouseX, mouseY);
        if (!isMouseOver(mouseX, mouseY)) return false;
        if (e != null) {
            if (selected != null) selected.selected = false;
            selected = e;

            if (e.mouseClicked(mouseX, mouseY, button)) {
                setFocused(e);
                setDragging(true);
                return true;
            }
        }

        setDragging(true);
        return isScrolling(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        } else if (button == 0 && isScrolling(mouseX, mouseY, button)) {
            if (mouseY < (double) this.top) {
                this.setScrollAmount(0.0D);
            } else if (mouseY > (double) this.bottom) {
                this.setScrollAmount(this.getMaxScroll());
            } else {
                double d = Math.max(1, this.getMaxScroll());
                int i = this.bottom - this.top;
                int j = MathHelper.clamp((int) ((float) (i * i) / (float) this.getMaxPosition()), 32, i - 8);
                double e = Math.max(1.0D, d / (double) (i - j));
                this.setScrollAmount(this.getScrollAmount() + deltaY * e);
            }

            return true;
        }
        return false;
    }

    public static class CategoryEntry extends EntryListWidget.Entry<CategoryEntry> {
        public final String category;
        public boolean selected;
        final TextRenderer tr;

        public CategoryEntry(String category) {
            this.category = category;
            this.tr = MinecraftClient.getInstance().textRenderer;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            selected = true;
            return true;
        }

        @Override
        public void render(MatrixStack m, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int c = ColorGroup.NORMAL.text.normal;
            if (selected)
                c = ColorGroup.GREEN.text.normal;
            String text = category.equals("__ANY__") ? "All" : new TranslatableText(category).getString();
            String sTrim = tr.trimToWidth(text, entryWidth);
            tr.drawWithShadow(m, sTrim, x, y, c);
        }
    }
}
