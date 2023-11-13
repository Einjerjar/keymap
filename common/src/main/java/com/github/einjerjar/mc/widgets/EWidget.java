package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.widgets.utils.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true, chain = true)
public abstract class EWidget implements Renderable, GuiEventListener, NarratableEntry, Tooltipped {
    protected Font font = Minecraft.getInstance().font;

    @Getter
    @Setter
    protected ColorGroup color = ColorGroups.WHITE;

    @Getter
    @Setter
    protected Rect rect;

    @Getter
    @Setter
    protected boolean visible = true;

    @Getter
    @Setter
    protected boolean enabled = true;

    @Getter
    @Setter
    protected boolean focused = false;

    @Getter
    protected boolean active = false;

    @Getter
    protected boolean hovered = false;

    @Getter
    protected boolean allowRightClick = false;

    @Getter
    @Setter
    protected List<Component> tooltips;

    @Getter
    @Setter
    protected Point<Integer> padding = new Point<>(4);

    protected EWidget(int x, int y, int w, int h) {
        this.rect = new Rect(x, y, w, h);
    }

    protected EWidget(Rect rect) {
        this.rect = rect;
    }

    @Override
    public List<Component> getTooltips() {
        return tooltips;
    }

    protected void setTooltip(Component tip) {
        if (tooltips == null) {
            tooltips = new ArrayList<>();
        }
        tooltips.clear();
        tooltips.add(tip);
    }

    protected boolean onCharTyped(char codePoint, int modifiers) {
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (enabled() && visible() && focused()) return onCharTyped(codePoint, modifiers);
        return false;
    }

    protected void init() {
        //    override for widget's custom initializer
        //    afaik, no particular purpose, but eh, I'll keep it for now
    }

    protected ColorSet colorVariant() {
        return color.getVariant(hovered, active, !enabled);
    }

    @Override
    public void setFocused(boolean focused) {
        focused(focused);
    }

    @Override
    public boolean isFocused() {
        return focused();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!visible) return;
        hovered = isMouseOver(mouseX, mouseY);
        if (KeymapConfig.instance().debug()) {
            drawOutline(guiGraphics, 0x44_ff0000);
        }
        renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    public void updateTooltips() {
        //    override to allow external components to update any widget's tooltip (if any)
    }

    public boolean onMouseClicked(boolean inside, double mouseX, double mouseY, int button) {
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!hovered) {
            active = false;
            onMouseClicked(false, mouseX, mouseY, button);
            return false;
        }
        if (!allowRightClick && button != 0) return false;
        playSound(SoundEvents.UI_BUTTON_CLICK.value());
        this.active = true;
        return onMouseClicked(true, mouseX, mouseY, button);
    }

    public boolean onMouseReleased(boolean inside, double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.active = false;
        if (!hovered) {
            onMouseReleased(false, mouseX, mouseY, button);
            return false;
        }
        if (!allowRightClick && button != 0) return false;
        return onMouseReleased(true, mouseX, mouseY, button);
    }

    protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) return false;
        return onKeyPressed(keyCode, scanCode, modifiers);
    }

    protected boolean onMouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (!enabled) return false;
        return onMouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    protected boolean onMouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (!enabled) return false;
        return onMouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    // ------------------------------------
    // Override by subclass
    // ------------------------------------

    protected abstract void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

    // ------------------------------------
    // Utils
    // ------------------------------------

    public int top() {
        return rect.top();
    }

    public int bottom() {
        return rect.bottom();
    }

    public int left() {
        return rect.left();
    }

    public int right() {
        return rect.right();
    }

    public int midX() {
        return rect.midX();
    }

    public int midY() {
        return rect.midY();
    }

    public void drawOutline(@NotNull GuiGraphics guiGraphics, int left, int top, int right, int bottom, int color) {
        guiGraphics.hLine(left, right, top, color);
        guiGraphics.hLine(left, right, bottom, color);
        guiGraphics.vLine(left, top, bottom, color);
        guiGraphics.vLine(right, top, bottom, color);
    }

    public void drawOutline(@NotNull GuiGraphics guiGraphics, int color) {
        drawOutline(guiGraphics, left(), top(), right(), bottom(), color);
    }

    public void drawOutline(@NotNull GuiGraphics guiGraphics) {
        drawOutline(guiGraphics, colorVariant().border());
    }

    public void drawBg(@NotNull GuiGraphics guiGraphics, int left, int top, int right, int bottom, int color) {
        guiGraphics.fill(left, top, right, bottom, color);
    }

    public void drawBg(@NotNull GuiGraphics guiGraphics, int color) {
        drawBg(guiGraphics, left(), top(), right(), bottom(), color);
    }

    public void drawBg(@NotNull GuiGraphics guiGraphics) {
        drawBg(guiGraphics, colorVariant().bg());
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return rect.contains(mouseX, mouseY);
    }

    protected void playSound(SoundEvent sound, float pitch, float volume) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(sound, pitch, volume));
    }

    protected void playSound(SoundEvent sound, float pitch) {
        playSound(sound, pitch, 0.25f);
    }

    protected void playSound(SoundEvent sound) {
        playSound(sound, 1f);
    }

    @Override
    public NarrationPriority narrationPriority() {
        if (focused) return NarrationPriority.FOCUSED;
        if (hovered) return NarrationPriority.HOVERED;
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput narrationElementOutput) {
        //    IDK, but required by an extended/implemented class, so yeah
        //    tho method name is looks descriptive enough
    }

    public interface SimpleWidgetAction<T> {
        void run(T source);
    }
}
