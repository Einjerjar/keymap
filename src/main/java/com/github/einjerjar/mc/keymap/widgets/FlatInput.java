package com.github.einjerjar.mc.keymap.widgets;

import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
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

    protected KBAction onEnter;
    protected KBAction onEscape;
    protected KBAction onTextChanged;

    public FlatInput(int x, int y, int w, int h, String text) {
        super(FlatInput.class, x, y, w, h);
        this.text = Utils.or(text, "");
        this.lText = new LiteralText(this.text);
        this.setDrawBg(true).setDrawBorder(true).setVisible(true);
    }

    public FlatInput setOnTextChanged(KBAction onTextChanged) {
        this.onTextChanged = onTextChanged;
        return this;
    }

    public FlatInput setText(String text) {
        this.text = text;
        this.lText = new LiteralText(this.text);
        if (onTextChanged != null) {
            onTextChanged.run(this);
        }
        return this;
    }

    public FlatInput setOnEnter(KBAction onEnter) {
        this.onEnter = onEnter;
        return this;
    }

    public FlatInput setOnEscape(KBAction onEscape) {
        this.onEscape = onEscape;
        return this;
    }

    public String getText() {
        return text;
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
        if (cursorPosX != cursorPosX2) {
            delete(0, 0);
        }

        StringBuilder sb = new StringBuilder();
        for (Character c : cc.toCharArray()) {
            if (SharedConstants.isValidChar(c)) {
                sb.append(c);
            }
        }
        setText(new StringBuilder(text)
            .insert(cursorPosX, sb)
            .toString());
        setCursorPositionR(cc.length());
    }

    protected void write(Character c) {
        write(c + "");
    }

    protected void delete(int direction, int charCount) {
        // TODO: REMOVE LAZY
        int a = cursorPosX + direction * charCount;
        int b = cursorPosX;
        if (cursorPosX != cursorPosX2) {
            a = cursorPosX;
            b = cursorPosX2;
        }
        int left  = Math.min(a, b);
        int right = Math.max(a, b);

        if (left < 0) left = 0;
        setText(text.substring(0, left) +
            text.substring(right));
        setCursorPositionR(direction * (right - left));
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int cBg   = color.bg.getVariant(enabled, focused, hovered);
        int cBord = color.border.getVariant(enabled, focused, hovered);

        // WidgetUtils.drawBoxFilled(this, matrices, x, y, w, h, cBg, cBord);
        WidgetUtils.drawBoxFilled(this, matrices, x, y, w, h, color.border.normal, color.bg.normal);

        // fill(matrices, 10, 10, 50, 50, 0xff_000000);
        // KeymapMain.LOGGER.info("RENDER" + x + " " + y + " " + w + " " + h);

        // can do y+padY, w-padX*2 and h-padY*2 but will refrain since its easier that way
        WidgetUtils.drawCenteredText(matrices, tr, lText, x + padX, y, w, h, true, false, true, color.text.normal);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        write(chr);
        return super.charTyped(chr, modifiers);
    }

    public void setCursorPosition(int a, int b) {
        int left  = Math.min(a, b);
        int right = Math.max(a, b);

        this.cursorPosX = left;
        this.cursorPosX2 = right;
    }

    public void setCursorPosition(int a) {
        setCursorPosition(a, a);
    }

    public void setCursorPositionR(int a) {
        setCursorPosition(cursorPosX + a);
    }

    public void setCursorPosition(int a, boolean shift) {
        if (shift) {
            setCursorPosition(cursorPosX, a);
            return;
        }
        setCursorPosition(a, a);
    }

    public void setCursorPositionR(int a, boolean shift) {
        setCursorPosition(cursorPosX + x, shift);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        switch (keyCode) {
            // TODO: HANDLE CTRL PRESS ON MOVE
            case GLFW.GLFW_KEY_ENTER, GLFW.GLFW_KEY_KP_ENTER -> {
                if (onEnter != null) onEnter.run(this);
            }
            case GLFW.GLFW_KEY_END -> setCursorPosition(getTextLength() + 1, Screen.hasShiftDown());
            case GLFW.GLFW_KEY_HOME -> setCursorPosition(0, Screen.hasShiftDown());
            case GLFW.GLFW_KEY_RIGHT -> setCursorPositionR(1, Screen.hasShiftDown());
            case GLFW.GLFW_KEY_LEFT -> setCursorPositionR(-1, Screen.hasShiftDown());
            case GLFW.GLFW_KEY_BACKSPACE -> delete(-1, 1);
            case GLFW.GLFW_KEY_DELETE -> delete(1, 1);
            default -> {
                return false;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
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

    public interface KBAction {
        void run(FlatInput input);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isMouseOver(mouseX, mouseY)) {
            this.focused = true;
            playClickSound();
            return true;
        }
        this.focused = false;
        return false;
    }
}
