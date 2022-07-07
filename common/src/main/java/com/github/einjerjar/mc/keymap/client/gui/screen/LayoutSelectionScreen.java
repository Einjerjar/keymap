package com.github.einjerjar.mc.keymap.client.gui.screen;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.client.gui.widgets.VirtualKeyboardWidget;
import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.keymap.keys.layout.KeyLayout;
import com.github.einjerjar.mc.keymap.keys.sources.KeymappingNotifier;
import com.github.einjerjar.mc.keymap.utils.VKUtil;
import com.github.einjerjar.mc.widgets.*;
import com.github.einjerjar.mc.widgets.utils.Styles;
import com.github.einjerjar.mc.widgets.utils.Text;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import java.util.List;
import java.util.Map;

public class LayoutSelectionScreen extends EScreen {
    protected List<VirtualKeyboardWidget> vks;
    protected VirtualKeyboardWidget       vkBasic;
    protected VirtualKeyboardWidget       vkExtra;
    protected VirtualKeyboardWidget       vkMouse;
    protected VirtualKeyboardWidget       vkNumpad;
    protected ValueMapList                listLayouts;
    protected EButton                     btnSave;
    protected EButton                     btnCancel;
    protected EButton                     btnClose;
    protected ELabel                      lblScreenLabel;
    protected ELabel                      lblCreditTitle;
    protected ELabel                      lblCreditName;

    public LayoutSelectionScreen(Screen parent) {
        super(parent, Text.literal("Keymap Layout"));
    }

    @Override protected void onInit() {
        KeyLayout layout = KeyLayout.getLayoutWithCode(KeymapConfig.instance().customLayout());
        KeymappingNotifier.load();

        scr = scrFromWidth(Math.min(450, width));
        initVks(layout);
        int spaceLeft = scr.w() - padding.x() * 3 - vkBasic.rect().w();

        listLayouts = new ValueMapList(font.lineHeight,
                vkBasic.right() + padding.x(),
                vkBasic.top(),
                spaceLeft,
                scr.h() - padding.y() * 4 - 16 * 2,
                false);

        for (Map.Entry<String, KeyLayout> v : KeyLayout.layouts().entrySet()) {
            listLayouts.addItem(new ValueMapList.ValueMapEntry<>(
                    KeyLayout.layouts().get(v.getKey()).meta().name(),
                    v.getKey(),
                    listLayouts));
        }

        listLayouts.onItemSelected(this::onLayoutSelected);
        listLayouts.setItemSelectedWithValue(KeymapConfig.instance().customLayout());

        btnSave   = new EButton(
                Text.translatable("keymap.btnSave"),
                listLayouts.left(),
                listLayouts.bottom() + padding.y(),
                (listLayouts.rect().w() - padding.x()) / 2,
                16
        );
        btnCancel = new EButton(
                Text.translatable("keymap.btnCancel"),
                listLayouts.right() - btnSave.rect().w(),
                btnSave.top(),
                btnSave.rect().w(),
                16
        );

        btnSave.clickAction(this::onBtnSaveClicked);
        btnCancel.clickAction(this::onBtnCancelClicked);

        lblScreenLabel = new ELabel(
                Text.translatable("keymap.scrLayout"),
                scr.left(), scr.top() + padding.y(), scr.w(), 16
        );
        lblCreditTitle = new ELabel(
                Text.translatable("keymap.lblCredits"),
                vkNumpad.right() + padding.x(),
                vkNumpad.top() + padding.y() * 2,
                vkBasic.right() - vkNumpad.right() - padding.x(),
                font.lineHeight
        );
        lblCreditName  = new ELabel(
                Text.literal(qAuthor(layout)).withStyle(Styles.headerBold()),
                lblCreditTitle.left(),
                lblCreditTitle.bottom() + padding.y(),
                lblCreditTitle.rect().w(),
                font.lineHeight
        );
        lblScreenLabel.center(true);
        lblCreditTitle.center(true);
        lblCreditName.center(true);

        creditVis(layout);

        btnClose = new EButton(Text.translatable("keymap.btnClearSearch"),
                listLayouts.right() - 16,
                scr.y() + padding.y(),
                16,
                16);

        btnClose.clickAction(this::onBtnCloseClicked);


        addRenderableWidget(listLayouts);
        addRenderableWidget(btnSave);
        addRenderableWidget(btnCancel);
        addRenderableWidget(lblScreenLabel);
        addRenderableWidget(lblCreditTitle);
        addRenderableWidget(lblCreditName);
        addRenderableWidget(btnClose);
    }

    protected void initVks(KeyLayout layout) {
        vks = VKUtil.genLayout(layout, scr.x() + padding.x(), scr.y() + padding.y() * 2 + 16);
        for (VirtualKeyboardWidget vk : vks) {
            addRenderableWidget(vk);
        }

        vkBasic  = vks.get(0);
        vkExtra  = vks.get(1);
        vkMouse  = vks.get(2);
        vkNumpad = vks.get(3);
    }

    protected void onBtnCloseClicked(EWidget source) {
        onClose();
    }

    protected String qAuthor(KeyLayout layout) {
        return layout.meta().author() == null ? "" : layout.meta().author();
    }

    protected void creditVis(KeyLayout layout) {
        String qa = qAuthor(layout);
        lblCreditTitle.visible(!qa.isBlank());
        lblCreditName.visible(!qa.isBlank());
        lblCreditName.text(Text.literal(qa).withStyle(Styles.headerBold()));
    }

    protected void onBtnSaveClicked(EWidget source) {
        // Is def a safe cast
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
        // Is def a safe cast
        ValueMapList.ValueMapEntry<String> selected = (ValueMapList.ValueMapEntry<String>) listLayouts.itemSelected();

        KeyLayout layout = KeyLayout.getLayoutWithCode(selected.value());
        for (VirtualKeyboardWidget vk : vks) {
            removeWidget(vk.destroy());
        }
        KeymappingNotifier.clearSubscribers();
        initVks(layout);

        creditVis(layout);
    }

    @Override public void onClose() {
        KeymappingNotifier.clearSubscribers();
        if (KeymapConfig.instance().firstOpenDone()) {
            super.onClose();
        } else {
            KeymapConfig.instance().firstOpenDone(true);
            KeymapConfig.save();
            Minecraft.getInstance().setScreen(new KeymapScreen(parent()));
        }
    }

    @Override protected void preRenderScreen(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        fill(poseStack, 0, 0, width, height, 0x55000000);
        if (scr != null) drawOutline(poseStack, scr, 0xFFFFFFFF);
    }
}
