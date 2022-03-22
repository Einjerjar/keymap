package com.github.einjerjar.mc.keymap.screen.widgets;

import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class FlatWidget extends ClickableWidget {
    TextRenderer tr;
    FlatAction action = null;
    public boolean mouseActive = false;
    public boolean enabled = true;
    public ArrayList<InputUtil.Key> assignedKeys = new ArrayList<>();
    int halfWidth;
    int halfHeight;

    public ColorGroup colors = ColorGroup.NORMAL;

    public interface FlatAction {
        void execute(FlatWidget widget);
    }

    public FlatWidget(int x, int y, int width, int height, @Nullable FlatAction action) {
        super(x, y, width, height, null);
        this.tr = MinecraftClient.getInstance().textRenderer;
        this.action = action;
        this.halfWidth = width / 2;
        this.halfHeight = height / 2;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // if (!enabled || !isHovered()) return false;
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        mouseActive = true;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        if (action != null) action.execute(this);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        mouseActive = false;
        if (isHovered()) {
            return super.mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
    }

    @Override
    public void renderButton(MatrixStack m, int mouseX, int mouseY, float delta) {
        int cBorder = colors.border.getVariant(enabled, mouseActive, hovered);
        int cFill = colors.bg.getVariant(enabled, mouseActive, hovered);
        drawBoxFilled(m, x, y, width, height, cBorder, cFill);
    }

    public Text getToolTip() {
        return new LiteralText("Text");
    }

    public void drawText(MatrixStack m, String s, int x, int y, int color, boolean shadow, boolean centerX, boolean centerY) {
        if (centerX) x += /* (width / 2) */ -(tr.getWidth(s) / 2);
        if (centerY) y += /* (height / 2) */ -(tr.fontHeight / 2);

        if (shadow)
            tr.drawWithShadow(m, s, x, y, color);
        else
            tr.draw(m, s, x, y, color);
    }

    public void drawText(MatrixStack m, String s, int x, int y, int color, boolean centerX, boolean centerY) {
        drawText(m, s, x, y, color, false, centerX, centerY);
    }

    public void drawWithShadow(MatrixStack m, String s, int x, int y, int color, boolean centerX, boolean centerY) {
        drawText(m, s, x, y, color, true, centerX, centerY);
    }

    public void drawBoxFilled(MatrixStack m, int x, int y, int w, int h, int colBorder, int colFill) {
        fillBox(m, x + 1, y + 1, w - 2, h - 2, colFill);
        drawBoxOutline(m, x, y, w, h, colBorder);
    }

    public void drawBoxOutline(MatrixStack m, int x, int y, int w, int h, int color) {
        drawHorizontalLine(m, x, y, w, color);
        drawHorizontalLine(m, x, y + h - 1, w, color);
        drawVerticalLine(m, x, y + 1, h - 2, color);
        drawVerticalLine(m, x + w - 1, y + 1, h - 2, color);
    }

    public void drawHorizontalLine(MatrixStack m, int x, int y, int w, int color) {
        fillBox(m, x, y, w, 1, color);
    }

    public void drawVerticalLine(MatrixStack m, int x, int y, int h, int color) {
        fillBox(m, x, y, 1, h, color);
    }

    public void fillBox(MatrixStack m, int x, int y, int w, int h, int color) {
        int x2 = x + w;
        int y2 = y + h;
        fill(m, x, y, x2, y2, color);
    }
}
