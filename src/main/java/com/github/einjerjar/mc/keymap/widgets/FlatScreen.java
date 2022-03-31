package com.github.einjerjar.mc.keymap.widgets;

import com.github.einjerjar.mc.keymap.KeymapMain;
import com.github.einjerjar.mc.keymap.screen.Tooltipped;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

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

    protected void renderChildren(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        for (Element e : children()) {
            if (e instanceof FlatWidgetBase ee) {
                if (ee.visible) {
                    ee.render(matrices, mouseX, mouseY, delta);
                }
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
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
            KeymapMain.LOGGER.info(hovered.getClass().getName());
            if (hovered.mouseClicked(mouseX, mouseY, button)) {
                setFocused(hovered);
                setDragging(true);
                return true;
            }
        }
        setFocused(null);
        return false;
    }

    protected Element drillFocused(Element focused) {
        if (focused instanceof FlatContainer fc) {
            if (fc.focusedElement != null) return drillFocused(fc.focusedElement);
        }
        return focused;
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        super.setFocused(drillFocused(focused));
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

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        // if (keyCode == InputUtil.GLFW_KEY_ESCAPE && shouldCloseOnEsc()) {
        //     onClose();
        //     return true;
        // };
        // if (keyCode == InputUtil.GLFW_KEY_TAB) {
        //     changeFocus(!hasShiftDown());
        //     return false;
        // }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
