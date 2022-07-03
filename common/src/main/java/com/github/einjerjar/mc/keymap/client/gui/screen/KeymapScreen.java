package com.github.einjerjar.mc.keymap.client.gui.screen;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.client.gui.widgets.CategoryListWidget;
import com.github.einjerjar.mc.keymap.client.gui.widgets.KeyWidget;
import com.github.einjerjar.mc.keymap.client.gui.widgets.KeymapListWidget;
import com.github.einjerjar.mc.keymap.client.gui.widgets.VirtualKeyboardWidget;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.KeyType;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeyComboData;
import com.github.einjerjar.mc.keymap.keys.extrakeybind.KeybindRegistry;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.keymap.keys.registry.KeybindingRegistry;
import com.github.einjerjar.mc.keymap.keys.registry.category.CategoryRegistry;
import com.github.einjerjar.mc.keymap.keys.registry.category.CategorySource;
import com.github.einjerjar.mc.keymap.keys.registry.keymap.KeymapRegistry;
import com.github.einjerjar.mc.keymap.keys.registry.keymap.KeymapSource;
import com.github.einjerjar.mc.keymap.keys.wrappers.categories.CategoryHolder;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.github.einjerjar.mc.keymap.utils.VKUtil;
import com.github.einjerjar.mc.widgets.EButton;
import com.github.einjerjar.mc.widgets.EInput;
import com.github.einjerjar.mc.widgets.EScreen;
import com.github.einjerjar.mc.widgets.EWidget;
import com.github.einjerjar.mc.widgets.utils.Text;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;

public class KeymapScreen extends EScreen {
    protected int                   lastKeyCode;
    protected KeyComboData          lastKeyComboData;
    protected VirtualKeyboardWidget vkBasic;
    protected VirtualKeyboardWidget vkExtra;
    protected VirtualKeyboardWidget vkMouse;
    protected VirtualKeyboardWidget vkNumpad;

    protected KeymapListWidget   listKm;
    protected CategoryListWidget listCat;
    protected EButton            btnReset;
    protected EButton            btnResetAll;
    protected EButton            btnClearSearch;
    protected EButton            btnOpenSettings;
    protected EButton            btnOpenLayouts;
    protected EButton            btnOpenHelp;
    protected EInput             inpSearch;

    protected List<VirtualKeyboardWidget> vks;

    public KeymapScreen(Screen parent) {
        super(parent, Text.translatable("keymap.scrMain"));
    }

    @Override protected void onInit() {
        KeyLayout layout = KeyLayout.getLayoutWithCode(KeymapConfig.instance().customLayout());
        KeybindingRegistry.load();

        scr = scrFromWidth(Math.min(450, width));

        initVks(layout);

        int spaceLeft = scr.w() - padding.x() * 3 - vkBasic.rect().w();

        listKm = new KeymapListWidget(font.lineHeight,
                vkBasic.right() + padding.x(),
                vkBasic.top(),
                spaceLeft,
                scr.h() - padding.y() * 4 - 16 * 2);

        listCat = new CategoryListWidget(
                font.lineHeight,
                vkNumpad.right() + padding.x(),
                vkNumpad.top(),
                vkBasic.right() - vkNumpad.right() - padding.x(),
                vkNumpad.rect().h()
        );


        listKm.onItemSelected(this::onListKmSelected);
        listKm.onItemSelected(this::onListKmSelected);
        listCat.onItemSelected(this::onListCatSelected);

        inpSearch = new EInput(listKm.left(), scr.y() + padding.y(), listKm.rect().w() - 16 - padding.x(), 16);
        inpSearch.onChanged(this::onSearchChanged);

        btnReset    = new EButton(Text.translatable("keymap.btnReset"),
                listKm.left(),
                listKm.bottom() + padding.y(),
                (listKm.rect().w() - padding.x()) / 2,
                16);
        btnResetAll = new EButton(Text.translatable("keymap.btnResetAll"),
                btnReset.right() + padding.x(),
                listKm.bottom() + padding.y(),
                (listKm.rect().w() - padding.x()) / 2,
                16);

        int vkSplit = (vkBasic.rect().w() - padding.x()) / 3;
        btnOpenSettings = new EButton(Text.translatable("keymap.btnOpenSettings"),
                scr.x() + padding.x(),
                scr.y() + padding.y(),
                vkSplit,
                16);
        btnOpenLayouts  = new EButton(Text.translatable("keymap.btnOpenLayouts"),
                btnOpenSettings.right() + padding.x(),
                scr.y() + padding.y(),
                vkSplit,
                16);
        btnOpenHelp     = new EButton(Text.translatable("keymap.btnOpenHelp"),
                btnOpenLayouts.right() + padding.x(),
                scr.y() + padding.y(),
                vkBasic.right() - btnOpenLayouts.right() - padding.x(),
                16);
        btnClearSearch  = new EButton(Text.translatable("keymap.btnClearSearch"),
                listKm.right() - 16,
                scr.y() + padding.y(),
                16,
                16);

        if (KeymapConfig.instance().showHelpTooltips()) {
            btnReset.setTooltip(Text.translatable("keymap.btnResetTip"));
            btnResetAll.setTooltip(Text.translatable("keymap.btnResetAllTip"));
            btnOpenSettings.setTooltip(Text.translatable("keymap.btnOpenSettingsTip"));
            btnOpenLayouts.setTooltip(Text.translatable("keymap.btnOpenLayoutsTip"));
            btnOpenHelp.setTooltip(Text.translatable("keymap.btnOpenHelpTip"));
            btnClearSearch.setTooltip(Text.translatable("keymap.btnClearSearchTip2"));
        }

        btnReset.clickAction(this::onBtnResetClicked);
        btnResetAll.clickAction(this::onBtnResetAllClicked);

        btnOpenSettings.clickAction(this::onBtnOpenSettingsClicked);
        btnOpenLayouts.clickAction(this::onBtnOpenLayoutsClicked);
        btnOpenHelp.clickAction(this::onBtnOpenHelpClicked);
        btnClearSearch.clickAction(this::onBtnClearSearchClicked);

        assert minecraft != null;
        for (KeymapSource source : KeymapRegistry.sources()) {
            if (!source.canUseSource()) continue;
            for (KeyHolder kh : source.getKeyHolders()) {
                listKm.addItem(new KeymapListWidget.KeymapListEntry(kh, listKm));
            }
        }

        for (CategorySource source : CategoryRegistry.sources()) {
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
        addRenderableWidget(btnOpenHelp);
        addRenderableWidget(btnClearSearch);
        addRenderableWidget(inpSearch);
    }

    protected void initVks(KeyLayout layout) {
        vks = VKUtil.genLayout(layout,
                scr.x() + padding.x(),
                scr.y() + padding.y() * 2 + 16,
                this::onVKKeyClicked,
                this::onVKSpecialClicked);
        for (VirtualKeyboardWidget vk : vks) {
            for (KeyWidget k : vk.childKeys()) {
                addRenderableWidget(k);
            }
        }

        vkBasic  = vks.get(0);
        vkExtra  = vks.get(0);
        vkMouse  = vks.get(0);
        vkNumpad = vks.get(0);
    }

    @Override public void onClose() {
        KeybindingRegistry.clearSubscribers();
        super.onClose();
    }

    protected void onBtnOpenHelpClicked(EWidget source) {
        assert minecraft != null;
        minecraft.setScreen(new HelpScreen(this));
    }

    protected void onBtnClearSearchClicked(EWidget source) {
        if (inpSearch.text().isEmpty()) onClose();
        else inpSearch.text("");
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
            btnClearSearch.setTooltip(Text.translatable("keymap.btnClearSearchTip2"));
        } else {
            btnClearSearch.setTooltip(Text.translatable("keymap.btnClearSearchTip"));
        }
        listKm.setItemSelected(null);
    }

    protected void onBtnOpenSettingsClicked(EWidget source) {
        assert minecraft != null;
        minecraft.setScreen(ConfigScreenShared.provider().execute(this));
    }

    protected void onBtnOpenLayoutsClicked(EWidget source) {
        assert minecraft != null;
        minecraft.setScreen(new LayoutSelectionScreen(this));
    }

    protected void onBtnResetClicked(EWidget source) {
        listKm.resetKey();
    }

    protected void onBtnResetAllClicked(EWidget source) {
        listKm.resetAllKeys();
    }

    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
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
        lastKeyCode      = keyCode;
        lastKeyComboData = kd;

        return !KeybindRegistry.MODIFIER_KEYS().contains(keyCode);
    }

    @Override public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (getFocused() != listKm) {
            return super.keyReleased(keyCode, scanCode, modifiers);
        }
        if (lastKeyComboData == null) return false;

        // if last released is a modifier, even tho the last pressed was not, then ignore
        // TODO: let the user know about this
        if (KeybindRegistry.MODIFIER_KEYS().contains(keyCode) && lastKeyCode != keyCode) return false;

        listKm.setKey(lastKeyComboData);
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        if (getFocused() == listKm && hoveredWidget != listKm) {
            listKm.setItemSelected(null);
        }
        return false;
    }

    private void onVKKeyClicked(VirtualKeyboardWidget source) {
        if (source.lastActionFrom() == null) return;
        KeyComboData kd = new KeyComboData(source.lastActionFrom().key().code(),
                source.lastActionFrom().key().mouse() ? KeyType.MOUSE : KeyType.KEYBOARD,
                false,
                false,
                false);
        if (!listKm.setKey(kd)) {
            inpSearch.text(String.format("[%s]",
                    source.lastActionFrom().mcKey().getDisplayName().getString().toLowerCase()));
        }
    }

    private void onVKSpecialClicked(VirtualKeyboardWidget source, KeyWidget keySource, int button) {
        if (keySource == null) return;
        if (keySource.key().code() == -2) {
            // Keymap.logger().warn("Code: [{}], Key: [{}], Mouse: [{}], Special: [{}], @MouseExtended",
            //         button,
            //         source.lastActionFrom().key().name(),
            //         source.lastActionFrom().key().mouse(),
            //         source.lastActionFrom().isSpecial());
            KeyComboData kd = new KeyComboData(button, KeyType.MOUSE, false, false, false);
            listKm.setKey(kd);
        }
    }

    private void onListKmSelected(EWidget source) {
        // was for debugging, ignore for now
    }

    @Override protected void preRenderScreen(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        fill(poseStack, 0, 0, width, height, 0x55000000);
        if (scr != null) drawOutline(poseStack, scr, 0xFFFFFFFF);
    }
}
