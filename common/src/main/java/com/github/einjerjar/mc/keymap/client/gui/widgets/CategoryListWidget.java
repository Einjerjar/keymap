package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.wrappers.categories.CategoryHolder;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.widgets.EList;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.github.einjerjar.mc.widgets.utils.Styles;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
public class CategoryListWidget extends EList<CategoryListWidget.CategoryListEntry> {

    public CategoryListWidget(int itemHeight, int x, int y, int w, int h) {
        super(itemHeight, x, y, w, h);
    }

    protected CategoryListWidget(int itemHeight, Rect rect) {
        super(itemHeight, rect);
    }

    @Override
    public boolean onMouseReleased(boolean inside, double mouseX, double mouseY, int button) {
        boolean ret = super.onMouseReleased(inside, mouseX, mouseY, button);
        setItemSelected(null);
        setLastItemSelected(null);
        return ret;
    }

    public static class CategoryListEntry extends EListEntry<CategoryListEntry> {
        @Getter
        protected CategoryHolder category;

        @Getter
        protected Component keyString;

        public CategoryListEntry(CategoryHolder category, CategoryListWidget container) {
            super(container);
            this.category = category;
            this.keyString = this.category.getTranslatedName();
            updateTooltips();
        }

        @Override
        public void updateTooltips() {
            tooltips.clear();
            tooltips.add(Component.literal(keyString.getString()).withStyle(Styles.headerBold()));
            tooltips.add(Component.literal(category.getModName()).withStyle(Styles.muted2()));

            if (KeymapConfig.instance().debug()) {
                tooltips.add(Component.literal(Utils.SEPARATOR).withStyle(Styles.muted()));
                tooltips.add(Component.literal(String.format("Search: %s", category.getFilterSlug()))
                        .withStyle(Styles.yellow()));
            }
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics guiGraphics, Rect r, float partialTick) {
            String trimmed = font.substrByWidth(keyString, r.w()).getString();
            guiGraphics.drawString(font, trimmed, r.x(), r.y(), getVariant().text());
        }
    }
}
