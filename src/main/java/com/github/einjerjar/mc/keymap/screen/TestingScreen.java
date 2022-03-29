package com.github.einjerjar.mc.keymap.screen;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.key.VanillaKeybind;
import com.github.einjerjar.mc.keymap.screen.entrylist.FlatKeyList;
import com.github.einjerjar.mc.keymap.widgets.FlatScreen;
import com.github.einjerjar.mc.keymap.widgets.containers.FlatEntryList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.client.option.KeyBinding;

public class TestingScreen extends FlatScreen {
    FlatKeyList keyList;

    public TestingScreen() {
        super(new LiteralText("Test"));
    }

    public TestingScreen(Screen parent) {
        super(new LiteralText("Test"), parent);
    }

    @Override
    protected void init() {
        keyList = new FlatKeyList(10, 10, width - 20, height - 20, textRenderer.fontHeight);

        //noinspection ConstantConditions
        for (KeyBinding kb : client.options.keysAll) {
            keyList.addEntry(new FlatKeyList.FlatKeyListEntry(new VanillaKeybind(kb)));
        }

        addSelectableChild(keyList);
    }

    @Override
    public void render(MatrixStack m, int mouseX, int mouseY, float delta) {
        super.render(m, mouseX, mouseY, delta);
        renderBackground(m);
        keyList.render(m, mouseX, mouseY, delta);

        renderTooltips(m, mouseX, mouseY, delta);
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
