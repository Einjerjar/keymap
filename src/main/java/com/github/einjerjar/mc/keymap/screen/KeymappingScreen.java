package com.github.einjerjar.mc.keymap.screen;

import com.github.einjerjar.mc.keymap.Main;
import com.github.einjerjar.mc.keymap.screen.widgets.*;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class KeymappingScreen extends Screen {
    private final ArrayList<KeyWidget> keyWidgets = new ArrayList<>();
    private final HashMap<Integer, ArrayList<KeyWidget>> keyWidgetMap = new HashMap<>();
    private final HashMap<String, ArrayList<KeyBinding>> categorizedKeybinds = new HashMap<>();

    private KeyListWidget keyList;
    private CategoryListWidget categoryList;
    private Element lastClickedElement;
    private KeyBinding selectedKeyBind = null;
    private final HashMap<Integer, ArrayList<KeyBinding>> mappedKeyCount = new HashMap<>();
    private FlatButtonWidget resetSelected;
    private FlatButtonWidget resetAll;
    private FlatInputWidget searchBar;

    public KeymappingScreen() {
        super(new TranslatableText("ein.kb.screen.title"));
        // this.addDrawable(new KeyWidget(textRenderer, 10, 10, 16, 16, "test"));
    }

    @Override
    protected void init() {
        int keyBoardMaxWidth = 276;
        int expectedScreenWidth = Math.min(500, width);
        int padX = 10;
        int padY = 10;
        int tx = padX;
        int ty = padY;
        int sx = 16;
        int sy = 16;
        int ox = 2;
        int oy = 2;
        int keyListWidth = expectedScreenWidth - (padX * 2 + keyBoardMaxWidth);
        if (width > expectedScreenWidth) tx = padX + ((width / 2) - ((expectedScreenWidth - padX * 2) / 2));
        int keyListX = tx + keyBoardMaxWidth;

        assert client != null;
        keyList = new KeyListWidget(client, keyListWidth, height - ((padY * 2 + 50) + padY + 20), padY * 2 + 20, height - (padY * 2 + 20), textRenderer.fontHeight + 1);
        keyList.setLeftPos(keyListX);

        categoryList = new CategoryListWidget(client, 70 + padX, 88, ty + 112 + 5, ty + 88, textRenderer.fontHeight + 1);
        categoryList.setLeftPos(tx + 72 + padX + 70);
        // addDrawableChild(categoryList);

        resetAll = new FlatButtonWidget(keyListX + ((keyListWidth / 2)), height - (padY + 20), (keyListWidth - padX) / 2, 20, new TranslatableText("key.keymap.reset_all"), btn -> {
            resetAllKeybinds();
        });
        resetSelected = new FlatButtonWidget(keyListX, height - (padY + 20), (keyListWidth - padX) / 2, 20, new TranslatableText("key.keymap.reset"), btn -> {
            resetSelectedKeybind();
        });
        resetAll.enabled = true;
        resetSelected.enabled = true;
        addDrawableChild(keyList);
        addDrawableChild(resetAll);
        addDrawableChild(resetSelected);

        // FIXME: Disgusting + slow (bec of recreating entries) code
        searchBar = new FlatInputWidget(this.textRenderer, keyListX, padY, keyListWidth, 20, null);
        searchBar.setChangedListener(s -> {
            s = s.toLowerCase().trim();
            boolean blank = s.isBlank();
            keyList._clearEntries();
            for (String k : categorizedKeybinds.keySet()) {
                for (KeyBinding kk : categorizedKeybinds.get(k)) {
                    String txt = new TranslatableText(kk.getTranslationKey()).getString().toLowerCase();
                    String key = String.format("[%s]", kk.getBoundKeyLocalizedText().getString()).toLowerCase();
                    if (blank || txt.contains(s) || key.contains(s)) {
                        keyList.addEntry(new KeyListWidget.KeyListEntry(this.textRenderer, kk));
                    }
                }
            }
        });
        addDrawableChild(searchBar);

        keyWidgets.clear();
        keyWidgetMap.clear();
        refreshMapCount();
        refreshKeyList();

        addKeys(KeyboardLayout.getKeys(), tx, ty, sx, sy, ox, oy);

        ty += 112 + 5;
        addKeys(KeyboardLayout.getExtra(), tx, ty, sx, sy, ox, oy);

        ty += 54;
        addKeys(KeyboardLayout.getMouse(), tx, ty, sx, sy, ox, oy);

        ty -= 54;
        tx += 72 + padX;
        addKeys(KeyboardLayout.getNumpad(), tx, ty, sx, sy, ox, oy);
    }

    private void refreshKeyList() {
        for (String s : categorizedKeybinds.keySet()) {
            for(KeyBinding k : categorizedKeybinds.get(s)) {
                keyList.addEntry(new KeyListWidget.KeyListEntry(this.textRenderer, k));
            }
        }
    }

    private void addKeys(ArrayList<ArrayList<KeyboardLayout.KeyboardKey>> keys, int tx, int ty, int sx, int sy, int ox, int oy) {
        for (int y = 0; y < keys.size(); y++) {
            ArrayList<KeyboardLayout.KeyboardKey> row = keys.get(y);
            int currentX = tx;
            for (KeyboardLayout.KeyboardKey key : row) {
                int cw = sx + key.extraWidth;
                KeyWidget k = new KeyWidget(currentX, ty + ((sy + oy) * y), key, mappedKeyCount);
                currentX += cw + ox;
                addDrawableChild(k);
                addKeyWidget(k);
            }
        }
    }

    private void addKeyWidget(KeyWidget k) {
        keyWidgets.add(k);
        int code = k.key.keyCode;
        if (!keyWidgetMap.containsKey(code)) {
            keyWidgetMap.put(code, new ArrayList<>());
        }
        keyWidgetMap.get(code).add(k);
    }

    private void refreshMapCount() {
        mappedKeyCount.clear();
        categorizedKeybinds.clear();
        assert client != null;
        for (KeyBinding kk : client.options.keysAll) {
            int code = kk.boundKey.getCode();
            String cat = kk.getCategory();

            if (!mappedKeyCount.containsKey(code))
                mappedKeyCount.put(code, new ArrayList<>());
            mappedKeyCount.get(code).add(kk);

            if (!categorizedKeybinds.containsKey(cat)) categorizedKeybinds.put(cat, new ArrayList<>());
            categorizedKeybinds.get(cat).add(kk);

            if (!categoryList.knownCategories.contains(cat)) categoryList.addEntry(new CategoryListWidget.CategoryEntry(cat));
        }
        for (String cat : categorizedKeybinds.keySet()) {
            categorizedKeybinds.get(cat).sort((o1, o2) -> new TranslatableText(o1.getTranslationKey()).getString().compareToIgnoreCase(
                new TranslatableText(o2.getTranslationKey()).getString()
            ));
        }
    }

    public void resetAllKeybinds() {
        assert client != null;
        KeyBinding[] keys = client.options.keysAll;
        for (KeyBinding k : keys) {
            k.setBoundKey(k.getDefaultKey());
        }

        KeyBinding.updateKeysByCode();
        refreshMapCount();

        for (int k : keyWidgetMap.keySet()) {
            updateKeybindCount(k);
        }
    }

    private void resetSelectedKeybind() {
        if (selectedKeyBind == null) return;
        selectedKeyBind.setBoundKey(selectedKeyBind.getDefaultKey());

        for (int k : keyWidgetMap.keySet()) {
            updateKeybindCount(k);
        }
        KeyBinding.updateKeysByCode();
        refreshMapCount();

        selectedKeyBind = null;
        if (keyList.focusedEntry != null) {
            keyList.focusedEntry.focused = false;
            keyList.focusedEntry = null;
        }

        for (int k : keyWidgetMap.keySet()) {
            updateKeybindCount(k);
        }
    }

    private void updateKeybindCount(int code) {
        if (!keyWidgetMap.containsKey(code)) return;
        ArrayList<KeyWidget> keys = keyWidgetMap.get(code);
        for (KeyWidget k : keys) {
            k.updateState();
        }
    }

    @Nullable
    public KeyWidget getHoveredKeyWidget() {
        for (KeyWidget e : keyWidgets) {
            if (e.isHovered() && e.enabled) return e;
        }

        return null;
    }

    @Override
    public void render(MatrixStack m, int mouseX, int mouseY, float delta) {
        renderBackground(m);

        Element he = hoveredElement(mouseX, mouseY).orElse(null);

        for (KeyWidget k : keyWidgets) {
            // FIXME: only updated when changed
            k.selected = selectedKeyBind != null && k.key.keyCode == selectedKeyBind.boundKey.getCode();
            k.updateState();
            k.render(m, mouseX, mouseY, delta);
        }

        keyList.render(m, mouseX, mouseY, delta);
        categoryList.render(m, mouseX, mouseY, delta);
        resetAll.render(m, mouseX, mouseY, delta);
        resetSelected.render(m, mouseX, mouseY, delta);
        searchBar.render(m, mouseX, mouseY, delta);

        if (he instanceof FlatWidget) {
            Text tip = ((FlatWidget) he).getToolTip();
            if (tip != null) renderTooltip(m, tip, mouseX, mouseY);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (selectedKeyBind != null) {
            InputUtil.Key lastKey = selectedKeyBind.boundKey;
            assert client != null;
            if (keyCode == 256) {
                client.options.setKeyCode(selectedKeyBind, InputUtil.UNKNOWN_KEY);
            } else {
                client.options.setKeyCode(selectedKeyBind, InputUtil.fromKeyCode(keyCode, scanCode));
            }
            selectedKeyBind = null;
            // if (keyList != null) keyList.focusedEntry.focused = false;
            // keyList.focusedEntry = null;

            refreshMapCount();
            updateKeybindCount(keyCode);
            if (lastKey != null) {
                updateKeybindCount(lastKey.getCode());
            }
            KeyBinding.updateKeysByCode();
            return true;
        }
        if (keyCode == Main.KBOpenKBScreen.boundKey.getCode() && !searchBar.isFocused()) {
            onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (keyList.isMouseOver(mouseX, mouseY)) {
            return keyList.mouseScrolled(mouseX, mouseY, amount);
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (keyList.isMouseOver(mouseX, mouseY)) {
            return keyList.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        Element e = hoveredElement(mouseX, mouseY).orElse(null);
        Element le = lastClickedElement;
        lastClickedElement = null;
        if (le != null) le.mouseReleased(mouseX, mouseY, button);
        if (e != null && e != le) return e.mouseReleased(mouseX, mouseY, button);
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        lastClickedElement = hoveredElement(mouseX, mouseY).orElse(null);

        if (lastClickedElement != null) {
            Main.LOGGER.info(lastClickedElement.getClass().getName());
        }

        if (keyList.isMouseOver(mouseX, mouseY)) {
            if (keyList.mouseClicked(mouseX, mouseY, button)) {
                if (searchBar.isFocused()) searchBar.changeFocus(false);
                if (keyList.focusedEntry != null)
                    selectedKeyBind = keyList.focusedEntry.key;
                else
                    selectedKeyBind = null;
                return true;
            }
        }

        Main.LOGGER.info("?" + children().contains(lastClickedElement));

        Main.LOGGER.info("1");

        if (selectedKeyBind == null) super.mouseClicked(mouseX, mouseY, button);

        Main.LOGGER.info("2");

        // TODO: Handle mouse binds
        if ((lastClickedElement != null) && lastClickedElement instanceof KeyWidget kw && keyWidgets.contains(lastClickedElement)) {
            int keyCode = kw.key.keyCode;

            InputUtil.Key lastKey = selectedKeyBind.boundKey;
            assert client != null;
            if (keyCode == 256) {
                client.options.setKeyCode(selectedKeyBind, InputUtil.UNKNOWN_KEY);
            } else {
                client.options.setKeyCode(selectedKeyBind, kw.key.key);
            }
            selectedKeyBind = null;

            refreshMapCount();
            updateKeybindCount(keyCode);
            if (lastKey != null) {
                updateKeybindCount(lastKey.getCode());
            }
            KeyBinding.updateKeysByCode();
            return true;
        }

        Main.LOGGER.info("3");

        if (lastClickedElement == null) {
            InputUtil.Key lastKey = selectedKeyBind.boundKey;
            assert client != null;
            client.options.setKeyCode(selectedKeyBind, InputUtil.Type.MOUSE.createFromCode(button));
            selectedKeyBind = null;
            if (keyList.focusedEntry != null) keyList.focusedEntry.focused = false;
            keyList.focusedEntry = null;

            refreshMapCount();
            updateKeybindCount(button);
            if (lastKey != null) {
                updateKeybindCount(lastKey.getCode());
            }
            KeyBinding.updateKeysByCode();
            return true;
        }

        Main.LOGGER.info("4");

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
