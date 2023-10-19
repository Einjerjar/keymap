package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.widgets.utils.ColorSet;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true, chain = true)
public class EButton extends EWidget {
    @Getter
    @Setter
    Component text;

    @Setter
    SimpleWidgetAction<EWidget> clickAction;

    public EButton(Component text, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.text = text;
    }

    @Override
    public void setTooltip(Component tip) {
        super.setTooltip(tip);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ColorSet colors = colorVariant();
        drawBg(guiGraphics, colors.bg());
        drawOutline(guiGraphics, colors.border());
        guiGraphics.drawCenteredString(font, text, midX(), midY() - font.lineHeight / 2 + 1, colors.text());
    }

    @Override
    public boolean onMouseReleased(boolean inside, double mouseX, double mouseY, int button) {
        if (clickAction != null) {
            clickAction.run(this);
            return true;
        }
        return false;
    }
}
