package com.github.einjerjar.mc.keymap.client.gui.screen;

import com.github.einjerjar.mc.keymap.objects.Credits;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.widgets.*;
import com.github.einjerjar.mc.widgets.utils.Text;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;

public class CreditsScreen extends EScreen {
    protected ScrollTextList listHelp;
    protected ELabel         lblTitle;
    protected EButton        btnClose;

    protected CreditsScreen(Screen parent) {
        super(parent, Text.translatable("keymap.scrCredits"));
    }

    @Override protected void onInit() {
        scr = scrFromWidth(Math.min(450, width));

        lblTitle = new ELabel(
                scr.left() + padding.x(),
                scr.top() + padding.y(),
                scr.w() - padding.x() * 2,
                16
        );
        lblTitle.text(Text.translatable("keymap.lblCredits"));
        lblTitle.center(true);

        StringBuilder layoutCredits   = new StringBuilder(Utils.translate("keymap.lblCreditsLayout"));
        StringBuilder languageCredits = new StringBuilder(Utils.translate("keymap.lblCreditsLanguage"));

        for (Credits.LayoutCredits lay : Credits.instance().layout()) {
            layoutCredits
                    .append("\n\n- ")
                    .append(lay.key());
            for (String name : lay.name()) {
                layoutCredits
                        .append("\n   - ")
                        .append(name);
            }
        }

        for (Credits.LanguageCredits lang : Credits.instance().language()) {
            languageCredits
                    .append("\n\n- ")
                    .append(lang.lang());
            for (String name : lang.name()) {
                languageCredits
                        .append("\n   - ")
                        .append(name);
            }
        }

        listHelp = ScrollTextList.createFromString(
                layoutCredits + "\n\n\n" + languageCredits,
                lblTitle.left(),
                lblTitle.bottom() + padding.y(),
                lblTitle.rect().w(),
                scr.h() - padding.y() * 3 - 16
        );

        btnClose = new EButton(Text.translatable("keymap.btnClearSearch"),
                listHelp.right() - 16,
                scr.y() + padding.y(),
                16,
                16);

        btnClose.clickAction(this::onBtnCloseClicked);

        addRenderableWidget(lblTitle);
        addRenderableWidget(listHelp);
        addRenderableWidget(btnClose);
    }

    protected void onBtnCloseClicked(EWidget source) {
        onClose();
    }

    @Override protected void preRenderScreen(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        fill(poseStack, 0, 0, width, height, 0x55000000);
        if (scr != null) drawOutline(poseStack, scr, 0xFFFFFFFF);
    }
}
