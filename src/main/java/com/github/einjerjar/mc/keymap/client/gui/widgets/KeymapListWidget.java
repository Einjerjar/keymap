package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.keymap.keys.KeyType;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.registry.KeybindingRegistry;
import com.github.einjerjar.mc.keymap.keys.wrappers.KeyHolder;
import com.github.einjerjar.mc.keymap.keys.wrappers.VanillaKeymap;
import com.github.einjerjar.mc.widgets.EList;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.KeyMapping;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public class KeymapListWidget extends EList<KeymapListWidget.KeymapListEntry> {
    public KeymapListWidget(int itemHeight, int x, int y, int w, int h) {
        super(itemHeight, x, y, w, h);
    }

    public KeymapListWidget(int itemHeight, Rect rect) {
        super(itemHeight, rect);
    }

    public void resetKey() {
        if (itemSelected == null) return;
        if (!(itemSelected.map instanceof VanillaKeymap vk)) return;
        Integer ogCode = vk.getCode().get(0);
        itemSelected.resetKey();
        KeyMapping.resetMapping();
        Integer newCode = vk.getCode().get(0);
        KeybindingRegistry.updateKey(ogCode, newCode, vk);
        itemSelected.selected(false);
        itemSelected = null;
        lastItemSelected = null;
    }

    public void resetAllKeys() {
        for (KeymapListEntry item : items) {
            item.resetKey();
        }
        KeyMapping.resetMapping();
        KeybindingRegistry.loadWithoutClearingSubscribers();
        KeybindingRegistry.notifyAllSubscriber();
        if (itemSelected != null) itemSelected.selected(false);
        itemSelected = null;
        lastItemSelected = null;
    }

    public boolean setKeyForItem(KeymapListEntry item, KeyComboData kd) {
        if (item == null) return false;
        if (!(item.map instanceof VanillaKeymap vk)) return false;
        KeybindingRegistry.updateKey(vk.getCode().get(0), kd.keyCode(), vk);
        vk.setKey(List.of(kd.keyCode()), kd.keyType() == KeyType.MOUSE);
        item.updateTooltips();
        item.selected(false);

        return true;
    }

    public boolean setKeyForSelectedItem(KeyComboData kd) {
        boolean ret = setKeyForItem(itemSelected, kd);
        itemSelected = null;
        return ret;
    }

    public boolean setKeyForLastSelectedItem(KeyComboData kd) {
        boolean ret = setKeyForItem(lastItemSelected, kd);
        lastItemSelected = null;
        return ret;
    }

    @Override public void sort() {
        this.items().sort(Comparator.comparing(o -> o.map.getTranslatedName().getString()));
    }

    @Accessors(fluent = true, chain = true)
    public static class KeymapListEntry extends EListEntry<KeymapListEntry> {
        @Getter KeyHolder map;
        @Getter Component keyString;

        public KeymapListEntry(KeyHolder map, KeymapListWidget container) {
            super(container);
            this.map = map;
            this.keyString = map.getTranslatedName();
            updateTooltips();
        }

        @Override public void updateTooltips() {
            tooltips.clear();
            tooltips.add(new TextComponent(this.keyString.getString()).withStyle(Styles.header()));
            tooltips.add(new TextComponent(String.format("[%s]",
                    Language.getInstance().getOrDefault(this.map().getCategory()))).withStyle(Styles.muted2()));
            tooltips.add(map.getTranslatedKey());
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
            String trimmed = font.substrByWidth(keyString, r.w()).getString();
            drawString(poseStack, font, trimmed, r.x(), r.y(), getVariant().text());
        }
    }
}
