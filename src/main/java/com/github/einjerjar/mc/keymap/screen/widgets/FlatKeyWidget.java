package com.github.einjerjar.mc.keymap.screen.widgets;

import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import com.github.einjerjar.mc.keymap.keys.KeyboardLayout;
import com.github.einjerjar.mc.keymap.screen.Tooltipped;
import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.FlatButton;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlatKeyWidget extends FlatButton implements Tooltipped {
    public KeyboardLayout.KeyboardKey key;
    private Map<Integer, List<KeybindHolder>> mappedKeybindHolders;

    private final List<Text> tooltips = new ArrayList<>();

    static final ColorGroup COL_NORMAL = ColorGroup.NORMAL;
    static final ColorGroup COL_SET = ColorGroup.GREEN;
    static final ColorGroup COL_CONFLICT = ColorGroup.RED;
    static final ColorGroup COL_SELECTED = ColorGroup.YELLOW;

    public boolean selected = false;

    private final Text displayText;

    public FlatKeyWidget(int x, int y, KeyboardLayout.KeyboardKey key, Map<Integer, List<KeybindHolder>> mappedKeys) {
        super(x, y, 16 + key.extraWidth, 16 + key.extraHeight, new LiteralText(""));
        this.key = key;
        this.mappedKeybindHolders = mappedKeys;
        this.displayText = new LiteralText(key.text);
        updateState();
    }

    public void updateState() {
        int keyCode = key.keyCode;
        if (!mappedKeybindHolders.containsKey(keyCode)) color = COL_NORMAL;
        else {
            int count = mappedKeybindHolders.get(keyCode).size();
            if (count == 0) color = COL_NORMAL;
            else if (count == 1) color = COL_SET;
            else color = COL_CONFLICT;
        }

        tooltips.clear();
        tooltips.add(key.key.getLocalizedText().getWithStyle(Utils.styleKey).get(0));
        if (mappedKeybindHolders.containsKey(key.keyCode)) {
            List<KeybindHolder> kbs = mappedKeybindHolders.get(key.keyCode);

            int maxChars = 0;
            int maxWidth = 0;
            for (KeybindHolder kb : kbs) {
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
    public List<Text> getToolTips() {
        return tooltips;
    }

    @Override
    public Text getFirstToolTip() {
        return Utils.or(tooltips.get(0), null);
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        ColorGroup colors = selected ? COL_SELECTED : color;

        int cBg   = color.bg.getVariant(enabled, focused, hovered);
        int cBor  = color.border.getVariant(enabled, focused, hovered);
        int cText = color.text.getVariant(enabled, focused, hovered);

        WidgetUtils.drawBoxFilled(this, matrices, x, y, w, h, cBor, cBg);
        drawCenteredText(matrices, displayText, x, y, true, cText);
    }
}
