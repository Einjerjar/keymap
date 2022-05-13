package com.github.einjerjar.mc.keymap.screen.containers;

import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.widgets.FlatButton;
import com.github.einjerjar.mc.keymap.widgets.FlatContainer;
import com.github.einjerjar.mc.keymap.widgets.FlatText;
import com.github.einjerjar.mc.keymap.widgets.containers.FlexContainer;
import fi.dy.masa.malilib.util.KeyCodes;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true, chain = true)
public class HotkeyCapture extends FlatContainer {
    protected FlatText labelContainer;
    protected FlatText labelHotkey;

    protected FlexContainer parentContainer;
    protected FlexContainer buttonContainer;

    protected FlatButton buttonOk;
    protected FlatButton buttonClear;
    protected FlatButton buttonCancel;

    @Setter protected CommonAction onOkAction;
    @Setter protected CommonAction onCancelAction;
    @Setter protected CommonAction onCloseAction;
    @Getter protected List<InputUtil.Key> pressed = new ArrayList<>();
    protected List<InputUtil.Key> allowedModifiers = new ArrayList<>() {{
        add(InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_LEFT_SHIFT));
        add(InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_RIGHT_SHIFT));
        add(InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_LEFT_CONTROL));
        add(InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_RIGHT_CONTROL));
        add(InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_LEFT_ALT));
        add(InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_RIGHT_ALT));
    }};

    public HotkeyCapture(int x, int y, int w, int h) {
        super(x, y, w, h);

        color = ColorGroup.fromBaseColor(0xff_333333);
        color.bg.normal = (color.bg.normal & 0x00_ffffff) | 0xee_000000;

        labelContainer = new FlatText(w / 2 + x, h / 2 + y - tr.fontHeight * 2, 0, 0, new TranslatableText("key.keymap.set_hotkey"));
        labelHotkey = new FlatText(w / 2 + x, h / 2 + y, 0, 0, new LiteralText("A + B + C"));

        buttonOk = new FlatButton(0, 0, 0, 0, new TranslatableText("key.keymap.ok"));
        buttonClear = new FlatButton(0, 0, 0, 0, new TranslatableText("key.keymap.clear"));
        buttonCancel = new FlatButton(0, 0, 0, 0, new TranslatableText("key.keymap.cancel"));

        buttonOk.action(button -> ok());
        buttonClear.action(button -> clearKeys());
        buttonCancel.action(button -> close());

        buttonContainer = new FlexContainer(0, 0, 300, 20);
        parentContainer = new FlexContainer(w / 2 - 150 + x, h / 2 - 40 + y, 300, 80);

        parentContainer
            .addChild(labelContainer)
            .addChild(labelHotkey, 2)
            .addChild(buttonContainer)
            .direction(FlexContainer.FlexDirection.COLUMN)
            .arrange();

        buttonContainer
            .addChild(buttonOk)
            .addChild(buttonClear)
            .addChild(buttonCancel)
            .arrange();

        addSelectable(parentContainer);
    }

    public HotkeyCapture clear() {
        pressed.clear();
        return this;
    }

    public HotkeyCapture add(InputUtil.Key k) {
        pressed.add(k);
        return this;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) return true;
        int btn = 0;

        switch (button) {
            case GLFW.GLFW_MOUSE_BUTTON_1 -> btn = KeyCodes.MOUSE_BUTTON_1;
            case GLFW.GLFW_MOUSE_BUTTON_2 -> btn = KeyCodes.MOUSE_BUTTON_2;
            case GLFW.GLFW_MOUSE_BUTTON_3 -> btn = KeyCodes.MOUSE_BUTTON_3;
            case GLFW.GLFW_MOUSE_BUTTON_4 -> btn = KeyCodes.MOUSE_BUTTON_4;
            case GLFW.GLFW_MOUSE_BUTTON_5 -> btn = KeyCodes.MOUSE_BUTTON_5;
            case GLFW.GLFW_MOUSE_BUTTON_6 -> btn = KeyCodes.MOUSE_BUTTON_6;
            case GLFW.GLFW_MOUSE_BUTTON_7 -> btn = KeyCodes.MOUSE_BUTTON_7;
            case GLFW.GLFW_MOUSE_BUTTON_8 -> btn = KeyCodes.MOUSE_BUTTON_8;
        }

        return keyPressed(btn, 0, 0);
    }

    public void ok() {
        if (onOkAction != null) {
            onOkAction.run(this);
        }
        close();
    }

    public void clearKeys() {
        pressed.clear();
        labelHotkey.setText(new LiteralText(""));
    }

    public void close() {
        active(false);
        if (onCloseAction != null) {
            onCloseAction.run(this);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) buttonCancel.click();

        InputUtil.Key k = InputUtil.Type.KEYSYM.createFromCode(keyCode);

        int currentModifiers = 0;
        for (InputUtil.Key kk : pressed) {
            if (allowedModifiers.contains(kk)) currentModifiers++;
        }
        if (!pressed.contains(k) && (pressed.size() < 2 + currentModifiers || allowedModifiers.contains(k))) {
            pressed.add(k);
            StringBuilder sb = new StringBuilder();
            for (InputUtil.Key kk : pressed) {
                sb.append(kk.getLocalizedText().getString()).append(" + ");
            }
            sb.delete(sb.length() - 3, sb.length());
            labelHotkey.setText(new LiteralText(sb.toString()));
        }

        return true;
    }

    public HotkeyCapture updateState() {
        StringBuilder sb = new StringBuilder();
        for (InputUtil.Key kk : pressed) {
            sb.append(kk.getLocalizedText().getString()).append(" + ");
        }
        if (sb.length() > 3) sb.delete(sb.length() - 3, sb.length());
        labelHotkey.setText(new LiteralText(sb.toString()));

        return this;
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderWidget(matrices, mouseX, mouseY, delta);
    }
}
