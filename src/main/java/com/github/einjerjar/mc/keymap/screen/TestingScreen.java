package com.github.einjerjar.mc.keymap.screen;

import com.github.einjerjar.mc.keymap.screen.widgets.FlatTextWidget;
import com.github.einjerjar.mc.keymap.screen.widgets.KeyCaptureWidget;
import fi.dy.masa.malilib.event.InputEventHandler;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.Nullable;

public class TestingScreen extends Screen {
    InputEventHandler handler;
    Screen parent = null;

    KeyCaptureWidget keyCaptureWidget;

    public TestingScreen() {
        super(new LiteralText("Test"));
        this.handler = (InputEventHandler) InputEventHandler.getInputManager();
    }

    public TestingScreen(Screen parent) {
        super(new LiteralText("Test"));
        this.handler = (InputEventHandler) InputEventHandler.getInputManager();
        this.parent = parent;
    }

    @Override
    protected void init() {
        keyCaptureWidget = new KeyCaptureWidget(0, 0, width, height, new LiteralText("SHIT"));
        addSelectableChild(keyCaptureWidget);
    }

    @Override
    public void render(MatrixStack m, int mouseX, int mouseY, float delta) {
        renderBackground(m);

        keyCaptureWidget.render(m, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
        if (parent != null) {
            //noinspection ConstantConditions
            client.setScreen(parent);
            return;
        }
        super.onClose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
