package com.github.einjerjar.mc.keymap.client.gui.screen;

import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.widgets.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class HelpScreen extends EScreen {
    protected ScrollTextList listHelp;
    protected ELabel lblTitle;
    protected EButton btnClose;

    protected HelpScreen(Screen parent) {
        super(parent, Component.translatable("keymap.scrHelp"));
    }

    @Override
    protected void onInit() {
        scr = scrFromWidth(Math.min(450, width));

        lblTitle = new ELabel(scr.left() + padding.x(), scr.top() + padding.y(), scr.w() - padding.x() * 2, 16);
        lblTitle.text(Component.translatable("keymap.scrHelp"));
        lblTitle.center(true);

        listHelp = ScrollTextList.createFromString(
                Utils.translate("keymap.textHelp"),
                lblTitle.left(),
                lblTitle.bottom() + padding.y(),
                lblTitle.rect().w(),
                scr.h() - padding.y() * 3 - 16);

        btnClose = new EButton(
                Component.translatable("keymap.btnClearSearch"), listHelp.right() - 16, scr.y() + padding.y(), 16, 16);

        btnClose.clickAction(this::onBtnCloseClicked);

        addRenderableWidget(lblTitle);
        addRenderableWidget(listHelp);
        addRenderableWidget(btnClose);
    }

    protected void onBtnCloseClicked(EWidget source) {
        onClose();
    }

    @Override
    protected void preRenderScreen(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, width, height, 0x55000000);
        if (scr != null) drawOutline(guiGraphics, scr, 0xFFFFFFFF);
    }
}
