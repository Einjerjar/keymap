package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.KeyType;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.registry.KeybindingRegistry;
import com.github.einjerjar.mc.keymap.keys.wrappers.holders.KeyHolder;
import com.github.einjerjar.mc.keymap.keys.wrappers.holders.VanillaKeymap;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.widgets.EList;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.github.einjerjar.mc.widgets.utils.Styles;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.KeyMapping;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Accessors(fluent = true)
public class KeymapListWidget extends EList<KeymapListWidget.KeymapListEntry> {

    @Getter protected String                filterString     = "";
    protected         List<KeymapListEntry> filteredItemList = new ArrayList<>();

    public KeymapListWidget(int itemHeight, int x, int y, int w, int h) {
        super(itemHeight, x, y, w, h);
        updateFilteredList();
    }

    @Override protected List<KeymapListEntry> filteredItems() {
        return filteredItemList;
    }

    public KeymapListWidget filterString(String s) {
        this.filterString = s.trim().toLowerCase();
        updateFilteredList();
        return this;
    }

    @Override public void updateFilteredList() {
        filteredItemList.clear();
        if (filterString.trim().isBlank()) {
            filteredItemList.addAll(items);
        } else {
            for (KeymapListEntry item : items) {
                List<String> segments = Arrays.stream(filterString.split(" ")).filter(s -> !s.isBlank()).toList();
                long matches = segments.stream()
                        .sequential()
                        .filter(s -> item.map
                                .getSearchString()
                                .contains(s)).toList().size();
                if (matches == segments.size()) {
                    filteredItemList.add(item);
                }
            }
        }
        setScrollPos(0);
    }

    public void resetKey() {
        if (itemSelected == null) return;
        if (!(itemSelected.map instanceof VanillaKeymap vk)) return;
        Integer ogCode = vk.getCode().get(0);
        itemSelected.resetKey();
        KeyMapping.resetMapping();
        Integer newCode = vk.getCode().get(0);
        KeybindingRegistry.updateKey(ogCode, newCode, vk);
        setItemSelected(null);
        setLastItemSelected(null);
    }

    // FIXME: Redundant code
    @Override protected void setItemSelected(KeymapListEntry t) {
        if (itemSelected != null) {
            itemSelected.selected(false);
            KeybindingRegistry.notifySubscriber(itemSelected.map.getCode().get(0), false);
        }
        itemSelected = t;
        if (t != null) {
            itemSelected.selected(true);
            KeybindingRegistry.notifySubscriber(itemSelected.map.getCode().get(0), true);
        }
    }

    @Override protected void setLastItemSelected(KeymapListEntry t) {
        if (lastItemSelected != null) {
            lastItemSelected.selected(false);
            KeybindingRegistry.notifySubscriber(lastItemSelected.map.getCode().get(0), false);
        }
        lastItemSelected = t;
        if (t != null) {
            lastItemSelected.selected(true);
            KeybindingRegistry.notifySubscriber(lastItemSelected.map.getCode().get(0), true);
        }
    }

    public void resetAllKeys() {
        for (KeymapListEntry item : items) {
            item.resetKey();
        }
        KeyMapping.resetMapping();
        KeybindingRegistry.loadWithoutClearingSubscribers();
        KeybindingRegistry.notifyAllSubscriber();
        setItemSelected(null);
        setLastItemSelected(null);
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
        setItemSelected(null);
        return ret;
    }

    public boolean setKeyForLastSelectedItem(KeyComboData kd) {
        boolean ret = setKeyForItem(lastItemSelected, kd);
        setLastItemSelected(null);
        return ret;
    }

    @Override public void sort() {
        this.items().sort(Comparator.comparing(o -> o.map.getTranslatedName().getString()));
        this.filteredItemList.sort(Comparator.comparing(o -> o.map.getTranslatedName().getString()));
    }

    @Accessors(fluent = true, chain = true)
    public static class KeymapListEntry extends EListEntry<KeymapListEntry> {
        @Getter protected KeyHolder map;
        @Getter protected Component keyString;

        public KeymapListEntry(KeyHolder map, KeymapListWidget container) {
            super(container);
            this.map       = map;
            this.keyString = map.getTranslatedName();
            updateTooltips();
        }

        protected void updateDebugTooltips() {
            if (KeymapConfig.instance().debug()) {
                tooltips.add(new TextComponent(Utils.SEPARATOR).withStyle(Styles.muted()));
                tooltips.add(new TextComponent(String.format("Search: %s",
                        map.getSearchString())).withStyle(Styles.yellow()));
            }
        }

        @Override public void updateTooltips() {
            tooltips.clear();
            tooltips.add(new TextComponent(this.keyString.getString()).withStyle(Styles.header()));
            tooltips.add(new TextComponent(String.format("[@%s #%s]",
                    Language.getInstance().getOrDefault(this.map().getCategory()),
                    Language.getInstance().getOrDefault(this.map().getModName())
            )).withStyle(Styles.muted2()));
            tooltips.add(map.getTranslatedKey());
            updateDebugTooltips();
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
