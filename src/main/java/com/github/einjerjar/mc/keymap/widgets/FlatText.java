package com.github.einjerjar.mc.keymap.widgets;

import com.github.einjerjar.mc.keymap.utils.WidgetUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class FlatText extends FlatWidget<FlatText> {
    protected Text text;

    public FlatText(int x, int y, int w, int h, Text text) {
        super(FlatText.class, x, y, w, h);
        this.text = text;
        this.drawBg(false)
            .drawBorder(false)
            .drawShadow(true);
    }

    public Text getText() {
        return text;
    }

    public FlatText setText(Text text) {
        this.text = text;
        return this;
    }

    @Override
    public void renderWidget(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (drawBg) WidgetUtils.fillBox(this, matrices, x, y, w, h, color.bg.normal);
        if (drawBorder) WidgetUtils.fillBox(this, matrices, x, y, w, h, color.border.normal);
        drawCenteredText(matrices, text, x, y, drawShadow, color.text.normal);
    }
}
