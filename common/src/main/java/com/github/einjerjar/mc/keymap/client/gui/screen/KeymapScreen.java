package com.github.einjerjar.mc.keymap.client.gui.screen;

import com.github.einjerjar.mc.keymap.client.gui.widgets.CategoryListWidget;
import com.github.einjerjar.mc.keymap.client.gui.widgets.KeyWidget;
import com.github.einjerjar.mc.keymap.client.gui.widgets.KeymapListWidget;
import com.github.einjerjar.mc.keymap.client.gui.widgets.VirtualKeyboardWidget;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.KeyType;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeymapRegistry;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.keymap.keys.sources.KeymappingNotifier;
import com.github.einjerjar.mc.keymap.keys.sources.category.CategorySource;
import com.github.einjerjar.mc.keymap.keys.sources.category.CategorySources;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSource;
import com.github.einjerjar.mc.keymap.keys.sources.keymap.KeymapSources;
import com.github.einjerjar.mc.keymap.keys.wrappers.categories.CategoryHolder;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.github.einjerjar.mc.keymap.utils.VKUtil;
import com.github.einjerjar.mc.widgets.EButton;
import com.github.einjerjar.mc.widgets.EInput;
import com.github.einjerjar.mc.widgets.EScreen;
import com.github.einjerjar.mc.widgets.EWidget;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class KeymapScreen extends EScreen {
    protected int lastKeyCode;
    protected KeyComboData lastKeyComboData;
    protected VirtualKeyboardWidget vkBasic;
    protected VirtualKeyboardWidget vkExtra;
    protected VirtualKeyboardWidget vkMouse;
    protected VirtualKeyboardWidget vkNumpad;

    protected KeymapListWidget listKm;
    protected CategoryListWidget listCat;
    protected EButton btnReset;
    protected EButton btnResetAll;
    protected EButton btnClearSearch;
    protected EButton btnOpenSettings;
    protected EButton btnOpenLayouts;
    protected EButton btnOpenHelp;
    protected EButton btnOpenCredits;
    protected EInput inpSearch;

    protected List<VirtualKeyboardWidget> vks;

    public KeymapScreen(Screen parent) {
        super(parent, Component.translatable("keymap.scrMain"));
    }

    @Override
    protected void onInit() {
        KeyLayout layout = KeyLayout.getLayoutWithCode(KeymapConfig.instance().customLayout());
        KeymappingNotifier.load();

        scr = scrFromWidth(Math.min(450, width));
        initVks(layout);
        int spaceLeft = scr.w() - padding.x() * 3 - vkBasic.rect().w();

        listKm = new KeymapListWidget(
                font.lineHeight,
                vkBasic.right() + padding.x(),
                vkBasic.top(),
                spaceLeft,
                scr.h() - padding.y() * 4 - 16 * 2);

        listCat = new CategoryListWidget(
                font.lineHeight,
                vkNumpad.right() + padding.x(),
                vkNumpad.top(),
                vkBasic.right() - vkNumpad.right() - padding.x(),
                vkNumpad.rect().h());

        listKm.onItemSelected(this::onListKmSelected);
        listKm.onItemSelected(this::onListKmSelected);
        listCat.onItemSelected(this::onListCatSelected);

        inpSearch =
                new EInput(listKm.left(), scr.y() + padding.y(), listKm.rect().w() - 16 - padding.x(), 16);
        inpSearch.onChanged(this::onSearchChanged);

        btnReset = new EButton(
                Component.translatable("keymap.btnReset"),
                listKm.left(),
                listKm.bottom() + padding.y(),
                (listKm.rect().w() - padding.x()) / 2,
                16);
        btnResetAll = new EButton(
                Component.translatable("keymap.btnResetAll"),
                btnReset.right() + padding.x(),
                listKm.bottom() + padding.y(),
                (listKm.rect().w() - padding.x()) / 2,
                16);

        int vkSplit = (vkBasic.rect().w() - padding.x()) / 4;
        btnOpenSettings = new EButton(
                Component.translatable("keymap.btnOpenSettings"),
                scr.x() + padding.x(),
                scr.y() + padding.y(),
                vkSplit,
                16);
        btnOpenLayouts = new EButton(
                Component.translatable("keymap.btnOpenLayouts"),
                btnOpenSettings.right() + padding.x(),
                scr.y() + padding.y(),
                vkSplit,
                16);
        btnOpenCredits = new EButton(
                Component.translatable("keymap.btnOpenCredits"),
                btnOpenLayouts.right() + padding.x(),
                scr.y() + padding.y(),
                vkSplit,
                16);
        btnOpenHelp = new EButton(
                Component.translatable("keymap.btnOpenHelp"),
                btnOpenCredits.right() + padding.x(),
                scr.y() + padding.y(),
                vkBasic.right() - btnOpenCredits.right() - padding.x(),
                16);
        btnClearSearch = new EButton(
                Component.translatable("keymap.btnClearSearch"), listKm.right() - 16, scr.y() + padding.y(), 16, 16);

        if (KeymapConfig.instance().showHelpTooltips()) {
            btnReset.setTooltip(Component.translatable("keymap.btnResetTip"));
            btnResetAll.setTooltip(Component.translatable("keymap.btnResetAllTip"));
            btnOpenSettings.setTooltip(Component.translatable("keymap.btnOpenSettingsTip"));
            btnOpenLayouts.setTooltip(Component.translatable("keymap.btnOpenLayoutsTip"));
            btnOpenCredits.setTooltip(Component.translatable("keymap.btnOpenCreditsTip"));
            btnOpenHelp.setTooltip(Component.translatable("keymap.btnOpenHelpTip"));
            btnClearSearch.setTooltip(Component.translatable("keymap.btnClearSearchTip2"));
        }

        btnReset.clickAction(this::onBtnResetClicked);
        btnResetAll.clickAction(this::onBtnResetAllClicked);

        btnOpenSettings.clickAction(this::onBtnOpenSettingsClicked);
        btnOpenLayouts.clickAction(this::onBtnOpenLayoutsClicked);
        btnOpenCredits.clickAction(this::onBtnOpenCreditsClicked);
        btnOpenHelp.clickAction(this::onBtnOpenHelpClicked);
        btnClearSearch.clickAction(this::onBtnClearSearchClicked);

        assert minecraft != null;
        for (KeymapSource source : KeymapSources.sources()) {
            if (!source.canUseSource()) continue;
            for (KeyHolder kh : source.getKeyHolders()) {
                listKm.addItem(new KeymapListWidget.KeymapListEntry(kh, listKm));
            }
        }

        for (CategorySource source : CategorySources.sources()) {
            if (!source.canUseSource()) continue;
            for (CategoryHolder categoryHolder : source.getCategoryHolders()) {
                listCat.addItem(new CategoryListWidget.CategoryListEntry(categoryHolder, listCat));
            }
        }

        listKm.updateFilteredList();
        listKm.sort();

        addRenderableWidget(listKm);
        addRenderableWidget(listCat);
        addRenderableWidget(btnReset);
        addRenderableWidget(btnResetAll);
        addRenderableWidget(btnOpenSettings);
        addRenderableWidget(btnOpenLayouts);
        addRenderableWidget(btnOpenCredits);
        addRenderableWidget(btnOpenHelp);
        addRenderableWidget(btnClearSearch);
        addRenderableWidget(inpSearch);
    }

    protected void initVks(KeyLayout layout) {
        vks = VKUtil.genLayout(
                layout,
                scr.x() + padding.x(),
                scr.y() + padding.y() * 2 + 16,
                this::onVKKeyClicked,
                this::onVKSpecialClicked);
        for (VirtualKeyboardWidget vk : vks) {
            for (KeyWidget k : vk.childKeys()) {
                addRenderableWidget(k);
            }
        }

        vkBasic = vks.get(0);
        vkExtra = vks.get(1);
        vkMouse = vks.get(2);
        vkNumpad = vks.get(3);
    }

    @Override
    public void onClose() {
        KeymappingNotifier.clearSubscribers();
        super.onClose();
    }

    protected void onBtnOpenCreditsClicked(EWidget eWidget) {
        assert minecraft != null;
        minecraft.setScreen(new CreditsScreen(this));
    }

    protected void onBtnOpenHelpClicked(EWidget source) {
        assert minecraft != null;
        minecraft.setScreen(new HelpScreen(this));
    }

    protected void onBtnClearSearchClicked(EWidget source) {
        if (inpSearch.text().isEmpty()) {
            onClose();
            return;
        } else inpSearch.text("");
        setFocused(null);
    }

    protected void onListCatSelected(EWidget source) {
        inpSearch.text(listCat.itemSelected().category().getFilterSlug());
        EWidget f = (EWidget) getFocused();
        if (f != null) f.focused(false);
        setFocused(inpSearch);
        inpSearch.focused(true);
        listKm.setItemSelected(null);
    }

    protected void onSearchChanged(EInput source, String newText) {
        listKm.filterString(newText);
        if (newText.isEmpty()) {
            btnClearSearch.setTooltip(Component.translatable("keymap.btnClearSearchTip2"));
        } else {
            btnClearSearch.setTooltip(Component.translatable("keymap.btnClearSearchTip"));
        }
        listKm.setItemSelected(null);
    }

    protected void onBtnOpenSettingsClicked(EWidget source) {
        assert minecraft != null;
        minecraft.setScreen(new ConfigScreen(this));
    }

    protected void onBtnOpenLayoutsClicked(EWidget source) {
        assert minecraft != null;
        minecraft.setScreen(new LayoutSelectionScreen(this));
    }

    protected void onBtnResetClicked(EWidget source) {
        listKm.resetKey();
        setFocused(null);
    }

    protected void onBtnResetAllClicked(EWidget source) {
        listKm.resetAllKeys();
        setFocused(null);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        EWidget focus = (EWidget) getFocused();
        if (focus == null && keyCode == InputConstants.KEY_F && hasControlDown()) {
            setFocused(inpSearch);
            inpSearch.focused(true);
            return true;
        }
        if (focus != null && focus.keyPressed(keyCode, scanCode, modifiers)) return true;
        if (focus != null && keyCode == InputConstants.KEY_ESCAPE && focus != listKm) {
            focus.focused(false);
            setFocused(null);
            return true;
        }
        if (keyCode == InputConstants.KEY_ESCAPE && focus == null) {
            onClose();
            return true;
        }
        KeyComboData kd = new KeyComboData(keyCode, KeyType.KEYBOARD, hasAltDown(), hasShiftDown(), hasControlDown());
        lastKeyCode = keyCode;
        lastKeyComboData = kd;

        return !KeymapRegistry.MODIFIER_KEYS().contains(keyCode);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (getFocused() != listKm) {
            return super.keyReleased(keyCode, scanCode, modifiers);
        }
        if (lastKeyComboData == null) return false;

        // if last released is a modifier, even tho the last pressed was not, then ignore
        if (KeymapRegistry.MODIFIER_KEYS().contains(keyCode) && lastKeyCode != keyCode) return false;

        listKm.setKey(lastKeyComboData);
        setFocused(null);
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (getFocused() == listKm && hoveredWidget != listKm) {
            listKm.setItemSelected(null);
        }
        return false;
    }

    private void onVKKeyClicked(VirtualKeyboardWidget source) {
        if (source.lastActionFrom() == null) return;
        KeyComboData kd = new KeyComboData(
                source.lastActionFrom().key().code(),
                source.lastActionFrom().key().mouse() ? KeyType.MOUSE : KeyType.KEYBOARD,
                false,
                false,
                false);
        if (!listKm.setKey(kd)) {
            inpSearch.text(String.format(
                    "[%s]",
                    source.lastActionFrom().mcKey().getDisplayName().getString().toLowerCase()));
        }
    }

    private void onVKSpecialClicked(VirtualKeyboardWidget source, KeyWidget keySource, int button) {
        if (keySource == null) return;
        if (keySource.key().code() == -2) {
            KeyComboData kd = new KeyComboData(button, KeyType.MOUSE, false, false, false);
            listKm.setKey(kd);
        }
    }

    private void onListKmSelected(EWidget source) {
        // was for debugging, ignore for now
    }

    @Override
    protected void preRenderScreen(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, width, height, 0x55000000);
        if (scr != null) drawOutline(guiGraphics, scr, 0xFFFFFFFF);
    }
}
