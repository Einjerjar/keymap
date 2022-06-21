package com.github.einjerjar.mc.keymap.client.gui.screen;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.client.gui.widgets.KeyWidget;
import com.github.einjerjar.mc.keymap.client.gui.widgets.KeymapListWidget;
import com.github.einjerjar.mc.keymap.client.gui.widgets.VirtualKeyboardWidget;
import com.github.einjerjar.mc.keymap.keys.KeyType;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeybindRegistry;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.keymap.keys.registry.KeybindingRegistry;
import com.github.einjerjar.mc.keymap.keys.registry.KeymapRegistry;
import com.github.einjerjar.mc.keymap.keys.registry.KeymapSource;
import com.github.einjerjar.mc.keymap.keys.wrappers.KeyHolder;
import com.github.einjerjar.mc.widgets.EButton;
import com.github.einjerjar.mc.widgets.EScreen;
import com.github.einjerjar.mc.widgets.EWidget;
import com.github.einjerjar.mc.widgets.utils.Point;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class KeymapScreen extends EScreen {
    protected int lastKeyCode;
    protected KeyComboData lastKeyComboData;
    protected VirtualKeyboardWidget vkBasic;
    protected VirtualKeyboardWidget vkExtra;
    protected VirtualKeyboardWidget vkMouse;
    protected VirtualKeyboardWidget vkNumpad;

    protected KeymapListWidget listKm;
    protected EButton btnReset;
    protected EButton btnResetAll;
    protected EButton btnClearSearch;
    protected EButton btnOpenSettings;
    protected EButton btnOpenLayouts;

    protected Point<Integer> margin = new Point<>(6);
    protected Point<Integer> padding = new Point<>(4);

    public KeymapScreen(Screen parent) {
        super(parent, new TextComponent("Keymap"));
    }

    @Override protected void onInit() {
        KeyLayout layout = KeyLayout.getLayoutWithCode("en_us");
        KeybindingRegistry.load();

        int expectedScreenWidth = Math.min(400, width);
        Rect scr = new Rect(Math.max((width - expectedScreenWidth) / 2, 0) + margin.x(),
                margin.y(),
                expectedScreenWidth - margin.x() * 2,
                height - margin.y() * 2);

        vkBasic = new VirtualKeyboardWidget(layout.keys().basic(),
                scr.x() + padding.x(),
                scr.y() + padding.y() * 2 + 16,
                0,
                0);
        vkExtra = new VirtualKeyboardWidget(layout.keys().extra(), vkBasic.left(), vkBasic.bottom() + 4, 0, 0);
        vkMouse = new VirtualKeyboardWidget(layout.keys().mouse(), vkExtra.left(), vkExtra.bottom() + 2, 0, 0);
        vkNumpad = new VirtualKeyboardWidget(layout.keys().numpad(), vkMouse.right() + 4, vkBasic.bottom() + 4, 0, 0);

        vkBasic.onKeyClicked(this::onVKKeyClicked);
        vkExtra.onKeyClicked(this::onVKKeyClicked);
        vkMouse.onKeyClicked(this::onVKKeyClicked);
        vkNumpad.onKeyClicked(this::onVKKeyClicked);
        vkExtra.onSpecialKeyClicked(this::onVKSpecialClicked);
        vkBasic.onSpecialKeyClicked(this::onVKSpecialClicked);
        vkMouse.onSpecialKeyClicked(this::onVKSpecialClicked);
        vkNumpad.onSpecialKeyClicked(this::onVKSpecialClicked);

        for (EWidget w : vkBasic.childKeys()) addRenderableWidget(w);
        for (EWidget w : vkExtra.childKeys()) addRenderableWidget(w);
        for (EWidget w : vkMouse.childKeys()) addRenderableWidget(w);
        for (EWidget w : vkNumpad.childKeys()) addRenderableWidget(w);

        int spaceLeft = scr.w() - padding.x() * 3 - vkBasic.rect().w();

        listKm = new KeymapListWidget(font.lineHeight,
                vkBasic.right() + padding.x(),
                vkBasic.top(),
                spaceLeft,
                scr.h() - padding.y() * 4 - 16 * 2);

        listKm.onItemSelected(source -> this.onKeyListClicked((KeymapListWidget) source));

        btnReset = new EButton(new TranslatableComponent("keymap.btnReset"),
                listKm.left(),
                listKm.bottom() + padding.y(),
                (listKm.rect().w() - padding.x()) / 2,
                16);
        btnResetAll = new EButton(new TranslatableComponent("keymap.btnResetAll"),
                btnReset.right() + padding.x(),
                listKm.bottom() + padding.y(),
                (listKm.rect().w() - padding.x()) / 2,
                16);

        btnReset.clickAction(this::onBtnResetClicked);
        btnReset.setTooltip(new TranslatableComponent("keymap.btnResetTip"));
        btnResetAll.clickAction(this::onBtnResetAllClicked);
        btnResetAll.setTooltip(new TranslatableComponent("keymap.btnResetAllTip"));

        int vkHalf = (vkBasic.rect().w() - padding.x()) / 2;
        btnOpenSettings = new EButton(new TranslatableComponent("keymap.btnOpenSettings"),
                scr.x() + padding.x(),
                scr.y() + padding.y(),
                vkHalf,
                16);
        btnOpenLayouts = new EButton(new TranslatableComponent("keymap.btnOpenLayouts"),
                btnOpenSettings.right() + padding.x(),
                scr.y() + padding.y(),
                vkHalf,
                16);
        btnClearSearch = new EButton(new TranslatableComponent("keymap.btnClearSearch"),
                listKm.right() - 16,
                scr.y() + padding.y(),
                16,
                16);

        btnOpenSettings.setTooltip(new TranslatableComponent("keymap.btnOpenSettingsTip"));
        btnOpenLayouts.setTooltip(new TranslatableComponent("keymap.btnOpenLayoutsTip"));

        assert minecraft != null;
        for (KeymapSource source : KeymapRegistry.sources()) {
            if (!source.canUseSource()) continue;
            for (KeyHolder kh : source.getKeyHolders()) {
                listKm.addItem(new KeymapListWidget.KeymapListEntry(kh, listKm));
            }
        }

        listKm.sort();

        addRenderableWidget(listKm);
        addRenderableWidget(btnReset);
        addRenderableWidget(btnResetAll);
        addRenderableWidget(btnOpenSettings);
        addRenderableWidget(btnOpenLayouts);
        addRenderableWidget(btnClearSearch);
    }

    protected void onBtnResetClicked(EWidget source) {
        listKm.resetKey();
    }

    protected void onBtnResetAllClicked(EWidget source) {
        listKm.resetAllKeys();
    }

    protected void processComplexKey() {
        Keymap.logger().error("Complex: {}", lastKeyComboData);
        listKm.setKeyForSelectedItem(lastKeyComboData);
    }

    protected void processSimpleModifiers() {
        Keymap.logger().error("Simple: {}", lastKeyCode);
        listKm.setKeyForSelectedItem(lastKeyComboData);
    }

    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        EWidget focus = (EWidget) getFocused();
        if (focus != null && focus.keyPressed(keyCode, scanCode, modifiers)) return true;
        if (keyCode == InputConstants.KEY_ESCAPE) {
            onClose();
            return true;
        }
        KeyComboData kd = new KeyComboData(keyCode, KeyType.KEYBOARD, hasAltDown(), hasShiftDown(), hasControlDown());
        lastKeyCode = keyCode;
        lastKeyComboData = kd;

        return !KeybindRegistry.MODIFIER_KEYS().contains(keyCode);
    }

    @Override public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (lastKeyComboData == null) return false;
        if (KeybindRegistry.MODIFIER_KEYS().contains(keyCode) && lastKeyCode != keyCode) return false;

        if (KeybindRegistry.MODIFIER_KEYS().contains(keyCode) && lastKeyCode == keyCode) processSimpleModifiers();
        else if (!KeybindRegistry.MODIFIER_KEYS().contains(keyCode) && lastKeyComboData.onlyKey())
            processSimpleModifiers();
        else processComplexKey();
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void onVKKeyClicked(VirtualKeyboardWidget source) {
        if (source.lastActionFrom() == null) return;
        Keymap.logger().warn("Code: [{}], Key: [{}], Mouse: [{}], Special: [{}]",
                source.lastActionFrom().key().code(),
                source.lastActionFrom().key().name(),
                source.lastActionFrom().key().mouse(),
                source.lastActionFrom().isSpecial());
        KeyComboData kd = new KeyComboData(source.lastActionFrom().key().code(),
                source.lastActionFrom().key().mouse() ? KeyType.MOUSE : KeyType.KEYBOARD,
                false,
                false,
                false);
        listKm.setKeyForLastSelectedItem(kd);
    }

    private void onVKSpecialClicked(VirtualKeyboardWidget source, KeyWidget keySource, int button) {
        if (source.lastActionFrom() == null) return;
        if (keySource.key().code() == -2) {
            Keymap.logger().warn("Code: [{}], Key: [{}], Mouse: [{}], Special: [{}], @MouseExtended",
                    button,
                    source.lastActionFrom().key().name(),
                    source.lastActionFrom().key().mouse(),
                    source.lastActionFrom().isSpecial());
            KeyComboData kd = new KeyComboData(button, KeyType.MOUSE, false, false, false);
            listKm.setKeyForLastSelectedItem(kd);
        }
    }

    private void onKeyListClicked(KeymapListWidget source) {
        if (source.itemSelected() == null) return;
        Keymap.logger().info(source.itemSelected().map().getTranslatableName());
    }

    @Override protected void renderScreen(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        // empty just because
    }
}
