package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.widgets.utils.Rect;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Accessors(fluent = true)
public class ValueMapList extends EList<ValueMapList.ValueMapEntry<?>> {
    public ValueMapList(int itemHeight, int x, int y, int w, int h, boolean canDeselectItem) {
        super(itemHeight, x, y, w, h);
        this.canDeselectItem = canDeselectItem;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        drawOutline(guiGraphics, 0xff_ffffff);
    }

    public void setItemSelectedWithKey(String key) {
        for (ValueMapEntry<?> filteredItem : filteredItems()) {
            if (Objects.equals(filteredItem.key, key)) {
                setItemSelected(filteredItem);
                return;
            }
        }
    }

    public <T> void setItemSelectedWithValue(T value) {
        for (ValueMapEntry<?> filteredItem : filteredItems()) {
            if (Objects.equals(filteredItem.value, value)) {
                setItemSelected(filteredItem);
                return;
            }
        }
    }

    public static class ValueMapEntry<V> extends EListEntry<ValueMapEntry<?>> {
        @Getter
        protected String key;

        @Getter
        protected V value;

        public ValueMapEntry(String key, V value, ValueMapList container) {
            super(container);
            this.key = key;
            this.value = value;
        }

        @Override
        public void renderWidget(@NotNull GuiGraphics guiGraphics, Rect r, float partialTick) {
            String trimmed = font.plainSubstrByWidth(key, r.w());
            guiGraphics.drawString(font, trimmed, r.x(), r.y(), getVariant().text());
        }
    }
}
