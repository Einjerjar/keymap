package com.github.einjerjar.mc.widgets2;

import com.github.einjerjar.mc.widgets.utils.Rect;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
public class ELineToggleButton extends EToggleButton {
    protected Rect checkBox;

    public ELineToggleButton(Component text, int x, int y, int w, int h) {
        super(text, x, y, w, h);
    }

    @Override
    protected void init() {
        super.init();

        int boxS = rect.h() - padding.y() * 2;
        checkBox = new Rect(rect.right() - padding.x() - boxS, rect.top() + padding.y(), boxS, boxS);
    }

    @Override
    protected void preRenderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        U.bg(guiGraphics, rect, ColorOption.baseBG.fromState(state()));
    }

    @Override
    protected void onRenderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.drawString(font, text, padding.x() + rect.left(), center().y() - font.lineHeight / 2 + 1, tColor());
        U.outline(guiGraphics, checkBox, ColorOption.baseFG.fromState(state()));
        if (value()) U.bg(guiGraphics, checkBox, ColorOption.baseFG.base());
    }
}
