package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.KeyType;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeymapRegistry;
import com.github.einjerjar.mc.keymap.keys.sources.KeymappingNotifier;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.VanillaKeymap;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.widgets.EList;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.github.einjerjar.mc.widgets.utils.Styles;
import com.github.einjerjar.mc.widgets.utils.Text;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Accessors(fluent = true)
public class KeymapListWidget extends EList<KeymapListWidget.KeymapListEntry> {

    protected final List<KeymapListEntry> filteredItemList = new ArrayList<>();

    @Getter protected String filterString = "";

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

    @Override protected void setSelected(KeymapListEntry i, boolean selected) {
        if (i != null) {
            i.selected(selected);
            KeymappingNotifier.notifySubscriber(i.map.getSingleCode(), selected);
        }
    }

    @Override public void setItemSelected(KeymapListEntry t) {
        setLastItemSelected(itemSelected);
        setSelected(itemSelected, false);
        itemSelected = t;
        setSelected(itemSelected, true);
    }

    @Override public void setLastItemSelected(KeymapListEntry t) {
        setSelected(lastItemSelected, false);
        lastItemSelected = t;
        setSelected(lastItemSelected, true);
    }

    public void resetKey() {
        KeymapListEntry ix = itemSelected != null ? itemSelected : lastItemSelected;
        if (ix == null) return;
        if (!(ix.map instanceof VanillaKeymap vk)) return;
        setKey(new KeyComboData(vk.map().getDefaultKey()));
    }

    public void resetAllKeys() {
        KeymapRegistry.resetAll();
        for (KeymapListEntry item : items) {
            item.resetKey();
        }
        KeyMapping.resetMapping();
        KeymappingNotifier.loadKeys();
        KeymappingNotifier.notifyAllSubscriber();
        updateAllEntryTooltips();
        // twice to flush both current and last selected
        setItemSelected(null);
        setItemSelected(null);
    }

    public boolean setKeyForItem(KeyComboData kd) {
        return setKeyForItem(kd, false);
    }

    public boolean setKeyForItem(KeyComboData kd, boolean removeKeybindReg) {
        KeymapListEntry item = itemSelected != null ? itemSelected : lastItemSelected;
        if (item == null) return false;
        if (!(item.map instanceof VanillaKeymap vk)) return false;

        int code = kd.keyCode();

        if (code == InputConstants.KEY_ESCAPE) {
            code = -1;
        }

        Keymap.logger().warn(code);

        KeymappingNotifier.updateKey(vk.getCode().get(0), code, vk);
        vk.setKey(List.of(code), kd.keyType() == KeyType.MOUSE);
        item.updateTooltips();
        item.selected(false);

        if (removeKeybindReg) {
            KeymapRegistry.remove(vk.map());
        }

        return true;
    }

    public boolean setKey(KeyComboData kd) {
        KeymapListEntry item = itemSelected != null ? itemSelected : lastItemSelected;

        Keymap.logger().warn(kd);

        if (!(item != null && item.map instanceof VanillaKeymap vk)) return false;
        int lastCode = vk.getSingleCode();
        int newCode  = kd.keyCode() == InputConstants.KEY_ESCAPE ? -1 : kd.keyCode();

        int          ko = KeymappingNotifier.keyOf(vk);
        int          kk = ko;
        KeyComboData kl = KeymapRegistry.bindMap().get(vk.map());
        if (kl != null) kk = kl.keyCode();

        if (kd.onlyKey()) {
            KeymapRegistry.remove(vk.map());
            KeymappingNotifier.updateKey(lastCode, newCode, vk);
            vk.setKey(List.of(newCode), kd.keyType() == KeyType.MOUSE);
        } else {
            setComplexKey(kd, vk, lastCode, ko);
        }

        KeymappingNotifier.notifySubscriber(ko, false);
        KeymappingNotifier.notifySubscriber(kk, false);
        KeymappingNotifier.notifySubscriber(newCode, false);

        item.updateTooltips();
        item.selected(false);

        // twice to flush both current and last selected
        setItemSelected(null);
        setItemSelected(null);
        return true;
    }

    private void setComplexKey(KeyComboData kd, VanillaKeymap vk, int lastCode, int ko) {
        Keymap.logger().error(2);
        // FIXME: messy code
        KeyMapping vItem = null;
        if (KeymapRegistry.bindMap().inverse().containsKey(kd)) {
            vItem = KeymapRegistry.bindMap().inverse().get(kd);
        }
        KeymapRegistry.put(vk.map(), kd);

        KeymappingNotifier.updateKey(ko == -99 ? lastCode : ko, -1, vk);
        vk.setKey(List.of(-1), false);

        // when moving combo to another key, the last owner of the combo needs to get updated too
        if (vItem != null) {
            for (KeymapListEntry entry : items) {
                if (entry.map instanceof VanillaKeymap vvk && vvk.map() == vItem) {
                    entry.updateTooltips();
                    break;
                }
            }
        }
    }

    protected void updateAllEntryTooltips() {
        for (KeymapListEntry item : items) item.updateTooltips();
    }

    @Override public void sort() {
        this.items().sort(Comparator.comparing(o -> o.map.getTranslatedName().getString()));
        this.filteredItemList.sort(Comparator.comparing(o -> o.map.getTranslatedName().getString()));
    }

    @Accessors(fluent = true, chain = true)
    public static class KeymapListEntry extends EListEntry<KeymapListEntry> {
        protected static final String    CHAR_ASSIGNED   = "⬛ ";
        protected static final String    CHAR_UNASSIGNED = "⬜ ";
        protected static       Integer   CHAR_ASSIGN_W   = null;
        @Getter protected      KeyHolder map;
        @Getter protected      Component keyString;

        public KeymapListEntry(KeyHolder map, KeymapListWidget container) {
            super(container);
            this.map       = map;
            this.keyString = map.getTranslatedName();
            updateTooltips();
        }

        protected static int charW() {
            if (CHAR_ASSIGN_W == null) {
                CHAR_ASSIGN_W = Minecraft.getInstance().font.width(CHAR_ASSIGNED);
            }
            return CHAR_ASSIGN_W;
        }

        protected void updateDebugTooltips() {
            if (KeymapConfig.instance().debug()) {
                tooltips.add(Text.literal(Utils.SEPARATOR).withStyle(Styles.muted()));
                tooltips.add(Text.literal(String.format("Search: %s",
                        map.getSearchString())).withStyle(Styles.yellow()));
            }
        }

        @Override public void updateTooltips() {
            tooltips.clear();
            tooltips.add(Text.literal(this.keyString.getString()).withStyle(Styles.headerBold()));
            tooltips.add(Text.literal(String.format("[@%s #%s]",
                    Language.getInstance().getOrDefault(this.map().getCategory()),
                    Language.getInstance().getOrDefault(this.map().getModName())
            )).withStyle(Styles.muted2()));
            tooltips.add(map.isAssigned() ? map.getTranslatedKey() : Text.translatable("key.keyboard.unknown"));
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
            String trimmed = font.substrByWidth(keyString, r.w() - charW()).getString();
            drawString(poseStack, font,
                    map.isAssigned()
                    ? CHAR_ASSIGNED
                    : CHAR_UNASSIGNED, r.x(), r.y(),
                    map.isAssigned()
                    ? color.active().text()
                    : color.normal().text());
            drawString(poseStack, font, trimmed, r.x() + charW(), r.y(), getVariant().text());
        }
    }
}
