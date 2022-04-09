package com.github.einjerjar.mc.keymap.widgets;

import com.github.einjerjar.mc.keymap.screen.Tooltipped;
import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@SuppressWarnings("UnusedReturnValue")
@Accessors(fluent = true, chain = true)
public class FlatButton extends FlatSelectableWidget<FlatButton> implements Tooltipped {
    @Setter protected CommonAction action;
    @Setter protected Text text;

    public FlatButton(int x, int y, int w, int h, Text text) {
        super(FlatButton.class, x, y, w, h);
        this.text = text;
        this.drawBg(true)
            .drawBorder(true)
            .drawShadow(true);
    }

    public void click() {
        if (enabled) action.run(this);
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int cBg   = color.bg.getVariant(enabled, focused, hovered);
        int cBor  = color.border.getVariant(enabled, focused, hovered);
        int cText = color.text.getVariant(enabled, focused, hovered);

        if (drawBorder) WidgetUtils.fillBox(this, matrices, x, y, w, h, cBg);
        if (drawBg) WidgetUtils.drawBoxOutline(this, matrices, x, y, w, h, cBor);
        drawCenteredText(matrices, text, x, y, drawShadow, cText);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (hovered) {
            focused(true);
            playClickSound();
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        focused(false);
        if (hovered && action != null) {
            action.run(this);
            return true;
        }
        return false;
    }
}
