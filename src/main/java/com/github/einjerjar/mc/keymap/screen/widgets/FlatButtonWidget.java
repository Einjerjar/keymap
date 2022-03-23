package com.github.einjerjar.mc.keymap.screen.widgets;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class FlatButtonWidget extends FlatWidget {
    public Text text;

    public FlatButtonWidget(int x, int y, int width, int height, Text text, @Nullable FlatAction action) {
        super(x, y, width, height, action);
        this.text = text;
    }

    @Override
    public void renderButton(MatrixStack m, int mouseX, int mouseY, float delta) {
        super.renderButton(m, mouseX, mouseY, delta);
        drawWithShadow(m, text.getString(), x + halfWidth, y + halfHeight, colors.text.getVariant(enabled, mouseActive, hovered), true, true);
    }
}
