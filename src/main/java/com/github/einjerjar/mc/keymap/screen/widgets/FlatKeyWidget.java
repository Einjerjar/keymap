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
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        super(x, y, 16 + key.extraWidth(), 16 + key.extraHeight(), new LiteralText(""));
        this.key = key;
        this.enabled = key.enabled();
        this.mappedKeybindHolders = mappedKeys;
        this.displayText = this.setDisplayText();
        updateState();
    }

    public void updateState() {
        int keyCode = key.keyCode();
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
            tooltips.add(new LiteralText(key.text()).getWithStyle(Utils.styleKey).get(0));
        }
        if (mappedKeybindHolders.containsKey(key.keyCode())) {
            List<KeybindHolder> kbs = mappedKeybindHolders.get(key.keyCode());

            int maxChars = tooltips.get(0).getString().length();
            int maxWidth = tr.getWidth(tooltips.get(0));
            for (KeybindHolder kb : kbs) {
                Text t = new TranslatableText(kb.translationKey()).getWithStyle(Utils.styleKeybind).get(0);
                maxChars = Math.max(t.getString().length(), maxChars);
                maxWidth = Math.max(tr.getWidth(t), maxWidth);
                tooltips.add(t);
            }
            if (kbs.size() > 0) {
                tooltips.add(1, new LiteralText(tr.trimToWidth("-".repeat(maxChars), maxWidth)).getWithStyle(Utils.styleSeparator).get(0));
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

    private Text setDisplayText() {
        Text keyText = new LiteralText(this.key.text());
        switch (this.key.key().getTranslationKey()) {
            case "key.mouse.left":
            case "key.mouse.right":
            case "key.mouse.middle":
            case "key.mouse.4":
            case "key.mouse.5":
            case "key.mouse.6":
            case "key.mouse.7":
            case "key.mouse.8":
            case "key.keyboard.unknown":
            case "key.keyboard.num.lock":
            case "key.keyboard.keypad.enter":
            case "key.keyboard.down":
            case "key.keyboard.left":
            case "key.keyboard.right":
            case "key.keyboard.up":
            case "key.keyboard.space":
            case "key.keyboard.tab":
            case "key.keyboard.left.alt":
            case "key.keyboard.left.control":
            case "key.keyboard.left.shift":
            case "key.keyboard.left.win":
            case "key.keyboard.right.alt":
            case "key.keyboard.right.control":
            case "key.keyboard.right.shift":
            case "key.keyboard.right.win":
            case "key.keyboard.enter":
            case "key.keyboard.escape":
            case "key.keyboard.backspace":
            case "key.keyboard.delete":
            case "key.keyboard.end":
            case "key.keyboard.home":
            case "key.keyboard.insert":
            case "key.keyboard.page.down":
            case "key.keyboard.page.up":
            case "key.keyboard.caps.lock":
            case "key.keyboard.pause":
            case "key.keyboard.scroll.lock":
            case "key.keyboard.menu":
            case "key.keyboard.print.screen":
            case "key.keyboard.world.1":
            case "key.keyboard.world.2":
                break;
            default:
                if (!Objects.equals(this.key.text(), "ML") && !Objects.equals(this.key.text(), "MM") && !Objects.equals(this.key.text(), "MR")) {
                    keyText = new LiteralText(this.key.key().getLocalizedText().getString().toUpperCase());
                }
                break;
        }

        return keyText;
    }
}
