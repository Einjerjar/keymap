package com.github.einjerjar.mc.keymap.screen.containers;

import com.github.einjerjar.mc.keymap.widgets.FlatButton;
import com.github.einjerjar.mc.keymap.widgets.FlatContainer;
import com.github.einjerjar.mc.keymap.widgets.FlatText;
import com.github.einjerjar.mc.keymap.widgets.containers.FlexContainer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;

public class HotkeyCapture extends FlatContainer {
    FlatText labelContainer;
    FlatText labelHotkey;

    FlexContainer parentContainer;
    FlexContainer buttonContainer;

    FlatButton buttonOk;
    FlatButton buttonClear;
    FlatButton buttonCancel;

    CustomAction onOkAction;
    CustomAction onCancelAction;

    public interface CustomAction {
        void execute(HotkeyCapture cap);
    }

    List<InputUtil.Key> pressed = new ArrayList<>();

    public HotkeyCapture(int x, int y, int w, int h) {
        super(x, y, w, h);

        labelContainer = new FlatText(w / 2 + x, h / 2 + y - tr.fontHeight * 2, 0, 0, new LiteralText("Set Hotkey"));
        labelHotkey = new FlatText(w / 2 + x, h / 2 + y, 0, 0, new LiteralText("A + B + C"));

        buttonOk = new FlatButton(0, 0, 0, 0, new LiteralText("OK"));
        buttonClear = new FlatButton(0, 0, 0, 0, new LiteralText("Clear"));
        buttonCancel = new FlatButton(0, 0, 0, 0, new LiteralText("Cancel"));

        buttonOk.setAction(button -> ok());
        buttonClear.setAction(button -> clearKeys());
        buttonCancel.setAction(button -> close());

        buttonContainer = new FlexContainer(0, 0, 300, 20);
        parentContainer = new FlexContainer(w / 2 - 150 + x, h / 2 - 40 + y, 300, 80);

        parentContainer
            .addChild(labelContainer)
            .addChild(labelHotkey, 2)
            .addChild(buttonContainer)
            .setDirection(FlexContainer.FlexDirection.COLUMN)
            .arrange();

        buttonContainer
            .addChild(buttonOk)
            .addChild(buttonClear)
            .addChild(buttonCancel)
            .arrange();

        addSelectable(parentContainer);
    }

    public List<InputUtil.Key> getPressed() {
        return pressed;
    }

    public HotkeyCapture setOnOkAction(CustomAction onOkAction) {
        this.onOkAction = onOkAction;
        return this;
    }

    public HotkeyCapture setOnCancelAction(CustomAction onCancelAction) {
        this.onCancelAction = onCancelAction;
        return this;
    }

    public void ok() {
        if (onOkAction != null) {
            onOkAction.execute(this);
        }
        close();
    }

    public void clearKeys() {
        pressed.clear();
        labelHotkey.setText(new LiteralText(""));
    }

    public void close() {
        setEnabled(false);
        setVisible(false);
    }

    List<InputUtil.Key> allowedModifiers = new ArrayList<>() {{
        add(InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_LEFT_SHIFT));
        add(InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_RIGHT_SHIFT));
        add(InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_LEFT_CONTROL));
        add(InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_RIGHT_CONTROL));
        add(InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_LEFT_ALT));
        add(InputUtil.Type.KEYSYM.createFromCode(InputUtil.GLFW_KEY_RIGHT_ALT));
    }};

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

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.renderWidget(matrices, mouseX, mouseY, delta);
    }
}
