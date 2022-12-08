package com.github.einjerjar.mc.keymap.client.gui.screen;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.widgets.utils.Point;
import com.github.einjerjar.mc.widgets.utils.Text;
import com.github.einjerjar.mc.widgets2.ELabel2;
import com.github.einjerjar.mc.widgets2.ELineToggleButton;
import com.github.einjerjar.mc.widgets2.EScreen2;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

public class ConfigScreen extends EScreen2 {

    protected ConfigScreen(Screen parent) {
        super(parent);
    }

    @Override protected void onInit() {
        children.clear();
        Style bold = Style.EMPTY.withBold(true);

        int h = font.lineHeight + 8;

        // General
        ELabel2 labelGeneral = new ELabel2(
                Text.translatable("keymap.catGeneral").withStyle(bold),
                scr.left(), scr.top(), scr.w(), h);
        ELineToggleButton toggleReplaceKeybind = new ELineToggleButton(
                Text.translatable("keymap.optReplaceKeybindScreen"),
                scr.left(), scr.top() + h, scr.w(), h);

        toggleReplaceKeybind.padding(new Point<>(4));
        toggleReplaceKeybind.value(KeymapConfig.instance().replaceKeybindScreen());
        toggleReplaceKeybind.onToggle(self -> KeymapConfig.instance().replaceKeybindScreen(self.value()));

        // Layout
        ELabel2 labelLayout = new ELabel2(
                Text.translatable("keymap.catLayout").withStyle(bold),
                scr.left(), scr.top() + h * 2, scr.w(), h);
        ELineToggleButton toggleAutoSelectLayout = new ELineToggleButton(
                Text.translatable("keymap.optAutoSelectLayout"),
                scr.left(), scr.top() + h * 3, scr.w(), h);

        toggleAutoSelectLayout.padding(new Point<>(4));
        toggleAutoSelectLayout.value(KeymapConfig.instance().autoSelectLayout());
        toggleAutoSelectLayout.onToggle(self -> KeymapConfig.instance().autoSelectLayout(self.value()));

        // Tooltips
        ELabel2 labelTooltips = new ELabel2(
                Text.translatable("keymap.catTooltips").withStyle(bold),
                scr.left(), scr.top() + h * 4, scr.w(), h);
        ELineToggleButton toggleShowHelpTooltips = new ELineToggleButton(
                Text.translatable("keymap.optShowHelpTooltips"),
                scr.left(), scr.top() + h * 5, scr.w(), h);

        toggleShowHelpTooltips.padding(new Point<>(4));
        toggleShowHelpTooltips.value(KeymapConfig.instance().showHelpTooltips());
        toggleShowHelpTooltips.onToggle(self -> KeymapConfig.instance().showHelpTooltips(self.value()));

        // Extra
        ELabel2 labelExtra = new ELabel2(
                Text.translatable("keymap.catExtra").withStyle(bold),
                scr.left(), scr.top() + h * 6, scr.w(), h);
        ELineToggleButton toggleFirstOpenDone = new ELineToggleButton(
                Text.translatable("keymap.optFirstOpenDoneExtra"),
                scr.left(), scr.top() + h * 7, scr.w(), h);
        ELineToggleButton toggleDebug = new ELineToggleButton(
                Text.translatable("keymap.optDebug"),
                scr.left(), scr.top() + h * 8, scr.w(), h);
        ELineToggleButton toggleDebug2 = new ELineToggleButton(
                Text.translatable("keymap.optDebug2"),
                scr.left(), scr.top() + h * 9, scr.w(), h);
        ELineToggleButton toggleMad = new ELineToggleButton(
                Text.translatable("keymap.optCrashOnProblematicError"),
                scr.left(), scr.top() + h * 10, scr.w(), h);

        toggleFirstOpenDone.padding(new Point<>(4));
        toggleFirstOpenDone.value(KeymapConfig.instance().firstOpenDone());
        toggleFirstOpenDone.onToggle(self -> KeymapConfig.instance().firstOpenDone(self.value()));

        toggleDebug.padding(new Point<>(4));
        toggleDebug.value(KeymapConfig.instance().debug());
        toggleDebug.onToggle(self -> KeymapConfig.instance().debug(self.value()));

        toggleDebug2.padding(new Point<>(4));
        toggleDebug2.value(KeymapConfig.instance().debug2());
        toggleDebug2.onToggle(self -> KeymapConfig.instance().debug2(self.value()));

        toggleMad.padding(new Point<>(4));
        toggleMad.value(KeymapConfig.instance().crashOnProblematicError());
        toggleMad.onToggle(self -> KeymapConfig.instance().crashOnProblematicError(self.value()));

        children.add(labelGeneral);
        children.add(toggleReplaceKeybind);

        children.add(labelLayout);
        children.add(toggleAutoSelectLayout);

        children.add(labelTooltips);
        children.add(toggleShowHelpTooltips);

        children.add(labelExtra);
        children.add(toggleFirstOpenDone);
        children.add(toggleDebug);
        children.add(toggleDebug2);
        children.add(toggleMad);
    }

    @Override protected void preRender(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        super.preRender(poseStack, mouseX, mouseY, partialTick);
    }

    @Override protected void postRender(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        // postRender
    }

    @Override public void onClose() {
        KeymapConfig.save();
        super.onClose();
    }
}
