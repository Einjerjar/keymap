package com.github.einjerjar.mc.keymap.widgets;

import com.github.einjerjar.mc.keymap.screen.Tooltipped;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.List;

public class FlatScreen extends Screen {
    protected Screen parent;

    protected Element hovered;

    protected FlatScreen(Text title) {
        super(title);
    }

    protected FlatScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    @Override
    public void onClose() {
        if (parent != null) {
            //noinspection ConstantConditions
            client.setScreen(parent);
            return;
        }
        super.onClose();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        // super.render(matrices, mouseX, mouseY, delta);

        for (Element e : children()) {
            if (e instanceof FlatWidget<?> ee) {
                if (ee.visible) {
                    ee.render(matrices, mouseX, mouseY, delta);
                }
            }
        }

        hovered = null;

        for (FlatWidgetBase c : children().stream().filter(e -> e instanceof FlatWidgetBase).map(e -> (FlatWidgetBase) e).toList()) {
            if (!c.enabled) continue;
            if (c.isMouseOver(mouseX, mouseY)) {
                hovered = c;
            }
        }
    }

    public void renderTooltips(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (hovered != null && hovered instanceof FlatWidgetBase fw) {
            if (fw instanceof Tooltipped tipped) {
                List<Text> tips = tipped.getToolTips();
                if (tips != null) {
                    renderTooltip(matrices, tipped.getToolTips(), mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (hovered != null) {
            if (hovered.mouseClicked(mouseX, mouseY, button)) {
                setFocused(hovered);
                setDragging(true);
                return true;
            }
        }
        setFocused(null);
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        Element focus = getFocused();
        setDragging(false);
        if (focus != null && focus != hovered) {
            focus.mouseReleased(mouseX, mouseY, button);
        }
        // if (focus != null) KeymapMain.LOGGER.info(focus.getClass().getName());
        // if (hovered != null) KeymapMain.LOGGER.info(hovered.getClass().getName());
        if (hovered != null) return hovered.mouseReleased(mouseX, mouseY, button);
        return false;
    }

    // @Override
    // public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    //     if (keyCode == InputUtil.GLFW_KEY_ESCAPE && shouldCloseOnEsc()) {
    //         onClose();
    //         return true;
    //     };
    //     if (keyCode == InputUtil.GLFW_KEY_TAB) {
    //         changeFocus(!hasShiftDown());
    //         return false;
    //     }
    //     return super.keyPressed(keyCode, scanCode, modifiers);
    // }
}
