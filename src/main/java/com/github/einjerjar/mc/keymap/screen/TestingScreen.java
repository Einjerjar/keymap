package com.github.einjerjar.mc.keymap.screen;

import com.github.einjerjar.mc.keymap.keys.key.VanillaKeybind;
import com.github.einjerjar.mc.keymap.screen.entrylist.FlatKeyList;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.FlatInput;
import com.github.einjerjar.mc.keymap.widgets.FlatScreen;
import com.github.einjerjar.mc.keymap.widgets.FlatText;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class TestingScreen extends FlatScreen {
    Text keyPress;

    public TestingScreen() {
        super(new LiteralText("Test"));
    }

    public TestingScreen(Screen parent) {
        super(new LiteralText("Test"), parent);
    }

    @Override
    protected void init() {

    }

    @Override
    public void renderScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (keyPress != null) {
            WidgetUtils.drawCenteredText(matrices, textRenderer, keyPress, 0, 0, width, height, 0x00ff00);
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        keyPress = new LiteralText(String.format(
            "KeyCode: %s , ScanCode: %s , Modifiers: %s, Translation: %s , Translation2: %s",
            keyCode,
            scanCode,
            modifiers,
            InputUtil.fromKeyCode(keyCode, scanCode),
            InputUtil.Type.KEYSYM.createFromCode(keyCode)
        ));
        return true;
    }
}
