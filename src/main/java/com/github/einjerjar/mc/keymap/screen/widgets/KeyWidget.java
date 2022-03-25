package com.github.einjerjar.mc.keymap.screen.widgets;

import com.github.einjerjar.mc.keymap.keys.KeyboardLayout;
import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.Utils;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

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
        tooltips.add(key.key.getLocalizedText().getWithStyle(Utils.styleKey).get(0));
        if (mappedKeyCount.containsKey(key.keyCode)) {
            ArrayList<KeyBinding> kbs = mappedKeyCount.get(key.keyCode);

            int maxChars = 0;
            int maxWidth = 0;
            for (KeyBinding kb : kbs) {
                Text t = new TranslatableText(kb.getTranslationKey()).getWithStyle(Utils.styleKeybind).get(0);
                maxChars = t.getString().length();
                maxWidth = tr.getWidth(t);
                tooltips.add(t);
            }
            if (kbs.size() > 0) {
                tooltips.add(new LiteralText(tr.trimToWidth("-".repeat(maxChars), maxWidth)).getWithStyle(Utils.styleSeparator).get(0));
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
