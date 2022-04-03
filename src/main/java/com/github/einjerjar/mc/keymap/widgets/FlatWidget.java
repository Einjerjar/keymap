package com.github.einjerjar.mc.keymap.widgets;

import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class FlatWidget<T extends FlatWidget<?>> extends FlatWidgetBase {

    protected final T self;
    protected boolean drawBg = false;
    protected boolean drawBorder = false;
    protected boolean drawShadow = false;
    protected ColorGroup color = ColorGroup.NORMAL;

    public FlatWidget(final Class<T> self, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.self = self.cast(this);
    }


    public boolean isEnabled() {
        return enabled;
    }

    public T setFocused(boolean focused) {
        this.focused = focused;
        return self;
    }

    public T setDrawBg(boolean drawBg) {
        this.drawBg = drawBg;
        return self;
    }

    public T setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
        return self;
    }

    public T setDrawShadow(boolean drawShadow) {
        this.drawShadow = drawShadow;
        return self;
    }

    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
    }

    public boolean isActive() {
        return enabled && visible;
    }

    public T setActive(boolean active) {
        this.enabled = active;
        this.visible = active;
        return self;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        hovered = isMouseOver(mouseX, mouseY);
        renderWidget(matrices, mouseX, mouseY, delta);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return WidgetUtils.inBounds((int) mouseX, (int) mouseY, x, y, w, h);
    }

    protected void drawCenteredText(MatrixStack matrices, Text text, int x, int y, boolean shadow, boolean centerX, boolean centerY, int color) {
        x = centerX ? x + w / 2 - tr.getWidth(text) / 2 : x;
        y = centerY ? y + h / 2 - tr.fontHeight / 2 : y;
        if (shadow) tr.drawWithShadow(matrices, text, x, y, color);
        else tr.draw(matrices, text, x, y, color);
    }

    protected void drawCenteredText(MatrixStack matrices, Text text, int x, int y, boolean shadow, int color) {
        drawCenteredText(matrices, text, x, y, shadow, true, true, color);
    }

    protected void drawCenteredText(MatrixStack matrices, Text text, int x, int y, int color) {
        drawCenteredText(matrices, text, x, y, true, color);
    }
}
