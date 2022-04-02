package com.github.einjerjar.mc.keymap.screen.v1;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.KeyboardLayout;
import com.github.einjerjar.mc.keymap.screen.v1.widgets.*;
import com.github.einjerjar.mc.keymap.utils.Utils;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.HashMap;

public class KeymappingScreen extends Screen {
    TextRenderer tr;
    ArrayList<KeyWidget> keyWidgets = new ArrayList<>();
    HashMap<Integer, KeyWidget> keyWidgetMap = new HashMap<>();
    HashMap<Integer, ArrayList<KeyBinding>> mappedKeyCount = new HashMap<>();
    HashMap<String, ArrayList<KeyBinding>> categoryKeyMap = new HashMap<>();
    CategoryListWidget categoryList;
    KeyListWidget keyList;
    FlatButtonWidget btnReset;
    FlatButtonWidget btnResetAll;
    FlatButtonWidget btnToggleMalilib;
    FlatInputWidget inpSearch;

    Screen parent = null;

    int outerPadX = 4;
    int outerPadY = 4;
    int padX = 10;
    int padY = 10;
    int keyGapX = 2;
    int keyGapY = 2;
    int expectedScreenWidth;
    int minX;
    int maxX;
    int minY;
    int maxY;

    boolean malilib;

    Element lastClickedElement = null;

    public KeymappingScreen() {
        super(new TranslatableText("key.keymap.cfg.title"));
    }

    public KeymappingScreen(Screen parent) {
        super(new TranslatableText("key.keymap.cfg.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        malilib = KeymapMain.malilibSupport;
        tr = textRenderer;
        expectedScreenWidth = Math.min(500, width);
        minX = Math.max(0, width - expectedScreenWidth) / 2 + outerPadX;
        maxX = Math.max(0, width - expectedScreenWidth) / 2 + (expectedScreenWidth - outerPadX);
        minY = outerPadY;
        maxY = height - outerPadY;

        children().clear();
        if (keyList != null) keyList._clearEntries();
        if (categoryList != null) categoryList._clearEntries();

        keyWidgets.clear();
        // int[] kbKeys   = addKeys(KeyboardLayout.getKeys(), minX + padX, minY + padY);
        // int[] kbExtra  = addKeys(KeyboardLayout.getExtra(), minX + padX, minY + padY * 2 + kbKeys[1] - keyGapY);
        // int[] kbMouse  = addKeys(KeyboardLayout.getMouse(), minX + padX, minY + padY * 3 + kbKeys[1] + kbExtra[1] - keyGapY * 2);
        // int[] kbNumpad = addKeys(KeyboardLayout.getNumpad(), minX + padX * 2 + kbExtra[0] - keyGapX, minY + padY * 2 + kbKeys[1] - keyGapY);

        // IGNORE, DEPRECATED CODE, SHIT IS JUST FOR TESTING
        int[] kbKeys   = new int[0];
        int[] kbExtra  = new int[0];
        int[] kbMouse  = new int[0];
        int[] kbNumpad = new int[0];

        int leftSpaceX = expectedScreenWidth - outerPadX * 2 - kbKeys[0] - padX * 3;
        int btnSizeX   = (leftSpaceX - padX) / 2;

        btnReset = new FlatButtonWidget(maxX - (btnSizeX * 2 + padX * 2), maxY - (20 + padX), btnSizeX, 20, new TranslatableText("key.keymap.reset"), widget -> resetSelectedKey());
        btnResetAll = new FlatButtonWidget(maxX - (btnSizeX + padX), maxY - (20 + padX), btnSizeX, 20, new TranslatableText("key.keymap.reset_all"), widget -> resetAllKeys());
        inpSearch = new FlatInputWidget(maxX - leftSpaceX - padX, minY + padY, leftSpaceX, 16, new LiteralText(""));

        keyList = new KeyListWidget(client, leftSpaceX, maxY - minY - padY * 3 - 20, minY + padY * 2 + 16, maxY - padY * 2 - 20, tr.fontHeight + 1);
        keyList.setLeftPos(maxX - leftSpaceX - padX);

        int catX = minX + padX * 2 + kbExtra[0] - keyGapX * 2 + kbNumpad[0] + padX;
        int catY = minY + padY * 2 + kbKeys[1] - keyGapY;
        int catW = expectedScreenWidth - padX * 5 - kbExtra[0] - kbNumpad[0] - leftSpaceX - padX / 2 - 1;
        int catH = kbNumpad[1] - 2;

        if (malilib) {
            btnToggleMalilib = new FlatButtonWidget(catX, catY, catW, 16, new TranslatableText("togglemali"), null);
            addSelectableChild(btnToggleMalilib);
        }

        categoryList = new CategoryListWidget(client,
            catW,
            malilib ? catH - 16 - keyGapY : catH,
            malilib ? catY + 16 + keyGapY : catY,
            minY + padY * 2 + kbKeys[1] - keyGapY + kbNumpad[1] - 2,
            tr.fontHeight + 1);
        categoryList.setLeftPos(catX);

        addSelectableChild(btnReset);
        addSelectableChild(btnResetAll);
        addSelectableChild(inpSearch);
        addSelectableChild(keyList);
        addSelectableChild(categoryList);

        // Inefficient at load, but allows decoupled updates later down the line
        updateCategoryList();
        updateCategoryKeymap();
        updateMappedKeyCount();
        updateKeyList();
        updateKeyWidgetStates();

        // InputEventHandler handler = (InputEventHandler) InputEventHandler.getInputManager();
        // for(KeybindCategory cat : handler.getKeybindCategories()) {
        //     cat.getHotkeys()
        // }

        categoryList.setSelected(categoryList.children().get(0));
        inpSearch.setChangedListener(s -> updateKeyList());
    }

    private void updateKeyList() {
        assert client != null;
        keyList._clearEntries();

        if (keyList.selected != null) keyList.selected.selected = false;
        CategoryListWidget.CategoryEntry c           = categoryList.selected;
        String                           search      = inpSearch.getText().trim();
        boolean                          blankSearch = search.isBlank();

        for (String cat : categoryList.knownCategories) {
            if (c != null && !c.category.equalsIgnoreCase("__ANY__") && !c.category.equalsIgnoreCase(cat)) continue;
            if (!categoryKeyMap.containsKey(cat)) continue;
            for (KeyBinding k : categoryKeyMap.get(cat)) {
                if (!blankSearch) {
                    String tKey  = String.format("[%s]", k.getBoundKeyLocalizedText().getString().toLowerCase());
                    String tName = new TranslatableText(k.getTranslationKey()).getString().toLowerCase();
                    if (!tKey.contains(search) && !tName.contains(search)) continue;
                }
                keyList.addEntry(new KeyListWidget.KeyListEntry(k));
            }
        }
    }

    private void updateCategoryList() {
        assert client != null;
        categoryList._clearEntries();
        categoryList.addEntry(new CategoryListWidget.CategoryEntry("__ANY__"));
        for (KeyBinding k : client.options.keysAll) {
            String cat = k.getCategory();
            if (!categoryList.knownCategories.contains(cat))
                categoryList.addEntry(new CategoryListWidget.CategoryEntry(cat));
        }
    }

    private void updateMappedKeyCount() {
        assert client != null;
        mappedKeyCount.clear();
        for (KeyBinding k : client.options.keysAll) {
            int code = k.boundKey.getCode();

            if (!mappedKeyCount.containsKey(code)) mappedKeyCount.put(code, new ArrayList<>());
            mappedKeyCount.get(code).add(k);
        }
    }

    private void updateCategoryKeymap() {
        assert client != null;
        categoryKeyMap.clear();
        for (KeyBinding k : client.options.keysAll) {
            String cat = k.getCategory();

            if (!categoryKeyMap.containsKey(cat)) categoryKeyMap.put(cat, new ArrayList<>());
            categoryKeyMap.get(cat).add(k);
        }
    }

    @Override
    public void render(MatrixStack m, int mouseX, int mouseY, float delta) {
        renderBackground(m);
        Utils.drawBoxFilled(this, m, minX, minY, expectedScreenWidth - outerPadX * 2, maxY - minY, 0xff_ffffff, 0x55_444444);

        btnReset.render(m, mouseX, mouseY, delta);
        btnResetAll.render(m, mouseX, mouseY, delta);
        inpSearch.render(m, mouseX, mouseY, delta);
        keyList.render(m, mouseX, mouseY, delta);
        categoryList.render(m, mouseX, mouseY, delta);

        if (malilib) {
            btnToggleMalilib.render(m, mouseX, mouseY, delta);
        }

        KeyListWidget.KeyListEntry ke = keyList.selected;
        for (KeyWidget k : keyWidgets) {
            k.selected = ke != null && k.key.key == ke.key.boundKey;
            k.render(m, mouseX, mouseY, delta);
        }
        Element e = hoveredElement(mouseX, mouseY).orElse(null);
        if (e instanceof FlatWidget f) {
            renderTooltip(m, f.getToolTips(), mouseX, mouseY);
        } else if (e instanceof KeyListWidget kl) {
            ArrayList<Text> tip = kl.getToolTips();
            if (tip != null) renderTooltip(m, tip, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.setDragging(false);
        Element h = hoveredElement(mouseX, mouseY).orElse(null);
        if (lastClickedElement != null && h != lastClickedElement) {
            lastClickedElement.mouseReleased(mouseX, mouseY, button);
        }
        lastClickedElement = null;
        if (h != null) return h.mouseReleased(mouseX, mouseY, button);
        return false;
    }

    private void resetAllKeys() {
        assert client != null;
        for (KeyBinding k : client.options.keysAll) {
            resetKey(k, false);
        }

        KeyBinding.updateKeysByCode();
        updateMappedKeyCount();
        keyList.setSelected(null);
    }

    private void resetSelectedKey() {
        KeyListWidget.KeyListEntry k = keyList.selected;
        if (k == null) return;

        resetKey(k.key, false);
        updateMappedKeyCount();
        updateKeyWidgetStates();
        keyList.setSelected(null);
    }

    private void resetKey(KeyBinding kb, boolean update) {
        assert client != null;
        InputUtil.Key def = kb.getDefaultKey();
        client.options.setKeyCode(kb, def);
        if (update) {
            KeyBinding.updateKeysByCode();
            updateKeys(kb.boundKey.getCode(), def.getCode(), kb);
        }
    }

    private void setKeyboardKey(KeyBinding kb, InputUtil.Key key, boolean update) {
        assert client != null;
        int lastKeyCode = kb.boundKey.getCode();
        int currKeyCode = key.getCode();

        if (key.getCode() == 256) {
            client.options.setKeyCode(kb, InputUtil.UNKNOWN_KEY);
        } else {
            client.options.setKeyCode(kb, key);
        }
        if (update) {
            KeyBinding.updateKeysByCode();
            updateKeys(lastKeyCode, currKeyCode, kb);
        }
    }

    public void updateKeyWidgetStates() {
        for (KeyWidget kw : keyWidgets) {
            kw.updateState();
        }
    }

    public void updateKeys(int from, int to, KeyBinding kb) {
        mappedKeyCount.get(from).remove(kb);
        if (!mappedKeyCount.containsKey(to)) mappedKeyCount.put(to, new ArrayList<>());
        mappedKeyCount.get(to).add(kb);

        // KeyWidgets
        if (keyWidgetMap.containsKey(from)) keyWidgetMap.get(from).updateState();
        if (keyWidgetMap.containsKey(to)) keyWidgetMap.get(to).updateState();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        KeyListWidget.KeyListEntry ke = keyList.selected;
        if (ke != null) {
            setKeyboardKey(ke.key, InputUtil.fromKeyCode(keyCode, scanCode), true);
            // updateMappedKeyCount();
            keyList.setSelected(null);
            return true;
        }
        if (keyCode == KeymapMain.KBOpenKBScreen.boundKey.getCode()) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        assert client != null;

        if (parent != null) client.setScreen(parent);
        else super.onClose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Element c : children()) {
            if (c.mouseClicked(mouseX, mouseY, button)) {
                if (c instanceof CategoryListWidget cl) {
                    Element ce = cl.hoveredElement(mouseX, mouseY).orElse(null);
                    if (ce != null) {
                        inpSearch.setText("");
                        updateKeyList();
                        keyList.setScrollAmount(0);
                    }
                }
                if (c instanceof KeyWidget k && keyWidgets.contains(c)) {
                    KeyListWidget.KeyListEntry ke = keyList.selected;
                    if (ke != null) {
                        setKeyboardKey(ke.key, k.key.key, true);
                        keyList.setSelected(null);
                    }
                }
                if (c instanceof FlatInputWidget) {
                    keyList.setSelected(null);
                }
                setFocused(c);
                if (button == 0) setDragging(true);
                lastClickedElement = c;
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private int[] addKeys(ArrayList<ArrayList<KeyboardLayout.KeyboardKey>> keys, int x, int y) {
        int sizeX    = 0;
        int sizeY    = 0;
        int currentX = x;
        int currentY = y;

        for (ArrayList<KeyboardLayout.KeyboardKey> row : keys) {
            int minItemHeight = height;
            for (KeyboardLayout.KeyboardKey key : row) {
                KeyWidget k = new KeyWidget(currentX, currentY, key, mappedKeyCount);
                keyWidgets.add(k);
                keyWidgetMap.put(key.keyCode, k);
                addSelectableChild(k);
                currentX += keyGapX + k.getWidth();
                minItemHeight = Math.min(k.getHeight(), minItemHeight);
            }
            sizeX = Math.max(currentX - x, sizeX);
            currentX = x;
            currentY += keyGapY + minItemHeight;
            sizeY = currentY - y;
        }

        return new int[]{sizeX, sizeY};
    }
}
