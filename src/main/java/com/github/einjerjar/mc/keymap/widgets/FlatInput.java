package com.github.einjerjar.mc.keymap.widgets;

import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class FlatInput extends FlatWidget<FlatInput> implements Selectable {
    protected String text;
    protected Text lText;
    protected int cursorPosX;
    protected int cursorPosX2;
    protected int padX = 4;
    protected int padY = 2;
    protected int tick = 0;

    protected CommonAction onEnter;
    protected CommonAction onEscape;
    protected CommonAction onTextChanged;

    public FlatInput(int x, int y, int w, int h, String text) {
        super(FlatInput.class, x, y, w, h);
        this.text = Utils.or(text, "");
        this.lText = new LiteralText(this.text);
        this.setDrawBg(true).setDrawBorder(true).setVisible(true);
    }

    public FlatInput setText(String text, boolean reset) {
        this.text = text;
        this.lText = new LiteralText(this.text);
        if (onTextChanged != null) {
            onTextChanged.run(this);
        }
        if (reset) setCursorPosition(text.length());
        return this;
    }

    public void setOnTextChanged(CommonAction onTextChanged) {
        this.onTextChanged = onTextChanged;
    }

    public FlatInput setOnEnter(CommonAction onEnter) {
        this.onEnter = onEnter;
        return this;
    }

    public FlatInput setOnEscape(CommonAction onEscape) {
        this.onEscape = onEscape;
        return this;
    }

    public String getText() {
        return text;
    }

    public FlatInput setText(String text) {
        return setText(text, false);
    }

    public Text getLiteralText() {
        return lText;
    }

    public int getTextLength() {
        return text.length();
    }

    public int getCursorPosX() {
        return cursorPosX;
    }

    // Y is ignored for single line input (extend class for multi)
    protected void moveCursor(int x, int y) {
        cursorPosX += x;

        // clamp cursor
        if (cursorPosX < 0) cursorPosX = 0;
        if (cursorPosX >= getTextLength()) cursorPosX = getTextLength();
    }

    protected void write(String cc) {
        if (cc.isEmpty()) return;

        StringBuilder sb = new StringBuilder();
        for (Character c : cc.toCharArray()) {
            if (SharedConstants.isValidChar(c)) {
                sb.append(c);
            }
        }
        setText(text.substring(0, cursorPosX) + sb + text.substring(cursorPosX), false);
        setCursorPositionR(cc.length());
    }

    protected void write(Character c) {
        write(c + "");
    }

    protected void delete(int direction, int charCount) {
        // TODO: REMOVE LAZY

        if (text.length() == 0) return;
        StringBuilder sb = new StringBuilder(text);
        for (int i = 0; i < charCount; i++) {
            if (direction > 0) sb.deleteCharAt(cursorPosX);
            else if (direction < 0) sb.deleteCharAt(cursorPosX - i - 1);
        }
        setText(sb.toString(), false);
        setCursorPositionR(direction * charCount);
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        boolean cursorVis = tick < 20;
        if (tick >= 39) tick = 0;
        tick += 1;

        int cBg   = color.bg.getVariant(enabled, false, hovered);
        int cBord = color.border.getVariant(enabled, focused, hovered);
        int cText = color.text.normal;

        // bg+border
        WidgetUtils.drawBoxFilled(this, matrices, x, y, w, h, cBord, cBg);

        // text
        WidgetUtils.drawCenteredText(matrices, tr, lText, x + padX, y, w, h, true, false, true, cText);
        // WidgetUtils.drawCenteredText(matrices, tr, new LiteralText("" + cursorPosX), x + padX, y, w, h, true, false, false, ColorGroup.RED.text.normal);

        // cursor
        if (cursorVis && focused) {
            int cx = tr.getWidth(lText.getString().substring(0, Math.max(0, cursorPosX))) + 1;
            if (cursorPosX == text.length()) {
                WidgetUtils.drawHorizontalLine(this, matrices, x + padX + cx + 1, y + h - 6 + 1, 4, 0x88_000000);
                WidgetUtils.drawHorizontalLine(this, matrices, x + padX + cx, y + h - 6, 4, ColorGroup.RED.text.normal);
            } else {
                WidgetUtils.drawVerticalLine(this, matrices, x + padX + cx - 2 + 1, y + 3 + 1, tr.fontHeight, 0x88_000000);
                WidgetUtils.drawVerticalLine(this, matrices, x + padX + cx - 2, y + 3, tr.fontHeight, ColorGroup.RED.text.normal);
            }
        }
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        write(chr);
        return super.charTyped(chr, modifiers);
    }

    public void setCursorPosition(int a) {
        if (a < 0) a = 0;
        if (a > text.length()) a = text.length();
        this.cursorPosX = a;
    }

    public void setCursorPositionR(int a) {
        setCursorPosition(cursorPosX + a);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            // TODO: HANDLE CTRL PRESS ON MOVE
            case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                if (onEnter != null) onEnter.run(this);
            }
            case GLFW.GLFW_KEY_END -> setCursorPosition(getTextLength() + 1);
            case GLFW.GLFW_KEY_HOME -> setCursorPosition(0);
            case GLFW.GLFW_KEY_RIGHT -> setCursorPositionR(1);
            case GLFW.GLFW_KEY_LEFT -> setCursorPositionR(-1);
            case GLFW.GLFW_KEY_BACKSPACE -> delete(-1, 1);
            case GLFW.GLFW_KEY_DELETE -> delete(1, 1);
            default -> {
                return false;
            }
        }
        return true;
        // return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public SelectionType getType() {
        return focused
               ? SelectionType.FOCUSED
               : hovered
                 ? SelectionType.HOVERED
                 : SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return isMouseOver(mouseX, mouseY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            this.focused = true;
            playClickSound();
            return true;
        }
        this.focused = false;
        return false;
    }
}
