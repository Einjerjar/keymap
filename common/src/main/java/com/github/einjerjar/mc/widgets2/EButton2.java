package com.github.einjerjar.mc.widgets2;

import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;

@Accessors(fluent = true)
public class EButton2<T extends EButton2> extends EWidget2 {
    @Getter @Setter protected Component text;

    @Setter protected EAction<T> onClick      = null;
    @Setter protected EAction<T> onRightClick = null;

    public EButton2(Component text, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.text(text);
    }

    @Override protected boolean onLeftMouseReleased(double mouseX, double mouseY, int button) {
        if (onClick != null) {
            onClick.run((T) this);
            return true;
        }
        return false;
    }

    @Override protected boolean onRightMouseReleased(double mouseX, double mouseY, int button) {
        if (onRightClick != null) {
            onRightClick.run((T) this);
            return true;
        }
        return false;
    }

    @Override protected void preRenderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        U.bbg(poseStack, rect, ColorOption.baseBG, ColorOption.baseFG, state());
    }

    @Override protected void onRenderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        drawCenteredString(poseStack, font, text, center().x(), center().y() - font.lineHeight / 2 + 1, tColor());
    }
}
