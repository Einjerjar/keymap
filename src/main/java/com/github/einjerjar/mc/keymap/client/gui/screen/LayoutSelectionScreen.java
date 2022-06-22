package com.github.einjerjar.mc.keymap.client.gui.screen;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.client.gui.widgets.VirtualKeyboardWidget;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.keymap.keys.registry.KeybindingRegistry;
import com.github.einjerjar.mc.widgets.EButton;
import com.github.einjerjar.mc.widgets.EScreen;
import com.github.einjerjar.mc.widgets.EWidget;
import com.github.einjerjar.mc.widgets.ValueMapList;
import com.github.einjerjar.mc.widgets.utils.Point;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class LayoutSelectionScreen extends EScreen {
    protected VirtualKeyboardWidget vkBasic;
    protected VirtualKeyboardWidget vkExtra;
    protected VirtualKeyboardWidget vkMouse;
    protected VirtualKeyboardWidget vkNumpad;
    protected ValueMapList          listLayouts;
    protected EButton               btnSave;
    protected EButton               btnCancel;

    protected Rect           scr;
    protected Point<Integer> margin  = new Point<>(6);
    protected Point<Integer> padding = new Point<>(4);


    public LayoutSelectionScreen(Screen parent) {
        super(parent, new TextComponent("Keymap Layout"));
    }

    @Override protected void onInit() {
        KeyLayout layout = KeyLayout.getLayoutWithCode(KeymapConfig.instance().customLayout());
        KeybindingRegistry.load();

        int expectedScreenWidth = Math.min(450, width);
        scr = new Rect(Math.max((width - expectedScreenWidth) / 2, 0) + margin.x(),
                margin.y(),
                expectedScreenWidth - margin.x() * 2,
                height - margin.y() * 2);

        generateVk(layout);

        int spaceLeft = scr.w() - padding.x() * 3 - vkBasic.rect().w();

        listLayouts = new ValueMapList(font.lineHeight,
                vkBasic.right() + padding.x(),
                vkBasic.top(),
                spaceLeft,
                scr.h() - padding.y() * 4 - 16 * 2,
                false);

        for (String s : KeyLayout.layouts().keySet()) {
            listLayouts.addItem(new ValueMapList.ValueMapEntry<>(KeyLayout.layouts().get(s).meta().name(),
                    s,
                    listLayouts));
        }

        listLayouts.onItemSelected(this::onLayoutSelected);
        listLayouts.setItemSelectedWithValue(KeymapConfig.instance().customLayout());

        btnSave   = new EButton(
                new TranslatableComponent("keymap.btnSave"),
                listLayouts.left(),
                listLayouts.bottom() + padding.y(),
                (listLayouts.rect().w() - padding.x()) / 2,
                16
        );
        btnCancel = new EButton(
                new TranslatableComponent("keymap.btnCancel"),
                listLayouts.right() - btnSave.rect().w(),
                btnSave.top(),
                btnSave.rect().w(),
                16
        );

        btnSave.clickAction(this::onBtnSaveClicked);
        btnCancel.clickAction(this::onBtnCancelClicked);

        addRenderableWidget(listLayouts);
        addRenderableWidget(btnSave);
        addRenderableWidget(btnCancel);
    }

    protected void onBtnSaveClicked(EWidget source) {
        ValueMapList.ValueMapEntry<String> selected = (ValueMapList.ValueMapEntry<String>) listLayouts.itemSelected();
        if (selected == null) {
            Keymap.logger().error("Cant save empty value!!");
            return;
        }
        KeymapConfig.instance().customLayout(selected.value());
        KeymapConfig.save();
        onClose();
    }

    protected void onBtnCancelClicked(EWidget source) {
        onClose();
    }

    protected void onLayoutSelected(EWidget source) {
        ValueMapList.ValueMapEntry<String> selected = (ValueMapList.ValueMapEntry<String>) listLayouts.itemSelected();

        KeyLayout layout = KeyLayout.getLayoutWithCode(selected.value());
        removeWidget(vkBasic.destroy());
        removeWidget(vkExtra.destroy());
        removeWidget(vkMouse.destroy());
        removeWidget(vkNumpad.destroy());
        KeybindingRegistry.clearSubscribers();
        generateVk(layout);
    }

    @Override public void onClose() {
        KeybindingRegistry.clearSubscribers();
        if (KeymapConfig.instance().firstOpenDone()) {
            super.onClose();
        } else {
            KeymapConfig.instance().firstOpenDone(true);
            KeymapConfig.save();
            Minecraft.getInstance().setScreen(new KeymapScreen(parent()));
        }
    }

    private void generateVk(KeyLayout layout) {
        vkBasic  = new VirtualKeyboardWidget(layout.keys().basic(),
                scr.x() + padding.x(),
                scr.y() + padding.y() * 2 + 16,
                0,
                0);
        vkExtra  = new VirtualKeyboardWidget(layout.keys().extra(), vkBasic.left(), vkBasic.bottom() + 4, 0, 0);
        vkMouse  = new VirtualKeyboardWidget(layout.keys().mouse(), vkExtra.left(), vkExtra.bottom() + 2, 0, 0);
        vkNumpad = new VirtualKeyboardWidget(layout.keys().numpad(), vkMouse.right() + 4, vkBasic.bottom() + 4, 0, 0);

        addRenderableWidget(vkBasic);
        addRenderableWidget(vkExtra);
        addRenderableWidget(vkMouse);
        addRenderableWidget(vkNumpad);
    }

    @Override protected void preRenderScreen(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        fill(poseStack, 0, 0, width, height, 0x55000000);
        if (scr != null) drawOutline(poseStack, scr, 0xFFFFFFFF);
    }
}
