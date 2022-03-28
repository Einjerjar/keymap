package com.github.einjerjar.mc.keymap.screen;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import com.github.einjerjar.mc.keymap.keys.KeyboardLayout;
import com.github.einjerjar.mc.keymap.screen.widgets.FlatKeyWidget;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.FlatButton;
import com.github.einjerjar.mc.keymap.widgets.FlatScreen;
import com.github.einjerjar.mc.keymap.widgets.containers.FlexContainer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyMappingScreen2 extends FlatScreen {
    TextRenderer tr = textRenderer;

    boolean malilib;
    int outPadX = 4;
    int outPadY = 4;
    int padX = 10;
    int padY = 10;
    int gapX = 2;
    int gapY = 2;
    int expectedScreenWidth;
    int top;
    int left;
    int right;
    int bottom;

    private final Map<Integer, List<KeybindHolder>> mappedKeybindHolders = new HashMap<>();
    private final Map<Integer, List<FlatKeyWidget>> mappedKeyWidgets = new HashMap<>();

    FlexContainer buttonContainerSide;
    FlatButton buttonResetSelect;
    FlatButton buttonResetAll;

    public KeyMappingScreen2() {
        super(new LiteralText(""));
    }

    public KeyMappingScreen2(Screen parent) {
        super(new LiteralText(""), parent);
    }

    @Override
    protected void init() {
        tr = textRenderer;
        malilib = KeymapMain.malilibSupport;
        expectedScreenWidth = Math.min(500, width);

        mappedKeyWidgets.clear();
        mappedKeybindHolders.clear();

        top = outPadY;
        left = Math.max(0, width - expectedScreenWidth) / 2 + outPadX;
        right = left + expectedScreenWidth - outPadX * 2;
        bottom = height - outPadY;

        int[] kbKeys   = addKeys(KeyboardLayout.getKeys(), left + padX, top + padY);
        int[] kbExtra  = addKeys(KeyboardLayout.getExtra(), left + padX, top + padY * 2 + kbKeys[1] - gapY);
        int[] kbMouse  = addKeys(KeyboardLayout.getMouse(), left + padX, top + padY * 3 + kbKeys[1] + kbExtra[1] - gapY * 2);
        int[] kbNumpad = addKeys(KeyboardLayout.getNumpad(), left + padX * 2 + kbExtra[0] - gapX, top + padY * 2 + kbKeys[1] - gapY);

        int leftSpaceX = expectedScreenWidth - outPadX * 2 - kbKeys[0] - padX * 3;

        buttonResetSelect = new FlatButton(0, 0, 0, 0, new LiteralText("Reset"));
        buttonResetAll = new FlatButton(0, 0, 0, 0, new LiteralText("Reset All"));
        buttonContainerSide = new FlexContainer(right - leftSpaceX - padX, bottom - padY - outPadY - 16, leftSpaceX, 16);

        buttonContainerSide
            .addChild(buttonResetSelect)
            .addChild(buttonResetAll)
            .arrange();

        addSelectableChild(buttonContainerSide);
    }

    private int[] addKeys(List<List<KeyboardLayout.KeyboardKey>> keys, int x, int y) {
        int sizeX    = 0;
        int sizeY    = 0;
        int currentX = x;
        int currentY = y;

        for (List<KeyboardLayout.KeyboardKey> row : keys) {
            int minItemHeight = height;
            for (KeyboardLayout.KeyboardKey key : row) {
                FlatKeyWidget k    = new FlatKeyWidget(currentX, currentY, key, mappedKeybindHolders);
                int           code = key.keyCode;

                if (!mappedKeyWidgets.containsKey(code)) {
                    mappedKeyWidgets.put(code, new ArrayList<>());
                }
                mappedKeyWidgets.get(key.keyCode).add(k);
                addSelectableChild(k);
                currentX += gapX + k.getW();
                minItemHeight = Math.min(k.getH(), minItemHeight);
            }
            sizeX = Math.max(currentX - x, sizeX);
            currentX = x;
            currentY += gapY + minItemHeight;
            sizeY = currentY - y;
        }

        return new int[]{sizeX, sizeY};
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        WidgetUtils.drawBoxOutline(this, matrices, left, top, right - left, bottom - top, 0xff_ffffff);
    }
}
