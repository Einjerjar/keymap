package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.widgets.EList;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class CategoryListWidget extends EList<CategoryListWidget.CategoryListEntry> {
    protected CategoryListWidget(int itemHeight, int x, int y, int w, int h) {
        super(itemHeight, x, y, w, h);
    }

    protected CategoryListWidget(int itemHeight, Rect rect) {
        super(itemHeight, rect);
    }

    public static class CategoryListEntry extends EListEntry<CategoryListEntry> {
        protected CategoryListEntry(EList<CategoryListEntry> container) {
            super(container);
        }

        @Override public void renderWidget(@NotNull PoseStack poseStack, Rect r, float partialTick) {
            //    temp
        }
    }
}
