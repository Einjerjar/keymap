package com.github.einjerjar.mc.keymap.client.gui.widgets;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.keys.layout.KeyData;
import com.github.einjerjar.mc.keymap.keys.layout.KeyRow;
import com.github.einjerjar.mc.widgets.EWidget;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true, chain = true)
public class VirtualKeyboardWidget extends EWidget {
    @Getter @Setter protected int                                       gap = 2;
    @Setter protected         SimpleWidgetAction<VirtualKeyboardWidget> onKeyClicked;
    @Setter protected         SpecialVKKeyClicked                       onSpecialKeyClicked;

    @Getter protected       KeyWidget       lastActionFrom;
    @Getter protected final List<KeyWidget> childKeys = new ArrayList<>();

    protected final List<KeyRow> keys;

    public VirtualKeyboardWidget(List<KeyRow> keys, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.keys = keys;
        init();
    }

    @Override public boolean isMouseOver(double mouseX, double mouseY) {
        return false;
    }

    public VirtualKeyboardWidget destroy() {
        for (KeyWidget childKey : childKeys) {
            childKey.destroy();
        }
        return this;
    }

    protected void _onKeyClicked(KeyWidget source) {
        lastActionFrom = source;
        if (onKeyClicked != null) onKeyClicked.run(this);
        lastActionFrom = null;
    }

    protected void _onSpecialKeyClicked(KeyWidget source, int button) {
        // redundant check for my sanity
        if (source.isNormal()) {
            Keymap.logger().warn("False Special");
            _onKeyClicked(source);
            return;
        }
        lastActionFrom = source;
        // Other mouse keys
        if (onSpecialKeyClicked != null) onSpecialKeyClicked.run(this, source, button);
        lastActionFrom = null;
    }

    @Override protected void init() {
        int w        = 16;
        int h        = 16;
        int currentX = left();
        int currentY = top();

        int maxX = 0;

        for (KeyRow kk : keys) {
            for (KeyData k : kk.row()) {
                int       ww = w + k.width();
                int       hh = h + k.height();
                KeyWidget kw = new KeyWidget(k, currentX, currentY, ww, hh);
                kw.onClick(this::_onKeyClicked);
                kw.onSpecialClick(this::_onSpecialKeyClicked);

                childKeys.add(kw);
                currentX += gap + ww;
            }
            maxX     = Math.max(currentX - gap, maxX);
            currentX = left();
            currentY += gap + h;
        }

        currentY -= gap;
        rect.w(maxX - left());
        rect.h(currentY - top());
    }

    @Override protected void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        for (KeyWidget kw : childKeys) {
            kw.render(poseStack, mouseX, mouseY, partialTick);
        }
    }

    public interface SpecialVKKeyClicked {
        void run(VirtualKeyboardWidget source, KeyWidget keySource, int button);
    }
}
