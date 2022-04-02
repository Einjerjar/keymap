package com.github.einjerjar.mc.keymap.screen;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.CategoryHolder;
import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import com.github.einjerjar.mc.keymap.keys.KeyboardLayout;
import com.github.einjerjar.mc.keymap.keys.category.MalilibCategory;
import com.github.einjerjar.mc.keymap.keys.category.VanillaCategory;
import com.github.einjerjar.mc.keymap.keys.key.VanillaKeybind;
import com.github.einjerjar.mc.keymap.screen.entrylist.FlatCategoryList;
import com.github.einjerjar.mc.keymap.screen.entrylist.FlatKeyList;
import com.github.einjerjar.mc.keymap.screen.widgets.FlatKeyWidget;
import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.FlatButton;
import com.github.einjerjar.mc.keymap.widgets.FlatInput;
import com.github.einjerjar.mc.keymap.widgets.FlatScreen;
import com.github.einjerjar.mc.keymap.widgets.containers.FlexContainer;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.hotkeys.KeybindCategory;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

import java.util.*;

public class KeyMappingScreen2 extends FlatScreen {
    TextRenderer tr = textRenderer;

    boolean malilib;
    int outPadX = 4;
    int outPadY = 4;
    int padX = 10;
    int padY = 10;
    int gapX = 2;
    int gapY = 2;
    int expectedScreenWidth;
    int top;
    int left;
    int right;
    int bottom;

    private final Map<String, CategoryHolder> mappedCategories = new HashMap<>();
    private final Map<Integer, List<KeybindHolder>> mappedKeybindHolders = new HashMap<>();
    private final Map<Integer, List<FlatKeyWidget>> mappedKeyWidgets = new HashMap<>();

    FlatInput inputSearch;
    FlatKeyList listKeybinds;
    FlatCategoryList listCategories;
    FlexContainer containerSidebar;
    FlexContainer containerSideButtons;
    FlatButton buttonResetSelect;
    FlatButton buttonResetAll;

    public KeyMappingScreen2(Screen parent) {
        super(new LiteralText(""), parent);
    }

    public KeyMappingScreen2() {
        super(new LiteralText(""));
    }

    @Override
    protected void init() {
        tr = textRenderer;
        malilib = KeymapMain.malilibSupport;
        expectedScreenWidth = Math.min(500, width);

        mappedCategories.clear();
        mappedKeyWidgets.clear();
        mappedKeybindHolders.clear();

        top = outPadY;
        left = Math.max(0, width - expectedScreenWidth) / 2 + outPadX;
        right = left + expectedScreenWidth - outPadX * 2;
        bottom = height - outPadY;

        int[] kbKeys   = addKeys(KeyboardLayout.getKeys(), left + padX, top + padY);
        int[] kbExtra  = addKeys(KeyboardLayout.getExtra(), left + padX, top + padY * 2 + kbKeys[1] - gapY);
        int[] kbMouse  = addKeys(KeyboardLayout.getMouse(), left + padX, top + padY * 3 + kbKeys[1] + kbExtra[1] - gapY * 2);
        int[] kbNumpad = addKeys(KeyboardLayout.getNumpad(), left + padX * 2 + kbExtra[0] - gapX, top + padY * 2 + kbKeys[1] - gapY);

        int leftSpaceX = expectedScreenWidth - outPadX * 2 - kbKeys[0] - padX * 3;

        inputSearch = new FlatInput(0, 0, leftSpaceX, 16, "");
        listKeybinds = new FlatKeyList(0, 0, leftSpaceX, 50, 10);
        // TODO: simplify
        listCategories = new FlatCategoryList(
            left + padX * 3 + kbExtra[0] - gapX * 2 + kbNumpad[0] + padX,
            top + padY * 2 + kbKeys[1] - gapY + padY,
            (kbKeys[0] + padX * 2 + left) - (left + padX * 3 + kbExtra[0] - gapX * 2 + kbNumpad[0]) - padX - gapX - padX,
            kbNumpad[1] - gapY - padY * 2,
            tr.fontHeight
            );

        buttonResetSelect = new FlatButton(0, 0, 0, 0, new LiteralText("Reset"));
        buttonResetAll = new FlatButton(0, 0, 0, 0, new LiteralText("Reset All"));

        buttonResetSelect.setTooltip(new TranslatableText("key.keymap.tip.reset_selected"));
        buttonResetAll.setTooltip(new TranslatableText("key.keymap.tip.reset_all"));

        containerSideButtons = new FlexContainer(0, 0, 0, 16);
        containerSideButtons
            .addChild(buttonResetSelect)
            .addChild(buttonResetAll);

        buttonResetSelect.setAction(button -> {
            listKeybinds.resetSelected();
            updateMappedKeybinds();
            updateKeyWidgets();
        });
        buttonResetAll.setAction(button -> {
            listKeybinds.resetAll();
            updateMappedKeybinds();
            updateKeyWidgets();
        });

        containerSidebar = new FlexContainer(right - leftSpaceX - padX, top + padY, leftSpaceX, height - outPadY * 2 - padY);
        containerSidebar
            .setDirection(FlexContainer.FlexDirection.COLUMN)
            .addChild(inputSearch, -1)
            .addChild(listKeybinds)
            .addChild(containerSideButtons, -1)
            .setGap(10)
            .arrange();

        containerSideButtons.arrange();

        addSelectableChild(containerSidebar);
        addSelectableChild(listCategories);

        inputSearch.setOnTextChanged(input -> {
            filterListKeys();
        });

        listKeybinds.setOnKeyChanged(fk -> {
            updateMappedKeybinds();
            updateKeyWidgets();
        });

        listCategories.setOnSelectedAction(w -> {
            inputSearch.setText("");
            listKeybinds.setSelectedEntry(null);
           filterListKeys();
        });

        //noinspection ConstantConditions
        for (KeyBinding kb : client.options.keysAll) {
            String cat = kb.getCategory();
            if (!mappedCategories.containsKey(cat)) {
                mappedCategories.put(cat, new VanillaCategory(cat));
            }
            CategoryHolder catHolder = mappedCategories.get(cat);
            VanillaKeybind vkb       = new VanillaKeybind(kb);
            catHolder.addKeybind(vkb);
        }

        if (malilib) {
            InputEventHandler handler = (InputEventHandler) InputEventHandler.getInputManager();
            for (KeybindCategory cat : handler.getKeybindCategories()) {
                KeymapMain.LOGGER.info(cat.getModName());
                MalilibCategory c = new MalilibCategory(cat);
                mappedCategories.put(cat.getModName(), c);
            }
        }

        listCategories.clearEntries();
        listCategories.addEntry(new FlatCategoryList.FlatCategoryEntry("__ALL__"));

        for (CategoryHolder c : mappedCategories.values().stream().sorted(Comparator.comparing(o -> o.getCategoryName().getString())).toList()) {
            listCategories.addEntry(new FlatCategoryList.FlatCategoryEntry(c.getCategoryKey()));
            List<KeybindHolder> kbs = c.getKeybinds();
            for (KeybindHolder kb : kbs) {
                listKeybinds.addEntry(new FlatKeyList.FlatKeyListEntry(kb));
            }
        }

        updateMappedKeybinds();
        updateKeyWidgets();

        filterListKeys();
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
            for (KeybindHolder kb : mappedCategories.get(cat).getKeybinds()) {
                if (kb.getCode().size() == 0) continue;
                int kCode = kb.getCode().get(0);
                if (!mappedKeybindHolders.containsKey(kCode)) mappedKeybindHolders.put(kCode, new ArrayList<>());
                mappedKeybindHolders.get(kCode).add(kb);
            }
        }
    }

    private void filterListKeys() {
        String filter = inputSearch.getText().trim().toLowerCase();
        listKeybinds.clearEntries();

        FlatCategoryList.FlatCategoryEntry catE = listCategories.getSelectedEntry();
        boolean isBlank = catE == null || catE.category.equalsIgnoreCase("__ALL__");

        for (CategoryHolder cat : mappedCategories.values().stream().sorted(Comparator.comparing(o -> o.getCategoryName().getString())).toList()) {
            if (!isBlank && !cat.getCategoryKey().equals(catE.category)) continue;
            for (KeybindHolder kb : cat.getKeybinds()) {
                if (
                    filter.isBlank()
                        || kb.getTranslation().getString().toLowerCase().contains(filter)
                        || String.format("[%s]", kb.getKeyTranslation().getString().toLowerCase()).contains(filter)
                )
                    listKeybinds.addEntry(new FlatKeyList.FlatKeyListEntry(kb));
            }
        }
    }

    private int[] addKeys(List<List<KeyboardLayout.KeyboardKey>> keys, int x, int y) {
        int sizeX    = 0;
        int sizeY    = 0;
        int currentX = x;
        int currentY = y;

        for (List<KeyboardLayout.KeyboardKey> row : keys) {
            int minItemHeight = height;
            for (KeyboardLayout.KeyboardKey key : row) {
                FlatKeyWidget k    = new FlatKeyWidget(currentX, currentY, key, mappedKeybindHolders);
                int           code = key.keyCode;

                k.setAction(button -> {
                    FlatKeyList.FlatKeyListEntry fke = listKeybinds.getSelectedEntry();
                    if (fke != null) {
                        fke.holder.assignHotKey(new int[]{k.key.keyCode}, key.type == InputUtil.Type.MOUSE);
                        updateMappedKeybinds();
                        updateKeyWidgets();
                        listKeybinds.setSelectedEntry(null);
                    }
                });

                if (!mappedKeyWidgets.containsKey(code)) {
                    mappedKeyWidgets.put(code, new ArrayList<>());
                }
                mappedKeyWidgets.get(key.keyCode).add(k);
                addSelectableChild(k);
                currentX += gapX + k.getW();
                minItemHeight = Math.min(k.getH(), minItemHeight);
            }
            sizeX = Math.max(currentX - x, sizeX);
            currentX = x;
            currentY += gapY + minItemHeight;
            sizeY = currentY - y;
        }

        return new int[]{sizeX, sizeY};
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        WidgetUtils.drawBoxOutline(this, matrices, left, top, right - left, bottom - top, 0xff_ffffff);

        WidgetUtils.drawBoxOutline(this, matrices,
            listCategories.getX() - padX,
            listCategories.getY() - padY,
            listCategories.getW() + padX,
            listCategories.getH() + padY * 2,
            ColorGroup.NORMAL.border.normal);

        containerSidebar.render(matrices, mouseX, mouseY, delta);
        // listCategories.render(matrices, mouseX, mouseY, delta);

        if (getFocused() != null && KeymapMain.cfg.debug) {
            drawCenteredText(matrices, tr, getFocused().getClass().getName(), width / 2, 5, 0xff_00ff00);
        }

        renderChildren(matrices, mouseX, mouseY, delta);
        renderTooltips(matrices, mouseX, mouseY, delta);
    }
}
