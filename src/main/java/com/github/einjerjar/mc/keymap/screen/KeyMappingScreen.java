package com.github.einjerjar.mc.keymap.screen;

import com.github.einjerjar.mc.keymap.KeymapConfig;
import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.BasicKeyData;
import com.github.einjerjar.mc.keymap.keys.CategoryHolder;
import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import com.github.einjerjar.mc.keymap.keys.KeyboardKey;
import com.github.einjerjar.mc.keymap.keys.category.MalilibCategory;
import com.github.einjerjar.mc.keymap.keys.category.VanillaCategory;
import com.github.einjerjar.mc.keymap.keys.key.MalilibKeybind;
import com.github.einjerjar.mc.keymap.keys.key.VanillaKeybind;
import com.github.einjerjar.mc.keymap.screen.containers.HotkeyCapture;
import com.github.einjerjar.mc.keymap.screen.entrylist.FlatCategoryList;
import com.github.einjerjar.mc.keymap.screen.entrylist.FlatKeyList;
import com.github.einjerjar.mc.keymap.screen.widgets.FlatKeyWidget;
import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.FlatButton;
import com.github.einjerjar.mc.keymap.widgets.FlatInput;
import com.github.einjerjar.mc.keymap.widgets.FlatScreen;
import com.github.einjerjar.mc.keymap.widgets.FlatWidgetBase;
import com.github.einjerjar.mc.keymap.widgets.containers.FlexContainer;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.hotkeys.KeybindCategory;
import lombok.ToString;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.*;

public class KeyMappingScreen extends FlatScreen {
    protected final Map<String, CategoryHolder> mappedCategories = new HashMap<>();
    protected final Map<Integer, List<KeybindHolder>> mappedKeybindHolders = new HashMap<>();
    protected final Map<Integer, List<FlatKeyWidget>> mappedKeyWidgets = new HashMap<>();
    protected TextRenderer tr = textRenderer;

    protected int expectedScreenWidth;
    protected SimpleV2 outPad = new SimpleV2(4);
    protected SimpleV2 pad = new SimpleV2(10);
    protected SimpleV2 gap = new SimpleV2(2);
    protected KeyPoint r;

    protected boolean malilib;

    protected FlatInput inputSearch;
    protected FlatKeyList listKeybinds;
    protected FlatCategoryList listCategories;
    protected FlexContainer containerSidebar;
    protected FlexContainer containerSideButtons;
    protected FlatButton buttonResetSelect;
    protected FlatButton buttonResetAll;

    protected HotkeyCapture hotkeyCapture;

    protected FlatKeyWidget lastKey = null;

    public KeyMappingScreen(Screen parent) {
        super(new LiteralText(""), parent);
    }

    public KeyMappingScreen() {
        super(new LiteralText(""));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!firstClick) return false;
        boolean ret = super.mouseReleased(mouseX, mouseY, button);
        if (hotkeyCapture.active()) setFocused(hotkeyCapture, false);
        return ret;
    }

    @Override
    protected void init() {
        assert client != null;

        if (KeymapConfig.instance().autoSelectLayout) {
            KeymapMain.reloadLayouts(client.getLanguageManager().getLanguage().getCode());
        } else {
            KeymapMain.reloadLayouts(KeymapConfig.instance().customLayout);
        }

        tr = textRenderer;
        malilib = KeymapMain.malilibAvailable() && KeymapMain.cfg().malilibSupport;
        expectedScreenWidth = Math.min(500, width) - outPad.x * 2 - pad.x * 2;

        mappedCategories.clear();
        mappedKeyWidgets.clear();
        mappedKeybindHolders.clear();

        r = new KeyPoint(
            Math.max(0, width - expectedScreenWidth) / 2,
            outPad.y + pad.y,
            expectedScreenWidth,
            height - outPad.y * 2 - pad.y * 2);

        SimpleV2 midSpace = new SimpleV2(r.w, r.h);

        KeyPoint kbKeys   = addKeys(KeymapMain.keys().basic(), r.x, r.y);
        KeyPoint kbExtra  = addKeys(KeymapMain.keys().extra(), r.x, kbKeys.bottom + gap.y * 2);
        KeyPoint kbMouse  = addKeys(KeymapMain.keys().mouse(), r.x, kbExtra.bottom + gap.y * 2 + 16);
        KeyPoint kbNumpad = addKeys(KeymapMain.keys().numpad(), kbMouse.right + gap.x * 2, kbExtra.y);

        int leftSpaceX = midSpace.x - kbKeys.w - gap.x * 4;

        inputSearch = new FlatInput(0, 0, leftSpaceX, 16, "");
        listKeybinds = new FlatKeyList(0, 0, leftSpaceX, 50, 10);
        listCategories = new FlatCategoryList(
            kbNumpad.right + gap.x * 2 + pad.x,
            kbNumpad.y + pad.y,
            kbKeys.w - kbNumpad.w - kbMouse.w - gap.x * 9,
            kbNumpad.h - pad.y * 2,
            tr.fontHeight
        );

        hotkeyCapture = new HotkeyCapture(0, 0, width, height);
        hotkeyCapture.active(false);

        buttonResetSelect = new FlatButton(0, 0, 0, 0, new TranslatableText("key.keymap.reset"));
        buttonResetAll = new FlatButton(0, 0, 0, 0, new TranslatableText("key.keymap.reset_all"));

        buttonResetSelect.tooltip(new TranslatableText("key.keymap.tip.reset_selected"));
        buttonResetAll.tooltip(new TranslatableText("key.keymap.tip.reset_all"));

        containerSideButtons = new FlexContainer(0, 0, 0, 16);
        containerSideButtons
            .addChild(buttonResetSelect)
            .addChild(buttonResetAll);

        KeymapMain.LOGGER().info("{} {} {} {}", kbKeys.right + gap.x * 2, r.y, leftSpaceX, midSpace);
        containerSidebar = new FlexContainer(kbKeys.right + gap.x * 4, r.y, leftSpaceX, midSpace.y + pad.y);
        containerSidebar
            .direction(FlexContainer.FlexDirection.COLUMN)
            .addChild(inputSearch, -1)
            .addChild(listKeybinds)
            .addChild(containerSideButtons, -1)
            .gap(10)
            .arrange();

        containerSideButtons.arrange();

        inputSearch.onTextChanged(input -> filterListKeys());
        hotkeyCapture.onCloseAction(cap -> setFocused(null));

        assignHandlers();

        addSelectableChild(containerSidebar);
        addSelectableChild(listCategories);
        addSelectableChild(hotkeyCapture);

        updateMappedKeybinds();
        updateKeyWidgets();

        filterListKeys();
    }

    public void assignHandlers() {
        // Reset Keys
        buttonResetSelect.action(button -> {
            listKeybinds.resetSelected();
            refreshKeys();
        });
        buttonResetAll.action(button -> {
            inputSearch.setText("");
            listKeybinds.resetAll();
            refreshKeys();
        });

        // Category changed
        listCategories.onSelectedAction(w -> {
            inputSearch.setText("");
            listKeybinds.setSelectedEntry(null);
            filterListKeys();
            listKeybinds.setScrollOffset(0);
        });

        // Key pressed on keybind list
        listKeybinds.onKeyChanged((fk, k) -> {
            if (fk.getSelectedEntry() == null) return;
            FlatKeyList.FlatKeyListEntry fke    = fk.getSelectedEntry();
            KeybindHolder                holder = fke.holder();
            if (k == InputUtil.GLFW_KEY_ESCAPE) {
                holder.assignHotKey(new Integer[0], false);
                holder.updateState();

                refreshKeys();
                fke.updateDisplayText();

            } else if (holder instanceof VanillaKeybind) {
                holder.assignHotKey(new Integer[]{k}, false);

                refreshKeys();
                fke.updateDisplayText();

            } else if (malilib && holder instanceof MalilibKeybind) {
                handleHotkeyCapture(fke, k);
            }
        });

        // Gather standard keybinds
        for (KeyBinding kb : client.options.keysAll) {
            String cat = kb.getCategory();
            if (!mappedCategories.containsKey(cat)) {
                mappedCategories.put(cat, new VanillaCategory(cat));
            }
            CategoryHolder catHolder = mappedCategories.get(cat);
            VanillaKeybind vkb       = new VanillaKeybind(kb);
            catHolder.addKeybind(vkb);
        }

        // Gather malilib keybinds
        if (malilib) {
            InputEventHandler handler = (InputEventHandler) InputEventHandler.getInputManager();
            for (KeybindCategory cat : handler.getKeybindCategories()) {
                MalilibCategory c = new MalilibCategory(cat);
                if (mappedCategories.containsKey(cat.getModName()) && mappedCategories.get(cat.getModName()) instanceof MalilibCategory mc) {
                    mc.appendCategory(cat);
                    continue;
                }
                mappedCategories.put(cat.getModName(), c);
            }
        }

        // Gather categories for list
        listCategories.clearEntries();
        listCategories.addEntry(new FlatCategoryList.FlatCategoryEntry("__ALL__"));
        for (CategoryHolder c : mappedCategories.values().stream().sorted(Comparator.comparing(o -> o.categoryName().getString())).toList()) {
            listCategories.addEntry(new FlatCategoryList.FlatCategoryEntry(c.categoryKey()));
            List<KeybindHolder> kbs = c.keybinds();
            for (KeybindHolder kb : kbs) {
                listKeybinds.addEntry(new FlatKeyList.FlatKeyListEntry(kb));
            }
        }
    }

    public void handleHotkeyCapture(FlatKeyList.FlatKeyListEntry fke, int firstKey) {
        handleHotkeyCapture(fke, InputUtil.Type.KEYSYM.createFromCode(firstKey));
    }

    public void handleHotkeyCapture(FlatKeyList.FlatKeyListEntry fke, InputUtil.Key firstKey) {
        if (!(fke.holder() instanceof MalilibKeybind mk)) return;
        setFocused(hotkeyCapture, false);

        hotkeyCapture.clear()
                     .add(firstKey)
                     .updateState()
                     .active(true);
        hotkeyCapture.onOkAction(cap -> {
            List<InputUtil.Key> pressed = hotkeyCapture.pressed();
            Integer[]           _keys   = new Integer[pressed.size()];
            for (int i = 0; i < pressed.size(); i++) {
                _keys[i] = pressed.get(i).getCode();
            }
            mk.assignHotKey(_keys, false);

            refreshKeys();
            fke.updateDisplayText();
            listKeybinds.setSelectedEntry(null);
        });
    }

    public void refreshKeys() {
        if (malilib) ((InputEventHandler) InputEventHandler.getInputManager()).updateUsedKeys();
        KeyBinding.updateKeysByCode();
        updateMappedKeybinds();
        updateKeyWidgets();
    }

    public void updateKeyWidgets() {
        for (List<FlatKeyWidget> kwl : mappedKeyWidgets.values()) {
            for (FlatKeyWidget kw : kwl) {
                kw.updateState();
            }
        }
    }

    public void updateMappedKeybinds() {
        // TODO: Optimize
        mappedKeybindHolders.clear();
        for (String cat : mappedCategories.keySet().stream().sorted().toList()) {
            for (KeybindHolder kb : mappedCategories.get(cat).keybinds()) {
                if (kb.code().size() == 0) continue;
                int kCode = kb.code().get(0);
                if (kCode < 0) kCode += 100;
                if (!mappedKeybindHolders.containsKey(kCode)) mappedKeybindHolders.put(kCode, new ArrayList<>());
                mappedKeybindHolders.get(kCode).add(kb);
            }
        }
    }

    private void filterListKeys() {
        String filter = inputSearch.text().trim().toLowerCase();
        listKeybinds.clearEntries();

        FlatCategoryList.FlatCategoryEntry catE    = listCategories.getSelectedEntry();
        boolean                            isBlank = catE == null || catE.category().equalsIgnoreCase("__ALL__");

        for (CategoryHolder cat : mappedCategories.values().stream().sorted(Comparator.comparing(o -> o.categoryName().getString())).toList()) {
            if (!isBlank && !cat.categoryKey().equals(catE.category())) continue;
            for (KeybindHolder kb : cat.keybinds()) {
                String keyFilter = String.format("[%s]", kb.keyTranslation().getString().toLowerCase());
                if (kb instanceof MalilibKeybind mk) {
                    keyFilter = mk.getKeysString().toLowerCase();
                }
                if (
                    filter.isBlank()
                        || kb.translation().getString().toLowerCase().contains(filter)
                        || keyFilter.contains(filter)
                )
                    listKeybinds.addEntry(new FlatKeyList.FlatKeyListEntry(kb));
            }
        }
    }

    private void keyWidgetAction(KeyboardKey key, FlatKeyWidget k) {
        FlatKeyList.FlatKeyListEntry fke = listKeybinds.getSelectedEntry();
        if (fke != null) {
            KeybindHolder holder = fke.holder();
            if (holder instanceof VanillaKeybind vk) {
                vk.assignHotKey(new Integer[]{k.key().keyCode()}, key.type() == InputUtil.Type.MOUSE);
                updateMappedKeybinds();
                updateKeyWidgets();
                fke.updateDisplayText();
                listKeybinds.setSelectedEntry(null);
            } else if (malilib && holder instanceof MalilibKeybind mk) {
                if (key.type() == InputUtil.Type.MOUSE) {
                    mk.assignHotKey(new Integer[]{key.keyCode() - 100}, false);

                    refreshKeys();
                    fke.updateDisplayText();
                    listKeybinds.setSelectedEntry(null);
                    return;
                }
                handleHotkeyCapture(fke, k.key().key());
            }
            return;
        }
        listCategories.setSelectedEntry(null);
        inputSearch.setText(String.format(
            "[%s",
            k.key().key().getLocalizedText().getString()
        ), true);
        setFocused(inputSearch);
    }

    private KeyPoint addKeys(List<List<BasicKeyData>> keys, int x, int y) {
        int sizeX    = 0;
        int sizeY    = 0;
        int currentX = x;
        int currentY = y;

        for (List<BasicKeyData> row : keys) {
            int minItemHeight = height;
            for (BasicKeyData keyData : row) {
                KeyboardKey   key  = new KeyboardKey(keyData);
                FlatKeyWidget k    = new FlatKeyWidget(currentX, currentY, key, mappedKeybindHolders);
                int           code = key.keyCode();

                k.action(button -> keyWidgetAction(key, k));

                if (!mappedKeyWidgets.containsKey(code)) {
                    mappedKeyWidgets.put(code, new ArrayList<>());
                }
                mappedKeyWidgets.get(key.keyCode()).add(k);
                addSelectableChild(k);
                currentX += gap.x + k.w();
                minItemHeight = Math.min(k.h(), minItemHeight);
            }
            sizeX = Math.max(currentX - x, sizeX);
            currentX = x;
            currentY += gap.x + minItemHeight;
            sizeY = currentY - y;
        }

        return new KeyPoint(x, y, sizeX - gap.x, sizeY - gap.y);
    }

    @Override
    public void renderScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        WidgetUtils.drawBoxOutline(this, matrices, r.left - pad.x, r.top - pad.y, r.w + pad.x * 2, r.h + pad.y * 2, 0xff_ffffff);

        WidgetUtils.drawBoxOutline(this, matrices,
            listCategories.x() - pad.x,
            listCategories.y() - pad.y,
            listCategories.w() + pad.x,
            listCategories.h() + pad.y * 2,
            ColorGroup.NORMAL.border.normal);

        FlatKeyList.FlatKeyListEntry fe    = listKeybinds.getSelectedEntry();
        boolean                      feOk  = false;
        int                          feKey = -1;
        if (fe != null) {
            feOk = true;
            feKey = Utils.safeGet(fe.holder().code(), 0, -1);
        }

        if (!feOk && lastKey != null) {
            lastKey.selected(false);
            lastKey.updateState();
        }

        for (Element e : children()) {
            if (e instanceof FlatWidgetBase ee) {
                if (ee.visible()) {
                    if (feOk && ee instanceof FlatKeyWidget fkw && lastKey != fkw && fkw.key().keyCode() == feKey) {
                        fkw.selected(true);
                        fkw.updateState();
                        if (lastKey != null) {
                            lastKey.selected(false);
                            lastKey.updateState();
                        }
                        lastKey = fkw;
                    }
                    ee.render(matrices, mouseX, mouseY, delta);
                }
            }
        }

        renderTooltips(matrices, mouseX, mouseY);

        if (getFocused() != null && KeymapMain.cfg().debug) {
            drawCenteredText(matrices, tr, getFocused().getClass().getName(), width / 2, 5, 0xff_00ff00);
        }
    }

    @ToString
    static class SimpleV2 {
        public int x;
        public int y;

        public SimpleV2(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public SimpleV2(int xy) {
            this.x = xy;
            this.y = xy;
        }
    }

    @ToString
    static class KeyPoint {
        public int top;
        public int left;
        public int right;
        public int bottom;
        public int x;
        public int y;
        public int w;
        public int h;

        public KeyPoint(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;

            this.top = y;
            this.left = x;
            this.right = x + w;
            this.bottom = y + h;
        }
    }
}
