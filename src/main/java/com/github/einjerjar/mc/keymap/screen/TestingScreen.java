package com.github.einjerjar.mc.keymap.screen;

import com.github.einjerjar.mc.keymap.keys.key.VanillaKeybind;
import com.github.einjerjar.mc.keymap.screen.entrylist.FlatKeyList;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.FlatInput;
import com.github.einjerjar.mc.keymap.widgets.FlatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class TestingScreen extends FlatScreen {
    FlatKeyList keyList;
    FlatInput input;

    public TestingScreen() {
        super(new LiteralText("Test"));
    }

    public TestingScreen(Screen parent) {
        super(new LiteralText("Test"), parent);
    }

    @Override
    protected void init() {
        keyList = new FlatKeyList(10, 40, width - 20, height - 50, textRenderer.fontHeight);

        //noinspection ConstantConditions
        for (KeyBinding kb : client.options.keysAll) {
            keyList.addEntry(new FlatKeyList.FlatKeyListEntry(new VanillaKeybind(kb)));
        }

        input = new FlatInput(10, 10, width - 20, 20, "");
        addSelectableChild(keyList);
        addSelectableChild(input);
    }

    @Override
    public void render(MatrixStack m, int mouseX, int mouseY, float delta) {
        renderBackground(m);
        super.render(m, mouseX, mouseY, delta);
        keyList.render(m, mouseX, mouseY, delta);

        renderTooltips(m, mouseX, mouseY, delta);
        if (hovered != null)
            WidgetUtils.drawCenteredText(m, textRenderer, new LiteralText(hovered.getClass().getName()), 0, 0, 0, 0, true, false, false, 0xff_00ff00);
        if (getFocused() != null)
            WidgetUtils.drawCenteredText(m, textRenderer, new LiteralText(getFocused().getClass().getName()), 0, 10, 0, 0, true, false, false, 0xff_ff0000);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (getFocused() != null && getFocused().keyPressed(keyCode, scanCode, modifiers)) return true;
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
