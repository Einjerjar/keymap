package com.github.einjerjar.mc.widgets2;

import com.github.einjerjar.mc.widgets.utils.Rect;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;

@Accessors(fluent = true)
public class ELineToggleButton extends EToggleButton {
    protected Rect checkBox;

    public ELineToggleButton(Component text, int x, int y, int w, int h) {
        super(text, x, y, w, h);
    }

    @Override protected void init() {
        super.init();

        int boxS = rect.h() - padding.y() * 2;
        checkBox = new Rect(rect.right() - padding.x() - boxS, rect.top() + padding.y(), boxS, boxS);
    }

    @Override protected void preRenderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        U.bg(poseStack, rect, ColorOption.baseBG.fromState(state()));
    }

    @Override protected void onRenderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        drawString(poseStack, font, text, padding.x() + rect.left(), center().y() - font.lineHeight / 2 + 1, tColor());
        U.outline(poseStack, checkBox, ColorOption.baseFG.fromState(state()));
        if (value()) U.bg(poseStack, checkBox, ColorOption.baseFG.base());
    }
}
