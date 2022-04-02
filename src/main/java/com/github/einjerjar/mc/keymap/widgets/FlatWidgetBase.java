package com.github.einjerjar.mc.keymap.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;

public abstract class FlatWidgetBase extends DrawableHelper implements Element, Drawable {
    protected TextRenderer tr;

    public boolean hovered = false;
    public boolean focused = false;
    public boolean visible = true;
    public boolean enabled = true;

    protected int x;
    protected int y;
    protected int w;
    protected int h;

    public FlatWidgetBase(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tr = MinecraftClient.getInstance().textRenderer;
    }

    protected void updateSize() {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

    public FlatWidgetBase setX(int x) {
        this.x = x;
        updateSize();
        return this;
    }

    public FlatWidgetBase setY(int y) {
        this.y = y;
        updateSize();
        return this;
    }

    public FlatWidgetBase setH(int h) {
        this.h = h;
        updateSize();
        return this;
    }

    public FlatWidgetBase setW(int w) {
        this.w = w;
        updateSize();
        return this;
    }

    public FlatWidgetBase setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public FlatWidgetBase setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }
}
