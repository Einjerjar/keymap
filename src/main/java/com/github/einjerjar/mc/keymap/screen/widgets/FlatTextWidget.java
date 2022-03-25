package com.github.einjerjar.mc.keymap.screen.widgets;

import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.ColorSet;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public class FlatTextWidget extends DrawableHelper implements Drawable, Element {
    TextRenderer tr;
    Text text;
    int x;
    int y;
    ColorGroup color;
    boolean drawBg;
    boolean drawBorder;
    boolean drawShadow;
    int padX = 0;
    int padY = 0;

    public static FlatTextWidget builder(int x, int y, Text text) {
        return new FlatTextWidget(x, y, text);
    }

    public FlatTextWidget(int x, int y, Text text) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.tr = MinecraftClient.getInstance().textRenderer;
        this.color = ColorGroup.NORMAL;
    }

    public FlatTextWidget setText(Text text) {
        this.text = text;
        return this;
    }

    public FlatTextWidget setDrawBg(boolean drawBg) {
        this.drawBg = drawBg;
        return this;
    }

    public FlatTextWidget setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
        return this;
    }

    public FlatTextWidget setDrawShadow(boolean drawShadow) {
        this.drawShadow = drawShadow;
        return this;
    }

    @Override
    public void render(MatrixStack m, int mouseX, int mouseY, float delta) {
        int textWidth = tr.getWidth(text) + padX * 2;
        int textHeight = tr.fontHeight + padY * 2;

        if (drawBg || drawBorder) {
            if (drawBg) Utils.fillBox(this, m, x - textWidth / 2 - padX - 1, y - textHeight / 2 - padY - 1, textWidth+1, textHeight+1, color.bg.normal);
            if (drawBorder) Utils.drawBoxOutline(this, m, x - textWidth / 2 - padX - 1, y - textHeight / 2 - padY - 1, textWidth+1, textHeight+1, color.bg.normal);
        }


        if (drawShadow) tr.drawWithShadow(m, text, x - textWidth / 2f, y - textHeight / 2f, color.text.normal);
        else            tr.draw(m, text, x - (float)(textWidth / 2), y - (float)(textHeight / 2), color.text.normal);
    }
}
