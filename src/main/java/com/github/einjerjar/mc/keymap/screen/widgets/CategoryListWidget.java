package com.github.einjerjar.mc.keymap.screen.widgets;

import com.github.einjerjar.mc.keymap.utils.ColorGroup;
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
    public ArrayList<String> knownCategories = new ArrayList<>();

    public CategoryListWidget(MinecraftClient client, int width, int height, int top, int bottom, int itemHeight) {
        super(client, width, height, top, bottom, itemHeight);
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    public int addEntry(CategoryEntry entry) {
        if (!knownCategories.contains(entry.category)) knownCategories.add(entry.category);
        return super.addEntry(entry);
    }

    public boolean removeEntry(CategoryEntry entry) {
        if (knownCategories.contains(entry.category)) knownCategories.remove(entry.category);
        return super.removeEntry(entry);
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

        renderList(m, this.left, this.top, mouseX, mouseY, delta);
        renderScrollBar(bb, ts);
    }

    public void renderScrollBar(BufferBuilder bb, Tessellator ts) {
        int scrollbarStartX = this.getScrollbarPositionX();
        int scrollbarEndX = scrollbarStartX + 6;
        int maxScroll = this.getMaxScroll();
        if (maxScroll > 0) {
            RenderSystem.disableTexture();
            int p = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top)) / (float) this.getMaxPosition());
            p = MathHelper.clamp(p, 32, this.bottom - this.top - 8);
            int q = (int) this.getScrollAmount() * (this.bottom - this.top - p) / maxScroll + this.top;
            if (q < this.top) {
                q = this.top;
            }

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            bb.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            bb.vertex(scrollbarStartX, this.bottom, 0.0D).color(0, 0, 0, 255).next();
            bb.vertex(scrollbarEndX, this.bottom, 0.0D).color(0, 0, 0, 255).next();
            bb.vertex(scrollbarEndX, this.top, 0.0D).color(0, 0, 0, 255).next();
            bb.vertex(scrollbarStartX, this.top, 0.0D).color(0, 0, 0, 255).next();

            bb.vertex(scrollbarStartX, q + p, 0.0D).color(128, 128, 128, 255).next();
            bb.vertex(scrollbarEndX, q + p, 0.0D).color(128, 128, 128, 255).next();
            bb.vertex(scrollbarEndX, q, 0.0D).color(128, 128, 128, 255).next();
            bb.vertex(scrollbarStartX, q, 0.0D).color(128, 128, 128, 255).next();

            bb.vertex(scrollbarStartX, q + p - 1, 0.0D).color(192, 192, 192, 255).next();
            bb.vertex(scrollbarEndX - 1, q + p - 1, 0.0D).color(192, 192, 192, 255).next();
            bb.vertex(scrollbarEndX - 1, q, 0.0D).color(192, 192, 192, 255).next();
            bb.vertex(scrollbarStartX, q, 0.0D).color(192, 192, 192, 255).next();
            ts.draw();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        CategoryEntry e = getEntryAtPosition(mouseX, mouseY);
        if (selected != null) selected.selected = false;
        selected = e;
        if (e != null)
            e.selected = true;
        return true;
    }

    public static class CategoryEntry extends EntryListWidget.Entry<CategoryEntry> {
        public String category;
        public boolean selected;
        TextRenderer tr;

        public CategoryEntry(String category) {
            this.category = category;
            this.tr = MinecraftClient.getInstance().textRenderer;
        }

        @Override
        public void render(MatrixStack m, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int c = ColorGroup.NORMAL.text.normal;
            if (selected)
                c = ColorGroup.GREEN.text.normal;

            tr.drawWithShadow(m, new TranslatableText(category), x, y, c);
        }
    }
}
