package com.github.einjerjar.mc.keymap.widgets;

import com.github.einjerjar.mc.keymap.screen.Tooltipped;
import com.github.einjerjar.mc.keymap.screen.containers.HotkeyCapture;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Accessors(fluent = true, chain = true)
public abstract class FlatScreen extends Screen {
    protected Screen parent;
    protected Element hovered;
    // prevent ghost mouse release from the previous screen
    protected boolean regClick = false;
    protected boolean darkBg = true;

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
                if (ee.visible()) {
                    ee.render(matrices, mouseX, mouseY, delta);
                }
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        if (darkBg) fill(matrices, 0, 0, width, height, 0x55000000);

        hovered = null;

        for (FlatWidgetBase c : children().stream().filter(e -> e instanceof FlatWidgetBase).map(e -> (FlatWidgetBase) e).toList()) {
            if (!c.enabled) continue;
            if (c.isMouseOver(mouseX, mouseY)) {
                hovered = c;
            }
        }

        renderScreen(matrices, mouseX, mouseY, delta);
    }

    public abstract void renderScreen(MatrixStack matrices, int mouseX, int mouseY, float delta);

    public void renderTooltips(MatrixStack matrices, int mouseX, int mouseY) {
        if (hovered != null && hovered instanceof Tooltipped tipped) {
            List<Text> tips = tipped.getToolTips();
            if (tips != null) {
                renderTooltip(matrices, tipped.getToolTips(), mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        regClick = true;
        if (hovered != null) {
            if (hovered.mouseClicked(mouseX, mouseY, button)) {
                setFocused(hovered);
                setDragging(true);
                return wMouseClicked(mouseX, mouseY, button);
            }
        }
        setFocused(null);
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!regClick) return false;
        regClick = false;
        setDragging(false);
        return wMouseReleased(mouseX, mouseY, button);
    }

    public boolean wMouseClicked(double mouseX, double mouseY, int button) {
        return true;
    }

    public boolean wMouseReleased(double mouseX, double mouseY, int button) {
        Element focus = getFocused();
        if (focus == null) return false;
        if (focus == hovered) {
            return hovered.mouseReleased(mouseX, mouseY, button);
        }
        focus.mouseReleased(mouseX, mouseY, button);
        return false;
    }

    protected Element drillFocused(Element focused) {
        if (focused instanceof FlatContainer fc) {
            if (fc.focusedElement != null) return drillFocused(fc.focusedElement);
        }
        return focused;
    }

    public void setFocused(@Nullable Element focused, boolean drill) {
        if (drill) {
            Element topFocus = null;
            Element drilled  = drillFocused(focused);
            if (!(drilled instanceof FlatContainer)) topFocus = drilled;
            if (drilled instanceof HotkeyCapture) topFocus = drilled;
            super.setFocused(topFocus);
            if (topFocus instanceof FlatWidgetBase tf) {
                tf.focused = true;
            }
            return;
        }
        super.setFocused(focused);
    }

    @Override
    public void setFocused(@Nullable Element focused) {
        setFocused(focused, true);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (getFocused() != null) {
            // KeymapMain.LOGGER().info(getFocused().getClass().getName());
            if (getFocused().keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE && shouldCloseOnEsc()) {
            onClose();
            return true;
        }
        if (keyCode == InputUtil.GLFW_KEY_TAB) {
            changeFocus(!hasShiftDown());
            return false;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
