package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.keymap.keys.wrappers.KeyHolder;
import com.github.einjerjar.mc.widgets.EList;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class KeymapListWidget extends EList<KeymapListWidget.KeymapListEntry> {
    public KeymapListWidget(int itemHeight, int x, int y, int w, int h) {
        super(itemHeight, x, y, w, h);
    }

    public KeymapListWidget(int itemHeight, Rect rect) {
        super(itemHeight, rect);
    }

    @Accessors(fluent = true, chain = true)
    public static class KeymapListEntry extends EListEntry<KeymapListEntry> {
        @Getter KeyHolder map;
        @Getter Component keyString;

        public KeymapListEntry(KeyHolder map, KeymapListWidget container) {
            super(container);
            this.map = map;
            this.keyString = map.getTranslatedName();
        }

        @Override public String toString() {
            return "KeymapListEntryWidget{" +
                    "keyString=" + keyString.getString() +
                    '}';
        }

        public void resetKey() {
            map.resetKey();
        }

        @Override
        public void renderWidget(@NotNull PoseStack poseStack, Rect r, float partialTick) {
            drawString(poseStack, font, keyString, r.x(), r.y(), getVariant().text());
        }
    }
}
