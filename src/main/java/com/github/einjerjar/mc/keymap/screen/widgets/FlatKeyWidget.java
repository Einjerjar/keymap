package com.github.einjerjar.mc.keymap.screen.widgets;

import com.github.einjerjar.mc.keymap.keys.KeybindHolder;
import com.github.einjerjar.mc.keymap.keys.KeyboardKey;
import com.github.einjerjar.mc.keymap.utils.ColorGroup;
import com.github.einjerjar.mc.keymap.utils.Utils;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import com.github.einjerjar.mc.keymap.widgets.FlatButton;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Map;

@Accessors(fluent = true, chain = true)
public class FlatKeyWidget extends FlatButton {
    protected static final ColorGroup COL_NORMAL = ColorGroup.NORMAL;
    protected static final ColorGroup COL_SET = ColorGroup.GREEN;
    protected static final ColorGroup COL_CONFLICT = ColorGroup.RED;
    protected static final ColorGroup COL_SELECTED = ColorGroup.YELLOW;

    protected final Map<Integer, List<KeybindHolder>> mappedKeybindHolders;
    protected final Text displayText;

    @Getter protected KeyboardKey key;
    @Getter @Setter boolean selected = false;

    public FlatKeyWidget(int x, int y, KeyboardKey key, Map<Integer, List<KeybindHolder>> mappedKeys) {
        super(x, y, 16 + key.extraWidth(), 16 + key.extraHeight(), Text.of(""));
        this.key = key;
        this.enabled = key.enabled();
        this.mappedKeybindHolders = mappedKeys;
        this.displayText = Text.of(key.text().toUpperCase());
        updateState();
    }

    public void updateState() {
        int keyCode = key.keyCode();
        if (mappedKeybindHolders == null) {
            color = COL_NORMAL;
            return;
        }
        if (!mappedKeybindHolders.containsKey(keyCode)) color = COL_NORMAL;
        else if (selected) color = COL_SELECTED;
        else {
            int count = mappedKeybindHolders.get(keyCode).size();
            if (count == 0) color = COL_NORMAL;
            else if (count == 1) color = COL_SET;
            else {
                color = COL_CONFLICT;
            }
        }

        tooltips.clear();
        try {
            tooltips.add(key.key().getLocalizedText().getWithStyle(Utils.styleKey).get(0));
        } catch (Exception e) {
            tooltips.add(Text.of(key.text()).getWithStyle(Utils.styleKey).get(0));
        }
        if (mappedKeybindHolders.containsKey(key.keyCode())) {
            List<KeybindHolder> kbs = mappedKeybindHolders.get(key.keyCode());

            int maxChars = tooltips.get(0).getString().length();
            int maxWidth = tr.getWidth(tooltips.get(0));
            for (KeybindHolder kb : kbs) {
                Text t = Text.translatable(kb.translationKey()).getWithStyle(Utils.styleKeybind).get(0);
                maxChars = Math.max(t.getString().length(), maxChars);
                maxWidth = Math.max(tr.getWidth(t), maxWidth);
                tooltips.add(t);
            }
            if (kbs.size() > 0) {
                tooltips.add(1, Text.of(tr.trimToWidth("-".repeat(maxChars), maxWidth)).getWithStyle(Utils.styleSeparator).get(0));
            }
        }
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int cBg   = color.bg.getVariant(enabled, focused, hovered);
        int cBor  = color.border.getVariant(enabled, focused, hovered);
        int cText = color.text.getVariant(enabled, focused, hovered);

        WidgetUtils.drawBoxFilled(this, matrices, x, y, w, h, cBor, cBg);
        drawCenteredText(matrices, displayText, x, y, true, cText);
    }
}
