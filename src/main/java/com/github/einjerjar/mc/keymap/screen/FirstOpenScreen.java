package com.github.einjerjar.mc.keymap.screen;

import com.github.einjerjar.mc.keymap.KeymapConfig;
import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.BasicKeyData;
import com.github.einjerjar.mc.keymap.keys.CategoryHolder;
import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import com.github.einjerjar.mc.keymap.keys.KeyboardKey;
import com.github.einjerjar.mc.keymap.keys.category.MalilibCategory;
import com.github.einjerjar.mc.keymap.keys.category.VanillaCategory;
import com.github.einjerjar.mc.keymap.keys.key.VanillaKeybind;
import com.github.einjerjar.mc.keymap.keys.layout.KeyboardLayoutBase;
import com.github.einjerjar.mc.keymap.screen.entrylist.FlatStringList;
import com.github.einjerjar.mc.keymap.screen.widgets.FlatKeyWidget;
import com.github.einjerjar.mc.keymap.utils.SimpleRect;
import com.github.einjerjar.mc.keymap.utils.SimpleV2;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.FlatButton;
import com.github.einjerjar.mc.keymap.widgets.FlatScreen;
import com.github.einjerjar.mc.keymap.widgets.FlatText;
import com.github.einjerjar.mc.keymap.widgets.FlatWidgetBase;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.hotkeys.KeybindCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.*;

public class FirstOpenScreen extends FlatScreen {
    protected final Map<Integer, List<KeybindHolder>> mappedKeybindHolders = new HashMap<>();
    protected final Map<String, CategoryHolder> mappedCategories = new HashMap<>();
    protected ArrayList<FlatKeyWidget> fkList = new ArrayList<>();
    protected FlatStringList<String> flatStringList;
    protected FlatText labelSelectLayout;
    protected FlatText labelCreditHeader;
    protected FlatText labelCredit;
    protected FlatButton btnConfirm;
    protected boolean malilib;

    protected SimpleV2 gap = new SimpleV2(2);
    protected SimpleV2 pad = new SimpleV2(10);
    protected SimpleRect r;

    public FirstOpenScreen(Screen parent) {
        super(Text.of("First Open Screen"), parent);
    }

    @Override protected void init() {
        malilib = KeymapMain.malilibAvailable() && KeymapMain.cfg().malilibSupport;
        int expectedWidth = Math.min(500, width) - pad.x * 2;
        r = new SimpleRect(
            Math.max(width - expectedWidth, 0) / 2,
            pad.y,
            expectedWidth,
            height - pad.y * 2
        );

        fkList.clear();
        SimpleRect kbKeys   = addKeys(KeymapMain.keys().basic(), r.x, r.y + textRenderer.fontHeight + gap.y * 2);
        SimpleRect kbExtra  = addKeys(KeymapMain.keys().extra(), r.x, kbKeys.bottom + gap.y * 2);
        SimpleRect kbMouse  = addKeys(KeymapMain.keys().mouse(), r.x, kbExtra.bottom + gap.y * 2 + 16);
        SimpleRect kbNumpad = addKeys(KeymapMain.keys().numpad(), kbMouse.right + gap.x * 2, kbExtra.y);

        int spaceLeft = expectedWidth - kbKeys.w - gap.x * 2;

        labelSelectLayout = new FlatText(
            kbKeys.right + gap.x * 2,
            r.y,
            spaceLeft,
            textRenderer.fontHeight,
            Text.translatable("key.keymap.screen.first.select_layout").getWithStyle(Utils.styleKey).get(0));

        labelCreditHeader = new FlatText(
            kbNumpad.right + gap.x * 2,
            kbNumpad.y + 15,
            kbKeys.w - kbNumpad.w - kbMouse.w - gap.x * 4,
            textRenderer.fontHeight,
            Text.translatable("key.keymap.screen.first.credit").getWithStyle(Utils.styleSimpleBold).get(0));

        labelCredit = new FlatText(
            kbNumpad.right + gap.x * 2,
            kbNumpad.y + textRenderer.fontHeight + gap.y * 2 + 15,
            kbKeys.w - kbNumpad.w - kbMouse.w - gap.x * 4,
            textRenderer.fontHeight,
            Text.of("E").getWithStyle(Utils.styleKey).get(0));
        if (KeymapMain.keys().author() != null) {
            labelCredit.setText(Text.of(KeymapMain.keys().author()).getWithStyle(Utils.styleKey).get(0));
        }

        flatStringList = new FlatStringList<>(kbKeys.right + gap.x * 2, r.y + textRenderer.fontHeight + gap.y * 2, spaceLeft, r.h - textRenderer.fontHeight - gap.y * 4 - 16, textRenderer.fontHeight);
        flatStringList.drawBorder(true);
        flatStringList.onSelectedAction(w -> reloadKeyDisplay());

        KeyboardLayoutBase.layouts().forEach((s, klb) -> flatStringList.addEntry(new FlatStringList.FlatStringEntry<>(klb.code(), Text.of(klb.name()))));

        int selectedIndex = 0;
        for (int i = 0; i < flatStringList.entries().size(); i++) {
            FlatStringList.FlatStringEntry<String> e = flatStringList.getEntry(i);
            if (Objects.equals(e.entryIndex(), KeymapConfig.instance().customLayout)) {
                selectedIndex = i;
                break;
            }
        }
        flatStringList.setSelectedIndex(selectedIndex);

        btnConfirm = new FlatButton(
            flatStringList.left(),
            flatStringList.bottom() + gap.y * 2,
            flatStringList.w(),
            16, Text.translatable("key.keymap.screen.first.confirm"));

        btnConfirm.action(w -> {
            // KeymapMain.LOGGER().info("Selected: {}", flatStringList.getSelectedEntry().entryIndex());
            KeymapConfig.instance().customLayout = flatStringList.getSelectedEntry().entryIndex();
            KeymapConfig.instance().firstOpenDone = true;
            KeymapConfig.save();
            if (parent instanceof KeyMappingScreen) {
                MinecraftClient.getInstance().setScreen(parent);
            } else {
                MinecraftClient.getInstance().setScreen(new KeyMappingScreen(parent));
            }
        });

        addSelectableChild(flatStringList);
        addSelectableChild(btnConfirm);

        for (KeyBinding kb : client.options.allKeys) {
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
        updateMappedKeybinds();
        updateKeyWidgets();

        // mouseClicked(0, 0, 0);
        // mouseReleased(0, 0, 0);
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

    public void updateKeyWidgets() {
        for (FlatKeyWidget kw : fkList) {
            kw.updateState();
        }
    }

    private void reloadKeyDisplay() {
        if (flatStringList.getSelectedEntry() == null) return;
        for (FlatKeyWidget fk : fkList) {
            children().remove(fk);
        }

        fkList.clear();
        KeymapMain.reloadLayouts(flatStringList.getSelectedEntry().entryIndex());

        SimpleRect kbKeys   = addKeys(KeymapMain.keys().basic(), r.x, r.y + textRenderer.fontHeight + gap.y * 2);
        SimpleRect kbExtra  = addKeys(KeymapMain.keys().extra(), r.x, kbKeys.bottom + gap.y * 2);
        SimpleRect kbMouse  = addKeys(KeymapMain.keys().mouse(), r.x, kbExtra.bottom + gap.y * 2 + 16);
        SimpleRect kbNumpad = addKeys(KeymapMain.keys().numpad(), kbMouse.right + gap.x * 2, kbExtra.y);

        if (KeymapMain.keys().author() != null) {
            labelCredit.setText(Text.of(KeymapMain.keys().author()).getWithStyle(Utils.styleKey).get(0));
        }

        updateKeyWidgets();
    }

    private SimpleRect addKeys(List<List<BasicKeyData>> keys, int x, int y) {
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

                // addSelectableChild(k);
                fkList.add(k);
                addSelectableChild(k);
                currentX += gap.x + k.w();
                minItemHeight = Math.min(k.h(), minItemHeight);
            }
            sizeX = Math.max(currentX - x, sizeX);
            currentX = x;
            currentY += gap.x + minItemHeight;
            sizeY = currentY - y;
        }

        return new SimpleRect(x, y, sizeX - gap.x, sizeY - gap.y);
    }

    @Override public void renderScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (FlatWidgetBase c : children().stream().filter(e -> e instanceof FlatWidgetBase).map(e -> (FlatWidgetBase) e).toList()) {
            if (!c.enabled()) continue;
            c.render(matrices, mouseX, mouseY, delta);
        }
        labelSelectLayout.render(matrices, mouseX, mouseY, delta);

        FlatStringList.FlatStringEntry<String> e = flatStringList.getSelectedEntry();
        if (e != null && KeyboardLayoutBase.layoutWithCode(e.entryIndex()).author() != null) {
            labelCreditHeader.render(matrices, mouseX, mouseY, delta);
            labelCredit.render(matrices, mouseX, mouseY, delta);
        }

        WidgetUtils.drawBoxOutline(this, matrices, r.x - pad.x / 2, r.y - pad.y / 2, r.w + pad.x, r.h + pad.y, 0xFFFFFFFF);

        // fkList.forEach(flatKeyWidget -> {
        //     flatKeyWidget.renderWidget(matrices, mouseX, mouseY, delta);
        // });
    }
}
