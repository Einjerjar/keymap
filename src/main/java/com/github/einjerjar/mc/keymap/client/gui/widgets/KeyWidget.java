package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.keymap.keys.layout.KeyData;
import com.github.einjerjar.mc.widgets.EWidget;
import com.github.einjerjar.mc.widgets.utils.ColorSet;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.ArrayList;

@Accessors(fluent = true, chain = true)
public class KeyWidget extends EWidget {
    @Getter private KeyData key;
    @Getter private InputConstants.Key mcKey;
    @Setter private SimpleAction<KeyWidget> onClick;
    private Component text;

    public KeyWidget(KeyData key, int x, int y, int w, int h) {
        super(x, y, w, h);
        _init(key);
    }

    public KeyWidget(KeyData key, Rect rect) {
        super(rect);
        _init(key);
    }

    protected void _init(KeyData key) {
        this.key = key;
        this.text = new TextComponent(key.name());
        this.mcKey = getMCKey(key);
        this.tooltips = new ArrayList<>();
        this.tooltips.add(mcKey.getDisplayName());
    }

    protected InputConstants.Key getMCKey(KeyData key) {
        if (key.mouse()) return InputConstants.Type.MOUSE.getOrCreate(key.code());
        return InputConstants.Type.KEYSYM.getOrCreate(key.code());
    }

    @Override public boolean onMouseReleased(boolean inside, double mouseX, double mouseY, int button) {
        if (onClick != null) {
            onClick.run(this);
            return true;
        }
        return false;
    }

    @Override protected void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        ColorSet colors = colorVariant();
        drawBg(poseStack, colors.bg());
        drawOutline(poseStack, colors.border());
        drawCenteredString(poseStack, font, text, midX(), midY() - font.lineHeight / 2 + 1, colors.text());
    }
}
