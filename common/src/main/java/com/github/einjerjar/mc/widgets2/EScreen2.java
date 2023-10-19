package com.github.einjerjar.mc.widgets2;

import com.mojang.blaze3d.platform.InputConstants;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Accessors(fluent = true)
public abstract class EScreen2 extends EScreen2Utils {

    protected EScreen2(Screen parent) {
        this(parent, Component.translatable("SCREEN"));
        assert minecraft != null;
    }

    protected EScreen2(Screen parent, Component text) {
        super(text);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int tw = targetScreenWidth == -1 ? width : targetScreenWidth;
        scr = scrFromWidth(Math.min(tw, width));
        children.clear();

        onInit();

        for (EWidget2 child : children) {
            child.init();
        }
    }

    protected abstract void onInit();

    protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    protected boolean onCharTyped(char codePoint, int modifiers) {
        return false;
    }

    protected boolean onMouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    protected boolean onMouseReleased(double mouseX, double mouseY, int button) {
        switch (button) {
            case 0 -> {
                return onLeftMouseReleased(mouseX, mouseY, button);
            }
            case 1 -> {
                return onRightMouseReleased(mouseX, mouseY, button);
            }
            case 2 -> {
                return onMiddleMouseReleased(mouseX, mouseY, button);
            }
            default -> {
                return onOtherMouseReleased(mouseX, mouseY, button);
            }
        }
    }

    protected boolean onLeftMouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    protected boolean onMiddleMouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    protected boolean onRightMouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    protected boolean onOtherMouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    protected boolean onMouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    protected boolean onMouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }

    protected void preRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
    }

    protected void onRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        hoverWidget(null);
        for (EWidget2 w : children) {
            if (w.rect.contains(mouseX, mouseY)) hoverWidget(w);
            w.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    protected void postRender(GuiGraphics poseStack, int mouseX, int mouseY, float partialTick) {}

    @Override
    public void onClose() {
        assert minecraft != null;
        minecraft.setScreen(parent);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (focusWidget() != null && focusWidget().keyPressed(keyCode, scanCode, modifiers)) return true;

        if (keyCode == InputConstants.KEY_ESCAPE) {
            boolean r = false;
            if (focusWidget() != null) {
                if (focusWidget().escape()) r = true;
                focusWidget(null);
            }
            if (!r) onClose();
            return true;
        }

        return onKeyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (focusWidget() != null && focusWidget().charTyped(codePoint, modifiers)) return true;
        return onCharTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        clickState = true;

        if (focusWidget() != null && hoverWidget() != focusWidget()) {
            focusWidget().mouseClicked(mouseX, mouseY, button);
            focusWidget(null);
        }

        if (hoverWidget() != null && hoverWidget().mouseClicked(mouseX, mouseY, button)) {
            activeWidget(hoverWidget());
            focusWidget(hoverWidget());
            return true;
        }

        return onMouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!clickState) return false;
        clickState = false;

        EWidget2 active = activeWidget();

        boolean r = false;

        if (hoverWidget() != active && active != null) {
            // call fw to handle reset if not hovered
            active.mouseReleased(mouseX, mouseY, button);
            focusWidget(null);
        } else if (hoverWidget() != null && hoverWidget().mouseReleased(mouseX, mouseY, button)) {
            // call hw only if eq to fw
            r = true;
        }

        activeWidget(null);

        return r || onMouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!clickState) return false;

        return onMouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return onMouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        preRender(guiGraphics, mouseX, mouseY, partialTick);
        onRender(guiGraphics, mouseX, mouseY, partialTick);
        postRender(guiGraphics, mouseX, mouseY, partialTick);
    }
}
