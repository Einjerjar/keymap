package com.github.einjerjar.mc.keymap.screen.widgets;

import com.github.einjerjar.mc.keymap.screen.KeyboardLayout;
import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class KeyWidget extends FlatWidget {
    public KeyboardLayout.KeyboardKey key;
    public HashMap<Integer, ArrayList<KeyBinding>> mappedKeyCount = new HashMap<>();
    public boolean selected = false;
    static int baseWidth = 16;
    static final int baseHeight = 16;

    static final ColorGroup COL_NORMAL = ColorGroup.NORMAL;
    static final ColorGroup COL_SET = ColorGroup.GREEN;
    static final ColorGroup COL_CONFLICT = ColorGroup.RED;
    static final ColorGroup COL_SELECTED = ColorGroup.YELLOW;

    public KeyWidget(int x, int y, KeyboardLayout.KeyboardKey key, HashMap<Integer, ArrayList<KeyBinding>> mappedKeyCount) {
        super(x, y, baseWidth + key.extraWidth, baseHeight + key.extraHeight, null);
        this.key = key;
        this.mappedKeyCount = mappedKeyCount;
        this.enabled = key.enabled;
    }

    public void updateState() {
        int k = key.keyCode;
        if (!mappedKeyCount.containsKey(k)) colors = COL_NORMAL;
        else {
            int kc = mappedKeyCount.get(k).size();
            if (kc == 0) colors = COL_NORMAL;
            else if (kc == 1) colors = COL_SET;
            else colors = COL_CONFLICT;
        }
    }

    @Override
    public Text getToolTip() {
        return key.key.getLocalizedText();
    }

    @Override
    public void renderButton(MatrixStack m, int mouseX, int mouseY, float delta) {
        ColorGroup color = selected ? COL_SELECTED : colors;
        int cBorder = color.border.getVariant(enabled, mouseActive, hovered);
        int cFill = color.bg.getVariant(enabled, mouseActive, hovered);
        int cText = color.text.getVariant(enabled, mouseActive, hovered);
        drawBoxFilled(m, x, y, width, height, cBorder, cFill);
        drawWithShadow(m, key.text, x + halfWidth, y + halfHeight, cText, true, true);
    }
}
