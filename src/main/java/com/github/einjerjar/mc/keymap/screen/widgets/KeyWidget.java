package com.github.einjerjar.mc.keymap.screen.widgets;

import com.github.einjerjar.mc.keymap.screen.KeyboardLayout;
import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;

import java.util.ArrayList;
import java.util.HashMap;

public class KeyWidget extends FlatWidget {
    public KeyboardLayout.KeyboardKey key;
    public HashMap<Integer, ArrayList<KeyBinding>> mappedKeyCount;
    public boolean selected = false;

    private final ArrayList<Text> tooltips = new ArrayList<>();

    static final int baseWidth = 16;
    static final int baseHeight = 16;

    static final ColorGroup COL_NORMAL = ColorGroup.NORMAL;
    static final ColorGroup COL_SET = ColorGroup.GREEN;
    static final ColorGroup COL_CONFLICT = ColorGroup.RED;
    static final ColorGroup COL_SELECTED = ColorGroup.YELLOW;

    static final Style keyStyle = new Style(TextColor.fromRgb(0xff_55ff55), true, true, false, false, false, null, null, "", null);
    static final Style keyBindStyle = new Style(TextColor.fromRgb(0xff_ffffff), false, false, false, false, false, null, null, "", null);
    static final Style separatorStyle = new Style(TextColor.fromRgb(0xff_555555), false, false, false, false, false, null, null, "", null);

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

        tooltips.clear();
        tooltips.add(key.key.getLocalizedText().getWithStyle(keyStyle).get(0));
        if (mappedKeyCount.containsKey(key.keyCode)) {
            ArrayList<KeyBinding> kbs = mappedKeyCount.get(key.keyCode);

            if (kbs.size() > 0) {
                tooltips.add(new LiteralText("----------------").getWithStyle(separatorStyle).get(0));
            }

            for (KeyBinding kb : kbs) {
                tooltips.add(new TranslatableText(kb.getTranslationKey()).getWithStyle(keyBindStyle).get(0));
            }
        }
    }

    @Override
    public ArrayList<Text> getToolTips() {
        return tooltips;
    }

    @Override
    public void renderButton(MatrixStack m, int mouseX, int mouseY, float delta) {
        ColorGroup color = selected ? COL_SELECTED : colors;
        updateState();
        int cBorder = color.border.getVariant(enabled, mouseActive, hovered);
        int cFill = color.bg.getVariant(enabled, mouseActive, hovered);
        int cText = color.text.getVariant(enabled, mouseActive, hovered);
        drawBoxFilled(m, x, y, width, height, cBorder, cFill);
        drawWithShadow(m, key.text, x + halfWidth, y + halfHeight, cText, true, true);
    }
}
