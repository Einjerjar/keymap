package com.github.einjerjar.mc.keymap.widgets;

import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;

public class FlatWidget<T extends FlatWidget<?>> extends FlatWidgetBase {
    protected TextRenderer tr;
    protected int x;
    protected int y;
    protected int w;
    protected int h;

    protected boolean hovered = false;
    protected boolean visible = true;
    protected boolean focused = false;

    protected boolean drawBg = false;
    protected boolean drawBorder = false;
    protected boolean drawShadow = false;
    protected ColorGroup color = ColorGroup.NORMAL;

    protected final T self;

    public FlatWidget(final Class<T> self, int x, int y, int w, int h) {
        this.self = self.cast(this);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tr = MinecraftClient.getInstance().textRenderer;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public T setVisible(boolean visible) {
        this.visible = visible;
        return self;
    }

    public T setFocused(boolean focused) {
        this.focused = focused;
        return self;
    }

    public T setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public T setX(int x) {
        this.x = x;
        return self;
    }

    public T setY(int y) {
        this.y = y;
        return self;
    }

    public T setW(int w) {
        this.w = w;
        return self;
    }

    public T setH(int h) {
        this.h = h;
        return self;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    public void playDownSound() {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!visible) return;
    }

    public boolean isActive() {
        return enabled && visible;
    }

    public T setActive(boolean active) {
        this.enabled = active;
        this.visible = active;
        return self;
    }

    protected void drawCenteredText(MatrixStack matrices, Text text, int x, int y, boolean shadow, boolean centerX, boolean centerY, int color) {
        x = centerX ? x + w / 2 - tr.getWidth(text) / 2 : x;
        y = centerY ? y + h / 2 - tr.fontHeight / 2 : y;
        if (shadow) tr.drawWithShadow(matrices, text, x, y, color);
        else tr.draw(matrices, text, x, y, color);
    }

    protected void drawCenteredText(MatrixStack matrices, Text text, int x, int y, boolean shadow, int color) {
        drawCenteredText(matrices, text, x, y, true, true, true, color);
    }

    protected void drawCenteredText(MatrixStack matrices, Text text, int x, int y, int color) {
        drawCenteredText(matrices, text, x, y, true, color);
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
}
