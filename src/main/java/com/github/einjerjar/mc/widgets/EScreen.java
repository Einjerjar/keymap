package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.widgets.utils.ColorGroups;
import com.github.einjerjar.mc.widgets.utils.Rect;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@Accessors(fluent = true, chain = true)
public abstract class EScreen extends Screen {
    protected boolean autoRenderChild = true;
    protected boolean clickState      = false;
    protected EWidget hoveredWidget   = null;
    protected ELabel  debugFocus;
    protected ELabel  debugHover;
    protected boolean renderBg        = true;
    @Getter   Screen  parent;

    protected EScreen(Screen parent, Component text) {
        super(text);
        this.parent = parent;
    }

    @Override protected void init() {
        debugFocus = new ELabel(new TextComponent("focused"), 0, 4, width, font.lineHeight);
        debugHover = new ELabel(new TextComponent("hovered"), 0, 14, width, font.lineHeight);
        debugFocus.color(ColorGroups.WHITE);
        debugHover.color(ColorGroups.WHITE);
        debugFocus.center(true);
        debugHover.center(true);

        onInit();
    }

    protected abstract void onInit();


    @Override public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        EWidget focus = (EWidget) getFocused();
        if (focus != null && focus.keyPressed(keyCode, scanCode, modifiers)) return true;
        if (keyCode == InputConstants.KEY_ESCAPE) {
            onClose();
            return true;
        }
        return false;
    }

    protected boolean onCharTyped(char chr, int modifiers) {
        EWidget focus = (EWidget) getFocused();
        if (focus != null) return focus.charTyped(chr, modifiers);
        return false;
    }

    @Override public boolean charTyped(char chr, int modifiers) {
        return onCharTyped(chr, modifiers);
    }

    @Override public void onClose() {
        assert minecraft != null;
        minecraft.setScreen(parent);
    }

    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        clickState = true;

        // Click always resets focused element
        EWidget focus = (EWidget) getFocused();
        setFocused(null);

        // if hover element != last focused, un-focus the other one
        if (focus != null && hoveredWidget != focus) {
            focus.focused(false);
            focus.mouseClicked(mouseX, mouseY, button);
        }

        // if hover element is valid, trigger it
        boolean ret = false;
        if (hoveredWidget != null) {
            hoveredWidget.focused(true);
            ret = hoveredWidget.mouseClicked(mouseX, mouseY, button);
            setFocused(hoveredWidget);
        }

        setDragging(true);

        // execute extra flow
        return onMouseClicked(mouseX, mouseY, button) || ret;
    }

    public boolean onMouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override public boolean mouseReleased(double mouseX, double mouseY, int button) {
        setDragging(false);
        // Prevents flick from previous screen to pass through
        if (!clickState) return false;
        clickState = false;

        // Check if focus and current hover is the same
        EWidget focus = (EWidget) getFocused();

        // if hover and focus is not the same, ignore, else get return
        boolean ret = false;
        if (hoveredWidget != focus && focus != null) {
            focus.mouseReleased(mouseX, mouseY, button);
        }
        if (hoveredWidget != null) {
            ret = hoveredWidget.mouseReleased(mouseX, mouseY, button);
        }

        // execute extra flow
        return onMouseReleased(mouseX, mouseY, button) || ret;
    }

    public boolean onMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        EWidget focus = (EWidget) getFocused();
        if (focus == null) return false;
        return focus.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return onMouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    public boolean onMouseScrolled(double mouseX, double mouseY, double amount) {
        if (hoveredWidget == null) return false;
        return hoveredWidget.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return onMouseScrolled(mouseX, mouseY, amount);
    }

    public List<EWidget> widgets() {
        return children().stream().filter(EWidget.class::isInstance).map(EWidget.class::cast).toList();
    }

    @Override public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        if (renderBg) renderBackground(poseStack);
        hoveredWidget = null;
        if (autoRenderChild) {
            for (EWidget d : widgets()) {
                d.render(poseStack, mouseX, mouseY, partialTick);
                if (d.rect().contains(mouseX, mouseY)) {
                    hoveredWidget = d;
                }
            }
        }
        renderScreen(poseStack, mouseX, mouseY, partialTick);

        if (KeymapConfig.instance().debug2()) {
            fill(poseStack, 0, 0, width, 30, 0x66_000000);
            debugHover.text(new TextComponent(hoveredWidget != null ? hoveredWidget.getClass().getName() : "none"));
            debugFocus.text(new TextComponent(getFocused() != null ? getFocused().getClass().getName() : "none"));
            debugHover.render(poseStack, mouseX, mouseY, partialTick);
            debugFocus.render(poseStack, mouseX, mouseY, partialTick);
        }

        if (hoveredWidget != null && hoveredWidget.getTooltips() != null) renderTooltip(poseStack,
                hoveredWidget.getTooltips(),
                Optional.empty(),
                mouseX,
                mouseY);
    }

    protected abstract void renderScreen(PoseStack poseStack, int mouseX, int mouseY, float partialTick);

    public int left() {
        return 0;
    }

    public int top() {
        return 0;
    }

    public int right() {
        return width;
    }

    public int bottom() {
        return height;
    }

    public void drawOutline(PoseStack poseStack, int left, int top, int right, int bottom, int color) {
        hLine(poseStack, left, right, top, color);
        hLine(poseStack, left, right, bottom, color);
        vLine(poseStack, left, top, bottom, color);
        vLine(poseStack, right, top, bottom, color);
    }

    public void drawOutline(PoseStack poseStack, int color) {
        drawOutline(poseStack, left(), top(), right(), bottom(), color);
    }

    public void drawOutline(PoseStack poseStack, Rect r, int color) {
        drawOutline(poseStack, r.left(), r.top(), r.right(), r.bottom(), color);
    }

    public void drawBg(PoseStack poseStack, int left, int top, int right, int bottom, int color) {
        fill(poseStack, left, top, right, bottom, color);
    }

    public void drawBg(PoseStack poseStack, int color) {
        drawBg(poseStack, left(), top(), right(), bottom(), color);
    }
}
