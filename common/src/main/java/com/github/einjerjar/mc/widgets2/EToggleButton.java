package com.github.einjerjar.mc.widgets2;

import com.github.einjerjar.mc.widgets.utils.Text;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;

@Accessors(fluent = true)
public class EToggleButton extends EButton2<EToggleButton> implements EValueContainer<Boolean> {
    protected boolean value = false;

    @Getter @Setter EAction<EToggleButton> onToggle = null;

    public EToggleButton(Component text, int x, int y, int w, int h) {
        super(text, x, y, w, h);
        onClick(self -> {
            self.value(!self.value());
            if (onToggle != null) {
                onToggle.run(this);
            }
        });
    }

    @Override public Boolean value() {
        return value;
    }

    @Override public void value(Boolean v) {
        this.value = v;
    }

    @Override protected void onRenderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        drawCenteredString(poseStack, font, Text.literal("").append(text).append(": ").append(Boolean.TRUE.equals(value()) ? "ON" : "OFF"), center().x(), center().y() - font.lineHeight / 2 + 1, tColor());
    }
}
