package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeybindRegistry;
import com.github.einjerjar.mc.keymap.keys.layout.KeyData;
import com.github.einjerjar.mc.keymap.keys.registry.KeybindingRegistry;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.widgets.EWidget;
import com.github.einjerjar.mc.widgets.utils.ColorGroups;
import com.github.einjerjar.mc.widgets.utils.ColorSet;
import com.github.einjerjar.mc.widgets.utils.Styles;
import com.github.einjerjar.mc.widgets.utils.Text;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true, chain = true)
public class KeyWidget extends EWidget implements KeybindingRegistry.KeybindingRegistrySubscriber {
    @Getter protected         KeyData                       key;
    @Getter protected         InputConstants.Key            mcKey;
    @Setter protected         SimpleWidgetAction<KeyWidget> onClick;
    @Setter protected         SpecialKeyWidgetAction        onSpecialClick;
    @Getter @Setter protected boolean                       selected = false;
    protected                 Component                     text;
    protected boolean hasComplex = false;

    public KeyWidget(KeyData key, int x, int y, int w, int h) {
        super(x, y, w, h);
        _init(key);
    }

    protected void _init(KeyData key) {
        this.key      = key;
        this.text     = Text.literal(key.name());
        this.mcKey    = getMCKey(key);
        this.tooltips = new ArrayList<>();
        this.tooltips.add(mcKey.getDisplayName());
        if (key.code() == -2) {
            this.allowRightClick = true;
            for (int i = 0; i < 10; i++) {
                KeybindingRegistry.addListener(i, this);
            }
        } else {
            KeybindingRegistry.addListener(key.code(), this);
        }
        updateTooltips();
    }

    public void destroy() {
        if (key.code() == -2) {
            for (int i = 0; i < 10; i++) {
                KeybindingRegistry.removeListener(i, this);
            }
        } else {
            KeybindingRegistry.removeListener(key.code(), this);
        }
    }

    public boolean isSpecial() {
        return key.code() == -2;
    }

    public boolean updateTooltipForOtherMouseKeys() {
        tooltips.add(Text.literal(text.getString()).withStyle(Styles.header()));
        List<Component> boundKeys = new ArrayList<>();
        // 10 mouse keys
        for (int i = 0; i < 10; i++) {
            if (KeybindingRegistry.keys().containsKey(i)) {
                for (KeyHolder k : KeybindingRegistry.keys().get(i)) {
                    boundKeys.add(Text.literal(String.format("[%s] %s", i, k.getTranslatedName().getString())));
                }
            }
        }
        int size = boundKeys.size();

        this.color(ColorGroups.WHITE);
        if (size > 0) {
            this.color(ColorGroups.GREEN);
            tooltips.add(Text.literal(Utils.SEPARATOR).withStyle(Styles.muted()));
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
            tooltips.add(Text.literal(Utils.SEPARATOR).withStyle(Styles.muted()));
            tooltips.add(Text.literal(String.format("Code: %d", key.code())).withStyle(Styles.yellow()));
            tooltips.add(Text.literal(String.format("Mouse?: %b", key.mouse())).withStyle(Styles.yellow()));
            tooltips.add(Text.literal(String.format("Name: %s", key.name())).withStyle(Styles.yellow()));
        }
    }

    public void updateNormalTooltip() {
        tooltips.add(Text.literal(String.format("(%s) ",
                text.getString())).withStyle(Styles.yellow()).append(Text.literal(mcKey.getDisplayName().getString()).withStyle(
                Styles.headerBold())));

        hasComplex = KeybindRegistry.containsKey(key.code());
        if (KeybindingRegistry.keys().containsKey(key.code())) {
            List<KeyHolder> holders = KeybindingRegistry.keys().get(key.code());
            int             size    = holders.size();
            if (size <= 0) color(ColorGroups.WHITE);
            if (size == 1) color(ColorGroups.GREEN);
            if (size > 1) color(ColorGroups.RED);

            if (size > 0) {
                tooltips.add(Text.literal(Utils.SEPARATOR).withStyle(Styles.muted()));
                for (KeyHolder k : holders) {
                    tooltips.add(k.getTranslatedName());
                }
            }
            if (hasComplex) {
                List<KeyMapping> m = KeybindRegistry.getMappings(key.code());
                if (m.size() > 0 && tooltips.size() == 1) tooltips.add(Text.literal(Utils.SEPARATOR).withStyle(Styles.muted()));
                for (KeyMapping km : m) {
                    tooltips.add(Text.translatable(km.getName()));
                }
            }
        } else {
            color(ColorGroups.WHITE);
        }
        if (selected) color(ColorGroups.YELLOW);
    }

    @Override public void updateTooltips() {
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

    @Override public boolean onMouseReleased(boolean inside, double mouseX, double mouseY, int button) {
        if (!isSpecial()) {
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

    @Override protected void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        ColorSet colors = colorVariant();
        drawBg(poseStack, colors.bg());
        if (hasComplex) {
            fill(poseStack, right() - 6, bottom() - 6, right(), bottom(), 0xFF_ffff00);
        }
        drawOutline(poseStack, colors.border());
        drawCenteredString(poseStack, font, text, midX(), midY() - font.lineHeight / 2 + 1, colors.text());
    }

    @Override public void keybindingRegistryUpdated(boolean selected) {
        this.selected = selected;
        updateTooltips();
    }

    public interface SpecialKeyWidgetAction {
        void run(KeyWidget source, int button);
    }
}
