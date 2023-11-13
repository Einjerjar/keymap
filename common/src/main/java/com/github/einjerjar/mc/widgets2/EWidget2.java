package com.github.einjerjar.mc.widgets2;

import com.github.einjerjar.mc.keymap.Keymap;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

@Accessors(fluent = true)
public abstract class EWidget2 extends EWidget2Utils {
    protected EWidget2(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    protected void init() {}

    protected void updateTooltips() {}

    protected boolean onMouseClicked(double mouseX, double mouseY, int button) {
        return true;
    }

    private boolean handleMouseClick(double mouseX, double mouseY, int button) {
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

    protected boolean onMouseReleased(double mouseX, double mouseY, int button) {
        if (handleMouseClick(mouseX, mouseY, button)) {
            playSound(SoundEvents.UI_BUTTON_CLICK.value());
            return true;
        }
        return false;
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

    protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    protected boolean onCharTyped(char codePoint, int modifiers) {
        return false;
    }

    protected boolean onMouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    protected boolean onMouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return false;
    }

    protected void preRenderWidget(@NotNull GuiGraphics poseStack, int mouseX, int mouseY, float partialTick) {}

    protected abstract void onRenderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

    protected void postRenderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {}

    private boolean onEscape() {
        return false;
    }

    public boolean escape() {
        return onEscape();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!rect.contains(mouseX, mouseY)) return false;

        return onMouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!active()) return false;

        if (!rect.contains(mouseX, mouseY)) {
            Keymap.logger().warn("1111");
            return false;
        }

        return onMouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return onMouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return onMouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return onKeyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return onCharTyped(codePoint, modifiers);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        preRenderWidget(guiGraphics, mouseX, mouseY, partialTick);
        onRenderWidget(guiGraphics, mouseX, mouseY, partialTick);
        postRenderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }
}
