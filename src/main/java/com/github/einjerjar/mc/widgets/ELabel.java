package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;

@Accessors(fluent = true, chain = true)
public class ELabel extends EWidget {
    @Getter @Setter Component text;
    @Getter @Setter boolean centerX;
    @Getter @Setter boolean centerY;

    public ELabel(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public ELabel(Component text, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.text = text;
    }

    public ELabel(Rect rect) {
        super(rect);
    }

    public ELabel(Component text, Rect rect) {
        super(rect);
        this.text = text;
    }

    public void center(boolean x, boolean y) {
        this.centerX = x;
        this.centerY = y;
    }

    public void center(boolean xy) {
        this.centerX = xy;
        this.centerY = xy;
    }

    @Override protected void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        int x = centerX ? midX() : left();
        int y = centerY ? midY() : top();
        drawCenteredString(poseStack, font, text, x, y, colorVariant().text());
    }
}
