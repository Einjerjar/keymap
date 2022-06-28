package com.github.einjerjar.mc.keymap.screen;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.keys.BasicKeyData;
import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import com.github.einjerjar.mc.keymap.keys.KeyboardKey;
import com.github.einjerjar.mc.keymap.screen.widgets.FlatKeyWidget;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.FlatScreen;
import com.github.einjerjar.mc.keymap.widgets.FlatWidgetBase;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.awt.im.InputContext;
import java.util.*;

public class TestingScreen extends FlatScreen {
    protected final Map<Integer, List<KeybindHolder>> mappedKeybindHolders = new HashMap<>();
    protected final Map<Integer, List<FlatKeyWidget>> mappedKeyWidgets = new HashMap<>();
    protected int expectedScreenWidth;
    protected int outPadX = 4;
    protected int outPadY = 4;
    protected int padX = 10;
    protected int padY = 10;
    protected int gapX = 2;
    protected int gapY = 2;
    protected int top;
    protected int left;
    protected int right;
    protected int bottom;
    Text keyPress;

    public TestingScreen() {
        super(Text.of("Test"));
    }

    public TestingScreen(Screen parent) {
        super(Text.of("Test"), parent);
    }

    @Override
    protected void init() {
        top = outPadY;
        left = Math.max(0, width - expectedScreenWidth) / 2 + outPadX;
        right = left + expectedScreenWidth - outPadX * 2;
        bottom = height - outPadY;

        int[] kbKeys   = addKeys(KeymapMain.keys().basic(), padX, top + padY);
        int[] kbExtra  = addKeys(KeymapMain.keys().extra(), padX, top + padY * 2 + kbKeys[1] - gapY);
        int[] kbMouse  = addKeys(KeymapMain.keys().mouse(), padX, top + padY * 3 + kbKeys[1] + kbExtra[1] - gapY * 2);
        int[] kbNumpad = addKeys(KeymapMain.keys().numpad(), padX * 2 + kbExtra[0] - gapX, top + padY * 2 + kbKeys[1] - gapY);
    }

    private int[] addKeys(List<List<BasicKeyData>> keys, int x, int y) {
        int sizeX    = 0;
        int sizeY    = 0;
        int currentX = x;
        int currentY = y;

        for (List<BasicKeyData> row : keys) {
            int minItemHeight = height;
            for (BasicKeyData keyData : row) {
                KeyboardKey   key  = new KeyboardKey(keyData);
                FlatKeyWidget k    = new FlatKeyWidget(currentX, currentY, key, mappedKeybindHolders);
                int           code = key.keyCode();

                k.action(button -> {
                });

                if (!mappedKeyWidgets.containsKey(code)) {
                    mappedKeyWidgets.put(code, new ArrayList<>());
                }
                mappedKeyWidgets.get(key.keyCode()).add(k);
                addSelectableChild(k);
                currentX += gapX + k.w();
                minItemHeight = Math.min(k.h(), minItemHeight);
            }
            sizeX = Math.max(currentX - x, sizeX);
            currentX = x;
            currentY += gapY + minItemHeight;
            sizeY = currentY - y;
        }

        return new int[]{sizeX, sizeY};
    }

    @Override
    public void renderScreen(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (keyPress != null) {
            WidgetUtils.drawCenteredText(matrices, textRenderer, keyPress, 0, 0, width, height + 10, true, true, false, 0x00ff00);
        }

        for (Element e : children()) {
            if (e instanceof FlatWidgetBase ee) {
                if (ee.visible()) {
                    ee.render(matrices, mouseX, mouseY, delta);
                }
            }
        }

        renderTooltips(matrices, mouseX, mouseY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            KeymapMain.reloadLayouts();
            this.onClose();
            return true;
        }
        keyPress = Text.of(String.format(
            "KeyCode: %s , ScanCode: %s , Modifiers: %s, Translation: %s , Translation2: %s",
            keyCode,
            scanCode,
            modifiers,
            InputUtil.fromKeyCode(keyCode, scanCode),
            InputUtil.Type.KEYSYM.createFromCode(keyCode)
        ));
        Locale locale = InputContext.getInstance().getLocale();
        if (locale == null) locale = Locale.getDefault();
        KeymapMain.LOGGER().info(locale.toString());
        return true;
    }
}
