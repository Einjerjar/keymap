package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeymapRegistry;
import com.github.einjerjar.mc.keymap.keys.layout.KeyData;
import com.github.einjerjar.mc.keymap.keys.sources.KeymappingNotifier;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.widgets.EWidget;
import com.github.einjerjar.mc.widgets.utils.ColorGroups;
import com.github.einjerjar.mc.widgets.utils.ColorSet;
import com.github.einjerjar.mc.widgets.utils.Styles;
import com.mojang.blaze3d.platform.InputConstants;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true, chain = true)
public class KeyWidget extends EWidget implements KeymappingNotifier.KeybindingRegistrySubscriber {
    @Getter
    protected KeyData key;

    @Getter
    protected InputConstants.Key mcKey;

    @Setter
    protected SimpleWidgetAction<KeyWidget> onClick;

    @Setter
    protected SpecialKeyWidgetAction onSpecialClick;

    @Getter
    @Setter
    protected boolean selected = false;

    protected Component text;
    protected boolean hasComplex = false;

    public KeyWidget(KeyData key, int x, int y, int w, int h) {
        super(x, y, w, h);
        _init(key);
    }

    protected void _init(KeyData key) {
        this.key = key;
        this.text = Component.literal(key.name());
        this.mcKey = getMCKey(key);
        this.tooltips = new ArrayList<>();
        this.tooltips.add(mcKey.getDisplayName());
        if (key.code() == -2) {
            this.allowRightClick = true;
            for (int i = 0; i < 10; i++) {
                KeymappingNotifier.subscribe(i, this);
            }
        } else {
            KeymappingNotifier.subscribe(key.code(), this);
        }
        updateTooltips();
    }

    public void destroy() {
        if (key.code() == -2) {
            for (int i = 0; i < 10; i++) {
                KeymappingNotifier.unsubscribe(i, this);
            }
        } else {
            KeymappingNotifier.unsubscribe(key.code(), this);
        }
    }

    public boolean isNormal() {
        return key.code() != -2;
    }

    public boolean updateTooltipForOtherMouseKeys() {
        tooltips.add(Component.literal(text.getString()).withStyle(Styles.header()));
        List<Component> boundKeys = new ArrayList<>();
        // 10 mouse keys
        for (int i = 0; i < 10; i++) {
            if (KeymappingNotifier.keys().containsKey(i)) {
                for (KeyHolder k : KeymappingNotifier.keys().get(i)) {
                    boundKeys.add(Component.literal(
                            String.format("[%s] %s", i, k.getTranslatedName().getString())));
                }
            }
        }
        int size = boundKeys.size();

        this.color(ColorGroups.WHITE);
        if (size > 0) {
            this.color(ColorGroups.GREEN);
            tooltips.add(Component.literal(Utils.SEPARATOR).withStyle(Styles.muted()));
            tooltips.addAll(boundKeys);
        }
        if (selected) this.color(ColorGroups.YELLOW);
        return true;
    }

    public boolean updateSpecialTooltip() {
        // redundant check, but unlike isSpecial, actually tracks whatever makes it different
        if (key.code() == -2) {
            return updateTooltipForOtherMouseKeys();
        }
        return false;
    }

    protected void updateDebugTooltips() {
        if (KeymapConfig.instance().debug()) {
            tooltips.add(Component.literal(Utils.SEPARATOR).withStyle(Styles.muted()));
            tooltips.add(
                    Component.literal(String.format("Code: %d", key.code())).withStyle(Styles.yellow()));
            tooltips.add(
                    Component.literal(String.format("Mouse?: %b", key.mouse())).withStyle(Styles.yellow()));
            tooltips.add(
                    Component.literal(String.format("Name: %s", key.name())).withStyle(Styles.yellow()));
        }
    }

    // TODO: Intellij/Sonarlint complaining about cognitive complexity
    public void updateNormalTooltip() {
        tooltips.add(Component.literal(String.format("(%s) ", text.getString()))
                .withStyle(Styles.yellow())
                .append(Component.literal(mcKey.getDisplayName().getString()).withStyle(Styles.headerBold())));

        hasComplex = KeymapRegistry.containsKey(key.code());
        if (KeymappingNotifier.keys().containsKey(key.code())) {
            List<KeyHolder> holders = List.copyOf(KeymappingNotifier.keys().get(key.code()));

            int size = holders.size();
            switch (size) {
                case 0 -> color(ColorGroups.WHITE);
                case 1 -> color(ColorGroups.GREEN);
                default -> color(ColorGroups.RED);
            }

            if (size > 0) {
                tooltips.add(Component.literal(Utils.SEPARATOR).withStyle(Styles.muted()));
                for (KeyHolder k : holders) {
                    tooltips.add(k.getTranslatedName());
                }
            }
        } else {
            color(ColorGroups.WHITE);
        }

        if (hasComplex) {
            List<KeyMapping> m = KeymapRegistry.getMappings(key.code());
            if (m.isEmpty() && tooltips.size() == 1)
                tooltips.add(Component.literal(Utils.SEPARATOR).withStyle(Styles.muted()));
            for (KeyMapping km : m) {
                tooltips.add(Component.translatable(km.getName()));
            }
        }
        if (selected) color(ColorGroups.YELLOW);
    }

    @Override
    public void updateTooltips() {
        tooltips.clear();
        if (!updateSpecialTooltip()) {
            updateNormalTooltip();
        }
        updateDebugTooltips();
    }

    protected InputConstants.Key getMCKey(KeyData key) {
        if (key.mouse()) return InputConstants.Type.MOUSE.getOrCreate(key.code());
        return InputConstants.Type.KEYSYM.getOrCreate(key.code());
    }

    @Override
    public boolean onMouseReleased(boolean inside, double mouseX, double mouseY, int button) {
        if (isNormal()) {
            if (onClick != null) {
                onClick.run(this);
                return true;
            }
        } else {
            if (onSpecialClick != null) {
                onSpecialClick.run(this, button);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ColorSet colors = colorVariant();
        drawBg(guiGraphics, colors.bg());
        if (hasComplex) {
            guiGraphics.fill(right() - 6, bottom() - 6, right(), bottom(), 0xFF_ffff00);
        }
        drawOutline(guiGraphics, colors.border());
        guiGraphics.drawCenteredString(font, text, midX(), midY() - font.lineHeight / 2 + 1, colors.text());
    }

    @Override
    public void keybindingRegistryUpdated(boolean selected) {
        this.selected = selected;
        updateTooltips();
    }

    public interface SpecialKeyWidgetAction {
        void run(KeyWidget source, int button);
    }
}
